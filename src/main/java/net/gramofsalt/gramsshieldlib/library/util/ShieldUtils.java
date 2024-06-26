package net.gramofsalt.gramsshieldlib.library.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.gramofsalt.gramsshieldlib.GramsShieldLib;
import net.gramofsalt.gramsshieldlib.library.config.GramsShieldLibConfig;
import net.gramofsalt.gramsshieldlib.library.event.ShieldSetModelCallback;
import net.gramofsalt.gramsshieldlib.library.item.ModShield;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.ShieldEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BannerPatternsComponent;
import net.minecraft.component.type.DyedColorComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;

import java.text.DecimalFormat;
import java.util.*;

public class ShieldUtils {
    public static final Identifier VANILLA_SHIELD_TRIMS_ATLAS = Identifier.of(GramsShieldLib.MODID, "textures/atlas/vanilla_shield_trims");
    public static final Identifier VANILLA_SHIELD_PATTERNS_ATLAS = Identifier.of(GramsShieldLib.MODID, "textures/atlas/vanilla_shield_patterns");

    public static final EntityModelLayer VANILLA_SHIELD_MODEL_LAYER = new EntityModelLayer(Identifier.of(GramsShieldLib.MODID, "vanilla_shield"),"main");
    public static ShieldEntityModel VANILLA_SHIELD_MODEL;
    public static final SpriteIdentifier VANILLA_SHIELD_BASE = new SpriteIdentifier(VANILLA_SHIELD_PATTERNS_ATLAS, Identifier.of("entity/shield_base"));
    public static final SpriteIdentifier VANILLA_SHIELD_NO_PATTERN = new SpriteIdentifier(VANILLA_SHIELD_PATTERNS_ATLAS, Identifier.of("entity/shield_base_nopattern"));

    private static final Map<Identifier, SpriteIdentifier> SHIELD_PATTERN_TEXTURES = new HashMap<>();

    /**
     * Sets the vanilla shield model to use a new atlas. This is required to make trims work.
     */
    public static void registerVanillaShieldLayers() {
        EntityModelLayerRegistry.registerModelLayer(VANILLA_SHIELD_MODEL_LAYER, ShieldEntityModel::getTexturedModelData);
        ShieldSetModelCallback.EVENT.register((loader) -> {
            VANILLA_SHIELD_MODEL = new ShieldEntityModel(loader.getModelPart(VANILLA_SHIELD_MODEL_LAYER));
            return ActionResult.PASS;
        });
    }

    /**
     * Adds the stat tooltip to shield items. Called when initializing client.
     */
    public static void appendShieldTooltips() {
        ItemTooltipCallback.EVENT.register((stack, context, type, tooltip) -> {
            if (!GramsShieldLibConfig.enable_tooltip) return;
            // Appends modded shield tooltip
            if(stack.getItem() instanceof ModShield shield) {
                //Add custom shield tooltip
                if(shield.displayTooltip()) {
                    ShieldUtils.createShieldTooltip(stack, tooltip);
                }
            }
            //Display tooltip for vanilla shield
            if(stack.getItem() instanceof ShieldItem) {
                ShieldUtils.createShieldTooltip(stack,tooltip);
            }
        });
    }

    /**
     * Builds shield tooltip text.
     */
    public static void createShieldTooltip(ItemStack stack, List<Text> tooltip) {
        // Store advanced tooltip and remove it
        List<Text> advancedTooltip = grabAdvancedTooltip(stack, tooltip);

        // Tooltip Start
        tooltip.add(Text.literal(""));
        tooltip.add(Text.translatable(GramsShieldLib.MODID + ".shield_tooltip.start").append(Text.literal(":")).formatted(Formatting.GRAY));

        // Add blocking delay tooltip
        if (GramsShieldLibConfig.enable_delay_tooltip && ShieldAttributes.getModifierValue(stack, ShieldAttributes.SHIELD_DELAY_MODIFIER_ID) > 0) {
            tooltip.add(Text.literal(" " + buildString(ShieldAttributes.getModifierValue(stack, ShieldAttributes.SHIELD_DELAY_MODIFIER_ID) / 20.0))
                    .formatted(Formatting.DARK_GREEN)
                    .append(Text.translatable(GramsShieldLib.MODID + ".shield_tooltip.blocking_delay_unit"))
                    .append(Text.literal(" "))
                    .append(Text.translatable(GramsShieldLib.MODID + ".shield_tooltip.blocking_delay")));
        }

        // Add disabled cooldown tooltip
        if (GramsShieldLibConfig.enable_cooldown_tooltip && ShieldAttributes.getModifierValue(stack, ShieldAttributes.SHIELD_COOLDOWN_MODIFIER_ID) > 0) {
            tooltip.add(Text.literal(" " + buildString(ShieldAttributes.getModifierValue(stack, ShieldAttributes.SHIELD_COOLDOWN_MODIFIER_ID) / 20.0))
                    .formatted(Formatting.DARK_GREEN)
                    .append(Text.translatable(GramsShieldLib.MODID + ".shield_tooltip.cooldown_unit"))
                    .append(Text.literal(" "))
                    .append(Text.translatable(GramsShieldLib.MODID + ".shield_tooltip.cooldown")));
        }

        // Add Maximum Damage Tooltip
        if (GramsShieldLibConfig.enable_max_damage_tooltip && GramsShieldLibConfig.shield_behavior == GramsShieldLibConfig.BehaviorEnum.FULL_NERF && ShieldAttributes.getModifierValue(stack, ShieldAttributes.SHIELD_MAXIMUM_DAMAGE_MODIFIER_ID) != 0) {
            tooltip.add(Text.literal("-" + buildString(ShieldAttributes.getModifierValue(stack, ShieldAttributes.SHIELD_MAXIMUM_DAMAGE_MODIFIER_ID)))
                    .formatted(Formatting.DARK_GREEN)
                    .append(Text.literal(" "))
                    .append(Text.translatable(GramsShieldLib.MODID + ".shield_tooltip.maximum_damage")));
        }

        // Add Damage Resistance Tooltip
        if (GramsShieldLibConfig.enable_damage_resistance_tooltip && GramsShieldLibConfig.shield_behavior == GramsShieldLibConfig.BehaviorEnum.FULL_NERF && ShieldAttributes.getModifierValue(stack, ShieldAttributes.SHIELD_GENERIC_DAMAGE_RESISTANCE_MODIFIER_ID) > 0) {
            tooltip.add(Text.literal(" " + buildString(ShieldAttributes.getModifierValue(stack, ShieldAttributes.SHIELD_GENERIC_DAMAGE_RESISTANCE_MODIFIER_ID) * 100))
                    .formatted(Formatting.DARK_GREEN)
                    .append(Text.literal("%"))
                    .append(Text.literal(" "))
                    .append(Text.translatable(GramsShieldLib.MODID + ".shield_tooltip.damage_resistance")));
        }

        // Add Knockback Resistance Tooltip
        if (GramsShieldLibConfig.enable_knockback_resistance_tooltip && GramsShieldLibConfig.shield_behavior != GramsShieldLibConfig.BehaviorEnum.VANILLA && ShieldAttributes.getModifierValue(stack, ShieldAttributes.SHIELD_KNOCKBACK_RESISTANCE_MODIFIER_ID) > 0) {
            tooltip.add(Text.literal(" " + buildString(ShieldAttributes.getModifierValue(stack, ShieldAttributes.SHIELD_KNOCKBACK_RESISTANCE_MODIFIER_ID) * 100))
                    .formatted(Formatting.DARK_GREEN)
                    .append(Text.literal("%"))
                    .append(Text.literal(" "))
                    .append(Text.translatable(GramsShieldLib.MODID + ".shield_tooltip.knockback_resistance")));
        }

        // Add Explosion Resistance Tooltip
        if (GramsShieldLibConfig.enable_explosion_resistance_tooltip && GramsShieldLibConfig.shield_behavior == GramsShieldLibConfig.BehaviorEnum.SEMI_NERF && ShieldAttributes.getModifierValue(stack, ShieldAttributes.SHIELD_EXPLOSION_RESISTANCE_MODIFIER_ID) > 0) {
            tooltip.add(Text.literal(" " + buildString(ShieldAttributes.getModifierValue(stack, ShieldAttributes.SHIELD_EXPLOSION_RESISTANCE_MODIFIER_ID) * 100))
                    .formatted(Formatting.DARK_GREEN)
                    .append(Text.literal("%"))
                    .append(Text.literal(" "))
                    .append(Text.translatable(GramsShieldLib.MODID + ".shield_tooltip.explosion_resistance")));
        }


        if (GramsShieldLibConfig.enable_durability_multiplier_tooltip && ShieldAttributes.getModifierValue(stack, ShieldAttributes.SHIELD_DURABILITY_MULTIPLIER_MODIFIER_ID) != 1) {
            tooltip.add(Text.literal(" " + buildString(ShieldAttributes.getModifierValue(stack, ShieldAttributes.SHIELD_DURABILITY_MULTIPLIER_MODIFIER_ID)))
                    .formatted(Formatting.DARK_GREEN)
                    .append(Text.literal("×"))
                    .append(Text.literal(" "))
                    .append(Text.translatable(GramsShieldLib.MODID + ".shield_tooltip.durability_multiplier")));
        }

        if (GramsShieldLibConfig.enable_min_durability_damage_tooltip && ShieldAttributes.getModifierValue(stack, ShieldAttributes.SHIELD_MINIMUM_DURABILITY_DAMAGE_MODIFIER_ID) > 0) {
            tooltip.add(Text.literal(" " + buildString(ShieldAttributes.getModifierValue(stack, ShieldAttributes.SHIELD_MINIMUM_DURABILITY_DAMAGE_MODIFIER_ID)))
                    .formatted(Formatting.DARK_GREEN)
                    .append(Text.literal(" "))
                    .append(Text.translatable(GramsShieldLib.MODID + ".shield_tooltip.minimum_durability_damage")));
        }

        if (GramsShieldLibConfig.enable_movement_speed_multiplier_tooltip && ShieldAttributes.getModifierValue(stack, ShieldAttributes.SHIELD_BLOCKING_SPEED_MODIFIER_ID) > 0) {
        tooltip.add(Text.literal(" " + buildString(ShieldAttributes.getModifierValue(stack, ShieldAttributes.SHIELD_BLOCKING_SPEED_MODIFIER_ID) * 100))
                .formatted(Formatting.DARK_GREEN)
                .append(Text.literal("%"))
                .append(Text.literal(" "))
                .append(Text.translatable(GramsShieldLib.MODID + ".shield_tooltip.blocking_speed")));
        }

        // Append advanced tooltip
        tooltip.addAll(advancedTooltip);
    }

    /**
     * Find advanced tooltip lines, saves them, and removes them to be added after shield stats tooltip.
     */
    public static List<Text> grabAdvancedTooltip(ItemStack stack, List<Text> tooltip) {
        List<Text> advancedTooltip = new ArrayList<>();
        // These all loop in reverse to grab the first instance of a match at the end of the tooltip
        // Grab durability
        if(stack.isDamaged()) {
            for(int i = tooltip.size() - 1; i > 0; i--) {
                Text text = tooltip.get(i);
                String strText = text.getString();
                if(strText.startsWith("Durability")) {
                    advancedTooltip.add(text);
                    tooltip.remove(i);
                    break;
                }
            }
        }
        // Grab item id
        for(int i = tooltip.size() - 1; i > 0; i--) {
            Text text = tooltip.get(i);
            String strText = text.getString().trim();
            if(isValidIdentifier(strText)) {
                advancedTooltip.add(text);
                tooltip.remove(i);
                break;
            }
        }
        // Grab component string
        if(!stack.getComponents().isEmpty()) {
            for(int i = tooltip.size() - 1; i > 0; i--) {
                Text text = tooltip.get(i);
                String strText = text.getString();
                if(strText.contains("component(s)")) {
                    advancedTooltip.add(text);
                    tooltip.remove(i);
                    break;
                }
            }
        }
        return advancedTooltip;
    }

    public static StringBuilder buildString(double inputValue) {
        DecimalFormat formatter = new DecimalFormat("######.###");
        return new StringBuilder(formatter.format(inputValue));
    }

    public static boolean isValidIdentifier(String string) {
        boolean noNamespace = true;
        StringBuilder namespaceString = new StringBuilder();
        for(int i = 0; i < string.length(); i++) {
            if(string.charAt(i) == ':') {
                noNamespace = false;
                break;
            }
            else namespaceString.append(string.charAt(i));
        }
        if (!noNamespace) return Identifier.isNamespaceValid(namespaceString.toString());
        else return false;
    }

    /**
     * Renders banner on banner shield model.
     */
    @Environment(value = EnvType.CLIENT)
    public static void renderBanner(ItemStack stack, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, ShieldEntityModel model, SpriteIdentifier base, SpriteIdentifier base_nopattern){
        BannerPatternsComponent bannerPatternsComponent = stack.getOrDefault(DataComponentTypes.BANNER_PATTERNS, BannerPatternsComponent.DEFAULT);
        DyeColor dyeColor = stack.get(DataComponentTypes.BASE_COLOR);
        boolean bl = !bannerPatternsComponent.layers().isEmpty() || dyeColor != null;
        matrices.push();
        matrices.scale(1.0f, -1.0f, -1.0f);
        SpriteIdentifier spriteIdentifier = bl ? base : base_nopattern;
        VertexConsumer vertexConsumer = spriteIdentifier.getSprite().getTextureSpecificVertexConsumer(ItemRenderer.getDirectItemGlintConsumer(vertexConsumers, model.getLayer(spriteIdentifier.getAtlasId()), true, stack.hasGlint()));
        model.getHandle().render(matrices, vertexConsumer, light, overlay);
        if (bl) {
            renderCanvas(matrices, vertexConsumers, light, overlay, model.getPlate(), spriteIdentifier, Objects.requireNonNullElse(dyeColor, DyeColor.WHITE), bannerPatternsComponent, stack.hasGlint());
        } else {
            model.getPlate().render(matrices, vertexConsumer, light, overlay);
        }
        matrices.pop();
    }

    /**
     * Renders banner on banner shield model with armor trims on top.
     */
    @Environment(value = EnvType.CLIENT)
    public static void renderBannerWithTrims(ItemStack stack, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, ShieldEntityModel model, SpriteIdentifier base, SpriteIdentifier base_nopattern, Identifier trimsAtlas){
        BannerPatternsComponent bannerPatternsComponent = stack.getOrDefault(DataComponentTypes.BANNER_PATTERNS, BannerPatternsComponent.DEFAULT);
        ArmorTrim armorTrim = stack.get(DataComponentTypes.TRIM);
        DyeColor dyeColor = stack.get(DataComponentTypes.BASE_COLOR);
        boolean bl = !bannerPatternsComponent.layers().isEmpty() || dyeColor != null;
        matrices.push();
        matrices.scale(1.0f, -1.0f, -1.0f);
        SpriteIdentifier spriteIdentifier = bl ? base : base_nopattern;
        VertexConsumer vertexConsumer = spriteIdentifier.getSprite().getTextureSpecificVertexConsumer(ItemRenderer.getDirectItemGlintConsumer(vertexConsumers, model.getLayer(spriteIdentifier.getAtlasId()), true, stack.hasGlint()));
        model.getHandle().render(matrices, vertexConsumer, light, overlay);
        if (bl) {
            renderCanvas(matrices, vertexConsumers, light, overlay, model.getPlate(), spriteIdentifier, Objects.requireNonNullElse(dyeColor, DyeColor.WHITE), bannerPatternsComponent, stack.hasGlint());
            if (armorTrim != null) {
                renderTrimLayer(stack, matrices, vertexConsumers, light, overlay, model.getPlate(), armorTrim, trimsAtlas);
            }
        } else if (armorTrim != null) {
            renderTrimLayer(stack, matrices, vertexConsumers, light, overlay, model.getPlate(), spriteIdentifier, armorTrim, trimsAtlas, stack.hasGlint());
        } else {
            model.getPlate().render(matrices, vertexConsumer, light, overlay);
        }
        matrices.pop();
    }

    public static void renderCanvas(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, ModelPart canvas, SpriteIdentifier baseSprite, DyeColor color, BannerPatternsComponent patterns, boolean glint) {
        Identifier atlas = baseSprite.getAtlasId();
        SpriteIdentifier base = new SpriteIdentifier(atlas, Identifier.of(atlas.getNamespace(), "entity/shield/base"));
        canvas.render(matrices, baseSprite.getVertexConsumer(vertexConsumers, RenderLayer::getEntitySolid, glint), light, overlay);
        renderLayer(matrices, vertexConsumers, light, overlay, canvas, base, color);
        for (int i = 0; i < 16 && i < patterns.layers().size(); i++) {
            BannerPatternsComponent.Layer layer = patterns.layers().get(i);
            SpriteIdentifier spriteIdentifier = getShieldPatternTextureId(layer.pattern(), atlas);
            renderLayer(matrices, vertexConsumers, light, overlay, canvas, spriteIdentifier, layer.color());
        }
    }

    private static void renderLayer(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, ModelPart canvas, SpriteIdentifier textureId, DyeColor color) {
        int colorId = color.getEntityColor();
        canvas.render(matrices, textureId.getVertexConsumer(vertexConsumers, RenderLayer::getEntityNoOutline), light, overlay, colorId);
    }

    public static SpriteIdentifier getShieldPatternTextureId(RegistryEntry<BannerPattern> pattern, Identifier atlas) {
        return SHIELD_PATTERN_TEXTURES.computeIfAbsent((pattern.value()).assetId(), id -> {
            // Removes minecraft namespace section from string
            StringBuilder identifierString = new StringBuilder(id.toString());
            identifierString.delete(0, 10);

            Identifier identifier = Identifier.of(atlas.getNamespace(), "entity/shield/" + identifierString);
            return new SpriteIdentifier(atlas, identifier);
        });
    }

    /**
     * Renders color layer on dyeable shield model.
     */
    @Environment(value = EnvType.CLIENT)
    public static void renderColor(ItemStack stack, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, ShieldEntityModel model, SpriteIdentifier baseSprite, SpriteIdentifier colorLayer){
        DyedColorComponent dyeColor = stack.get(DataComponentTypes.DYED_COLOR);
        matrices.push();
        matrices.scale(1.0f, -1.0f, -1.0f);
        VertexConsumer vertexConsumer = baseSprite.getSprite().getTextureSpecificVertexConsumer(ItemRenderer.getDirectItemGlintConsumer(vertexConsumers, model.getLayer(baseSprite.getAtlasId()), true, stack.hasGlint()));
        model.getHandle().render(matrices, vertexConsumer, light, overlay);
        if (dyeColor != null) {
            renderColorLayer(matrices, vertexConsumers, light, overlay, model.getPlate(), baseSprite, colorLayer, dyeColor, stack.hasGlint());
        } else {
            model.getPlate().render(matrices, vertexConsumer, light, overlay);
        }
        matrices.pop();
    }

    /**
     * Renders color layer on dyeable shield model with armor trims on top.
     */
    @Environment(value = EnvType.CLIENT)
    public static void renderColorWithTrims(ItemStack stack, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, ShieldEntityModel model, SpriteIdentifier baseSprite, SpriteIdentifier colorLayer, Identifier trimsAtlas){
        ArmorTrim armorTrim = stack.get(DataComponentTypes.TRIM);
        DyedColorComponent dyeColor = stack.get(DataComponentTypes.DYED_COLOR);
        matrices.push();
        matrices.scale(1.0f, -1.0f, -1.0f);
        VertexConsumer vertexConsumer = baseSprite.getSprite().getTextureSpecificVertexConsumer(ItemRenderer.getDirectItemGlintConsumer(vertexConsumers, model.getLayer(baseSprite.getAtlasId()), true, stack.hasGlint()));
        model.getHandle().render(matrices, vertexConsumer, light, overlay);
        if (dyeColor != null) {
            renderColorLayer(matrices, vertexConsumers, light, overlay, model.getPlate(), baseSprite, colorLayer, dyeColor, stack.hasGlint());
            if (armorTrim != null) {
                renderTrimLayer(stack, matrices, vertexConsumers, light, overlay, model.getPlate(), armorTrim, trimsAtlas);
            }
        } else if (armorTrim != null) {
            renderTrimLayer(stack, matrices, vertexConsumers, light, overlay, model.getPlate(), baseSprite, armorTrim, trimsAtlas, stack.hasGlint());
        } else {
            model.getPlate().render(matrices, vertexConsumer, light, overlay);
        }
        matrices.pop();
    }

    public static void renderColorLayer(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, ModelPart layer, SpriteIdentifier baseSprite, SpriteIdentifier colorLayer, DyedColorComponent color, boolean glint) {
        int ColorId = ColorHelper.Argb.fullAlpha(color.rgb());

        layer.render(matrices, baseSprite.getVertexConsumer(vertexConsumers, RenderLayer::getEntitySolid, glint), light, overlay);
        layer.render(matrices, colorLayer.getVertexConsumer(vertexConsumers, RenderLayer::getEntityNoOutline), light, overlay, ColorId);
    }

    /**
     * Renders generic builtin model shields. Includes code to render armor trims, if needed.
     */
    @Environment(value = EnvType.CLIENT)
    public static void renderBuiltinModel(ItemStack stack, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, ShieldEntityModel model, SpriteIdentifier baseSprite, Identifier atlas){
        ArmorTrim armorTrim = stack.get(DataComponentTypes.TRIM);
        matrices.push();
        matrices.scale(1.0f, -1.0f, -1.0f);
        VertexConsumer vertexConsumer = baseSprite.getSprite().getTextureSpecificVertexConsumer(ItemRenderer.getDirectItemGlintConsumer(vertexConsumers, model.getLayer(baseSprite.getAtlasId()), true, stack.hasGlint()));
        model.getHandle().render(matrices, vertexConsumer, light, overlay);
        if (armorTrim != null) {
            renderTrimLayer(stack, matrices, vertexConsumers, light, overlay, model.getPlate(), baseSprite, armorTrim, atlas, stack.hasGlint());
        } else {
            model.getPlate().render(matrices, vertexConsumer, light, overlay);
        }
        matrices.pop();
    }

    public static void renderTrimLayer(ItemStack stack,MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, ModelPart layer, SpriteIdentifier baseSprite, ArmorTrim armorTrim, Identifier atlas, boolean glint) {
        SpriteIdentifier spriteIdentifier = getShieldTrimTextureId(stack, armorTrim, atlas);

        layer.render(matrices, baseSprite.getVertexConsumer(vertexConsumers, RenderLayer::getEntitySolid, glint), light, overlay);
        layer.render(matrices, spriteIdentifier.getVertexConsumer(vertexConsumers, RenderLayer::getEntityNoOutline), light, overlay);
    }

    public static void renderTrimLayer(ItemStack stack, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, ModelPart layer, ArmorTrim armorTrim, Identifier atlas) {
        SpriteIdentifier spriteIdentifier = getShieldTrimTextureId(stack, armorTrim, atlas);

        layer.render(matrices, spriteIdentifier.getVertexConsumer(vertexConsumers, RenderLayer::getEntityNoOutline), light, overlay);
    }

    public static SpriteIdentifier getShieldTrimTextureId(ItemStack stack, ArmorTrim armorTrim, Identifier atlas) {
        StringBuilder patternString = new StringBuilder(armorTrim.getPattern().getIdAsString());
        patternString.delete(0, 10);
        StringBuilder materialString = new StringBuilder(armorTrim.getMaterial().getIdAsString());
        materialString.delete(0, 10);

        boolean darken = false;
        if (stack.getItem() instanceof ModShield shield) {
            Ingredient darkenMaterials = shield.getTrimDarkenMaterials();
            List<String> darkenMaterialsID = new ArrayList<>();

            for (ItemStack itemStack : darkenMaterials.getMatchingStacks()) {
                darkenMaterialsID.add(itemStack.getItem().toString());
            }

            for (String ID : darkenMaterialsID) {
                if (ID.contains(materialString.toString())) {
                    darken = true;
                    break;
                }
            }
        }

        Identifier identifier = Identifier.of(atlas.getNamespace(), "trims/models/shield/" + patternString + "_" + materialString +  (darken ? "_darker" : ""));
        return new SpriteIdentifier(atlas, identifier);
    }
}