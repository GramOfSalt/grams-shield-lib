package net.gramofsalt.gramsshieldlib.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.gramofsalt.gramsshieldlib.library.event.ShieldSetModelCallback;
import net.gramofsalt.gramsshieldlib.library.util.ShieldUtils;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BannerPatternsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.util.DyeColor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin that creates shieldSetModelCallback and allows vanilla shields to render with armor trims
 */
@Mixin(BuiltinModelItemRenderer.class)
public abstract class BuiltinModelItemRendererMixin {
    @Final
    @Shadow
    private EntityModelLoader entityModelLoader;

    /**
     * Creates shieldSetModelCallback. Can be used to register new shield models.
     */
    @Inject(method = "reload", at = @At("HEAD"))
    private void setModelShield(CallbackInfo ci) {
        ShieldSetModelCallback.EVENT.invoker().setModel(this.entityModelLoader);
    }

    /**
     * Redirects vanilla shield rendering code to use mod code and renders armor trims on vanilla shields.
     * The redirect is needed to make the vanilla shield use a custom atlas. Otherwise, the banners on shields will render on top of trims (don't know why).
     */
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/block/entity/BannerBlockEntityRenderer;renderCanvas(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/model/ModelPart;Lnet/minecraft/client/util/SpriteIdentifier;ZLnet/minecraft/util/DyeColor;Lnet/minecraft/component/type/BannerPatternsComponent;Z)V"))
    public void renderVanillaShieldTrims(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, ModelPart canvas, SpriteIdentifier spriteIdentifier, boolean isBanner, DyeColor dyeColor, BannerPatternsComponent bannerPatternsComponent, boolean glint, @Local(argsOnly = true) ItemStack stack) {
        ArmorTrim armorTrim = stack.get(DataComponentTypes.TRIM);
        ShieldUtils.renderCanvas(matrices, vertexConsumers, light, overlay, ShieldUtils.VANILLA_SHIELD_MODEL.getPlate(), ShieldUtils.VANILLA_SHIELD_BASE, dyeColor, bannerPatternsComponent, glint);
        if (armorTrim != null) {
            ShieldUtils.renderTrimLayer(stack, matrices, vertexConsumers, light, overlay, ShieldUtils.VANILLA_SHIELD_MODEL.getPlate(), armorTrim, ShieldUtils.VANILLA_SHIELD_TRIMS_ATLAS);
        }
    }

    /**
     * Injects armor trim rendering code whenever the vanilla shield is rendered without a banner. Does not replace the vanilla rendering.
     */
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelPart;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;II)V", ordinal = 1, shift = At.Shift.AFTER))
    public void renderVanillaShieldTrims2(ItemStack stack, ModelTransformationMode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, CallbackInfo info) {
        ArmorTrim armorTrim = stack.get(DataComponentTypes.TRIM);
        if (armorTrim != null) {
            ShieldUtils.renderTrimLayer(stack, matrices, vertexConsumers, light, overlay, ShieldUtils.VANILLA_SHIELD_MODEL.getPlate(), ShieldUtils.VANILLA_SHIELD_NO_PATTERN, armorTrim, ShieldUtils.VANILLA_SHIELD_TRIMS_ATLAS, stack.hasGlint());
        }
    }
}
