package net.gramofsalt.gramsshieldlib.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.gramofsalt.gramsshieldlib.library.util.ShieldAttributes;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

/**
 * Mixin that allows movement speed to change based on shield attributes
 */
@Environment(value= EnvType.CLIENT)
@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin {
    @Unique private final PlayerEntity player = (PlayerEntity) (Object) this;

    /**
     * Modifies constant ClientPlayerEntity tickMovement class.
     * Movement speed while using an item is defined by the constant "0.2f". The constant is changed to the value of SHIELD_BLOCKING_SPEED if the item in use has the
     * attribute modifier.
     */
    @ModifyConstant(method = "tickMovement()V", constant = @Constant(floatValue = 0.2f))
    public float shieldBlockSpeed(float constant) {
        ItemStack activeItem = player.getActiveItem();
        if (ShieldAttributes.containsModifier(activeItem, ShieldAttributes.SHIELD_BLOCKING_SPEED_MODIFIER_ID)) {
            constant = (float) ShieldAttributes.getModifierValue(activeItem, ShieldAttributes.SHIELD_BLOCKING_SPEED_MODIFIER_ID);
        }
        return constant;
    }
}
