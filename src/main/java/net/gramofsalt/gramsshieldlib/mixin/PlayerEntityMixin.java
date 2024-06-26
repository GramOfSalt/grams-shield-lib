package net.gramofsalt.gramsshieldlib.mixin;

import net.gramofsalt.gramsshieldlib.library.event.ShieldDisableCallback;
import net.gramofsalt.gramsshieldlib.library.item.ModShield;
import net.gramofsalt.gramsshieldlib.library.util.ShieldAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShieldItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin that allows ModShields to be damaged like normal shields, ModShields to be disabled like normal shields, and durability and cooldown are influenced by attributes
 */
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
    @Unique private final PlayerEntity player = (PlayerEntity) (Object) this;

    /**
     * Redirects method to check if the active item is a ShieldItem or ModShield in order to damage it
     */
    @Redirect(method = "damageShield", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"))
    private boolean damageModShields(ItemStack instance, Item item) {
        if (instance == null) return false;
        return instance.getItem() instanceof ShieldItem || instance.getItem() instanceof ModShield;
    }

    /**
     * Modifies minimum damage required to deduct durability based on the value of SHIELD_MINIMUM_DURABILITY_DAMAGE
     */
    @ModifyConstant(method = "damageShield", constant = @Constant(floatValue = 3.0f))
    private float shieldMinDamage(float constant) {
        ItemStack activeItem = player.getActiveItem();
        if (ShieldAttributes.containsModifier(activeItem, ShieldAttributes.SHIELD_MINIMUM_DURABILITY_DAMAGE_MODIFIER_ID)) {
            constant = (float) ShieldAttributes.getModifierValue(activeItem, ShieldAttributes.SHIELD_MINIMUM_DURABILITY_DAMAGE_MODIFIER_ID);
        }
        return constant;
    }

    /**
     * Multiplies durability deducted based on the value of SHIELD_MINIMUM_DURABILITY_DAMAGE
     */
    @ModifyArg(method = "damageShield(F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;damage(ILnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/EquipmentSlot;)V"), index = 0)
    private int shieldDamageMultiplier(int amount) {
        ItemStack activeItem = player.getActiveItem();
        if (ShieldAttributes.containsModifier(activeItem, ShieldAttributes.SHIELD_DURABILITY_MULTIPLIER_MODIFIER_ID)) {
            amount = (int) Math.ceil(amount * ShieldAttributes.getModifierValue(activeItem, ShieldAttributes.SHIELD_DURABILITY_MULTIPLIER_MODIFIER_ID));
        }
        return amount;
    }

    /**
     * Handles "onShieldDisable" method.
     */
    @Inject(method = "disableShield", at = @At("HEAD"))
    public void onShieldDisable(CallbackInfo info) {
        ItemStack activeItem = player.getActiveItem();
        if (activeItem.getItem() instanceof ModShield shield) {
            shield.onShieldDisable(activeItem, player.getWorld(), player);
            ShieldDisableCallback.EVENT.invoker().shieldDisabled(activeItem, player.getWorld(), player);
        }
    }

    /**
     * Modifies disable cooldown length based on the value of SHIELD_MINIMUM_DURABILITY_DAMAGE
     */
    @ModifyConstant(method = "disableShield", constant = @Constant(intValue = 100))
    private int shieldCooldown(int constant) {
        ItemStack activeItem = player.getActiveItem();
        if (ShieldAttributes.containsModifier(activeItem, ShieldAttributes.SHIELD_COOLDOWN_MODIFIER_ID)) {
            constant = (int) ShieldAttributes.getModifierValue(activeItem, ShieldAttributes.SHIELD_COOLDOWN_MODIFIER_ID);
        }
        return constant;
    }

    /**
     * Item disabled is now the players active item rather than hard-set to ShieldItem
     */
    @Redirect(method = "disableShield", at = @At(value = "FIELD", target = "Lnet/minecraft/item/Items;SHIELD:Lnet/minecraft/item/Item;"))
    private Item shieldItemRedirect() {
        ItemStack activeItem = player.getActiveItem();
        if (activeItem == null) return Items.SHIELD;
        return activeItem.getItem();
    }
}
