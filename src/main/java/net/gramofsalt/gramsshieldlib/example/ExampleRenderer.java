package net.gramofsalt.gramsshieldlib.example;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.gramofsalt.gramsshieldlib.GramsShieldLib;
import net.gramofsalt.gramsshieldlib.library.event.ShieldSetModelCallback;
import net.gramofsalt.gramsshieldlib.library.util.ShieldUtils;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.ShieldEntityModel;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;

/**
 * Example class that registers and renders shield builtin models.
 */
@Environment(value = EnvType.CLIENT)
public class ExampleRenderer {
    /**
     * Example atlas declaration used for the banner and dye shields. The identifier should use the "textures/atlas" path, and then the name of the atlas json.
     * A custom atlas is not necessary if using the same banner textures as vanilla shields. The vanilla atlas is "TexturedRenderLayers.SHIELD_PATTERNS_ATLAS_TEXTURE".
     * An atlas should exist for every shield type that needs unique banner textures.
     * The prefix for banner patterns must always be "entity/shield/", only change the source. see "example_shield_patterns".
     * Must be added to the "LAYERS_TO_LOADERS" map through a BakedModelManager mixin, see the "BakedModelManagerMixin " in this library for an example.
     */
    public static final Identifier EXAMPLE_SHIELD_PATTERNS_ATLAS = Identifier.of(GramsShieldLib.MODID, "textures/atlas/example_shield_patterns");

    /**
     * Initialize Example Banner Shield model and model layer
     */
    public static final EntityModelLayer EXAMPLE_BANNER_SHIELD_MODEL_LAYER = new EntityModelLayer(Identifier.of(GramsShieldLib.MODID, "example_banner_shield"),"main");
    public static ShieldEntityModel EXAMPLE_BANNER_SHIELD_MODEL;

    /**
     * Initialize Example Banner Shield layers. Be sure to add the base and base no pattern directories to the shield_pattern atlas
     */
    public static final SpriteIdentifier EXAMPLE_BANNER_SHIELD_BASE = new SpriteIdentifier(EXAMPLE_SHIELD_PATTERNS_ATLAS, Identifier.of(GramsShieldLib.MODID, "entity/vanilla_shield_base"));
    public static final SpriteIdentifier EXAMPLE_BANNER_SHIELD_NO_PATTERN = new SpriteIdentifier(EXAMPLE_SHIELD_PATTERNS_ATLAS, Identifier.of(GramsShieldLib.MODID, "entity/vanilla_shield_base_nopattern"));

    /**
     * Initialize Example Dyeable Shield model an model layer
     */
    public static final EntityModelLayer EXAMPLE_DYEABLE_SHIELD_MODEL_LAYER = new EntityModelLayer(Identifier.of(GramsShieldLib.MODID, "example_dyeable_shield"),"main");
    public static ShieldEntityModel EXAMPLE_DYEABLE_SHIELD_MODEL;

    /**
     * Initialize Example Dyeable Shield layers. Be sure to add the base and color layer directories to the shield_pattern atlas
     */
    public static final SpriteIdentifier EXAMPLE_DYEABLE_SHIELD_BASE = new SpriteIdentifier(EXAMPLE_SHIELD_PATTERNS_ATLAS, Identifier.of(GramsShieldLib.MODID, "entity/vanilla_shield_base_nopattern"));
    public static final SpriteIdentifier EXAMPLE_DYEABLE_SHIELD_COLOR_LAYER = new SpriteIdentifier(EXAMPLE_SHIELD_PATTERNS_ATLAS, Identifier.of(GramsShieldLib.MODID, "entity/vanilla_shield_color_layer"));

    /**
     * Initialize Example Trim Shield model an model layer
     */
    public static final EntityModelLayer EXAMPLE_TRIM_SHIELD_MODEL_LAYER = new EntityModelLayer(Identifier.of(GramsShieldLib.MODID, "example_trim_shield"),"main");
    public static ShieldEntityModel EXAMPLE_TRIM_SHIELD_MODEL;

    /**
     * Initialize Example Trim Shield layer. Be sure to add the base layer to the shield_pattern atlas
     */
    public static final SpriteIdentifier EXAMPLE_TRIM_SHIELD_BASE = new SpriteIdentifier(EXAMPLE_SHIELD_PATTERNS_ATLAS, Identifier.of(GramsShieldLib.MODID, "entity/vanilla_shield_base_nopattern"));

    /**
     * Model rendering method declared in the client initialization class.
     */
    public static void renderExampleShields() {
        // Register builtin model layer as ShieldEntityModel (or another custom shield model) using EntityModelLayerRegistry.
        // Set the shield model using ShieldSetModelCallback
        EntityModelLayerRegistry.registerModelLayer(EXAMPLE_BANNER_SHIELD_MODEL_LAYER, ShieldEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(EXAMPLE_DYEABLE_SHIELD_MODEL_LAYER, ShieldEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(EXAMPLE_TRIM_SHIELD_MODEL_LAYER, ShieldEntityModel::getTexturedModelData);
        ShieldSetModelCallback.EVENT.register((loader) -> {
            EXAMPLE_BANNER_SHIELD_MODEL = new ShieldEntityModel(loader.getModelPart(EXAMPLE_BANNER_SHIELD_MODEL_LAYER));
            EXAMPLE_DYEABLE_SHIELD_MODEL = new ShieldEntityModel(loader.getModelPart(EXAMPLE_DYEABLE_SHIELD_MODEL_LAYER));
            EXAMPLE_TRIM_SHIELD_MODEL = new ShieldEntityModel(loader.getModelPart(EXAMPLE_TRIM_SHIELD_MODEL_LAYER));
            return ActionResult.PASS;
        });

        // Render builtin item model using the renderBanner method from ShieldUtils.
        // Pass all parameters from the event into the method, then pass it the shield model, shield base texture, and shield base nopattern texture.
        BuiltinItemRendererRegistry.INSTANCE.register(ExampleItems.EXAMPLE_BANNER_SHIELD, (stack, mode, matrices, vertexConsumers, light, overlay) -> {
            ShieldUtils.renderBannerWithTrims(stack, matrices, vertexConsumers, light, overlay, EXAMPLE_BANNER_SHIELD_MODEL, EXAMPLE_BANNER_SHIELD_BASE, EXAMPLE_BANNER_SHIELD_NO_PATTERN, ShieldUtils.VANILLA_SHIELD_TRIMS_ATLAS);
        });

        // Render builtin item model using the renderColor method from ShieldUtils.
        // Pass all parameters from the event into the method, then pass it the shield model, shield base texture, and shield color layer texture.
        BuiltinItemRendererRegistry.INSTANCE.register(ExampleItems.EXAMPLE_DYEABLE_SHIELD, (stack, mode, matrices, vertexConsumers, light, overlay) -> {
            ShieldUtils.renderColorWithTrims(stack, matrices, vertexConsumers, light, overlay, EXAMPLE_DYEABLE_SHIELD_MODEL, EXAMPLE_DYEABLE_SHIELD_BASE, EXAMPLE_DYEABLE_SHIELD_COLOR_LAYER, ShieldUtils.VANILLA_SHIELD_TRIMS_ATLAS);
        });

        // For shields that use trims, but are not dyeable or decorable, render builtin item model using the renderBuiltinModel method from ShieldUtils
        // Pass all parameters from the event into the method, then pass it the shield model, shield base texture, and armor trim atlas.
        BuiltinItemRendererRegistry.INSTANCE.register(ExampleItems.EXAMPLE_TRIM_SHIELD, (stack, mode, matrices, vertexConsumers, light, overlay) -> {
            ShieldUtils.renderBuiltinModel(stack, matrices, vertexConsumers, light, overlay, EXAMPLE_TRIM_SHIELD_MODEL, EXAMPLE_TRIM_SHIELD_BASE, ShieldUtils.VANILLA_SHIELD_TRIMS_ATLAS);
        });
    }
}