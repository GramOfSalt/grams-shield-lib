package net.gramofsalt.gramsshieldlib.mixin;

import net.gramofsalt.gramsshieldlib.library.item.material.PredefinedShieldMaterials;
import net.gramofsalt.gramsshieldlib.library.util.ShieldAttributes;
import net.gramofsalt.gramsshieldlib.library.item.material.ShieldMaterial;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * Mixin that allows vanilla shields to be enchantable and use modded attribute modifiers
 */
@Mixin(ShieldItem.class)
public abstract class ShieldItemMixin extends Item {
    public ShieldItemMixin(Settings settings) {
        super(settings);
    }

    /**
     * Allows vanilla shields to be enchanted
     */
    @Override
    public boolean isEnchantable(ItemStack item) {
        return !item.hasEnchantments();
    }

    /**
     * Sets vanilla shield enchantability
     */
    @Override
    public int getEnchantability() {
        return PredefinedShieldMaterials.VANILLA_SHIELD.getEnchantability();
    }

    /**
     * Modifies Item.Settings on the shield to add the AttributeModifierComponents
     */
    @ModifyVariable(method = "<init>(Lnet/minecraft/item/Item$Settings;)V", at = @At(value = "HEAD"), argsOnly = true)
    private static Item.Settings addAttributes(Item.Settings settings) {
        return settings.attributeModifiers(createAttributeModifiers(PredefinedShieldMaterials.VANILLA_SHIELD)).maxDamage(PredefinedShieldMaterials.VANILLA_SHIELD.getDurability());
    }

    @Unique
    private static AttributeModifiersComponent createAttributeModifiers(ShieldMaterial material) {
        return AttributeModifiersComponent.builder()
                .add(ShieldAttributes.SHIELD_COOLDOWN, new EntityAttributeModifier(ShieldAttributes.SHIELD_COOLDOWN_MODIFIER_ID, material.getCoolDownTicks(), EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.ANY)
                .add(ShieldAttributes.SHIELD_DELAY, new EntityAttributeModifier(ShieldAttributes.SHIELD_DELAY_MODIFIER_ID, material.getBlockingDelay(), EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.ANY)
                .add(ShieldAttributes.SHIELD_MAXIMUM_DAMAGE, new EntityAttributeModifier(ShieldAttributes.SHIELD_MAXIMUM_DAMAGE_MODIFIER_ID, material.getMaximumDamage(), EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.ANY)
                .add(ShieldAttributes.SHIELD_GENERIC_DAMAGE_RESISTANCE, new EntityAttributeModifier(ShieldAttributes.SHIELD_GENERIC_DAMAGE_RESISTANCE_MODIFIER_ID, material.getDamageResistance(), EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.ANY)
                .add(ShieldAttributes.SHIELD_KNOCKBACK_RESISTANCE, new EntityAttributeModifier(ShieldAttributes.SHIELD_KNOCKBACK_RESISTANCE_MODIFIER_ID, material.getKnockbackResistance(), EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.ANY)
                .add(ShieldAttributes.SHIELD_EXPLOSION_RESISTANCE, new EntityAttributeModifier(ShieldAttributes.SHIELD_EXPLOSION_RESISTANCE_MODIFIER_ID, material.getExplosionResistance(), EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.ANY)
                .add(ShieldAttributes.SHIELD_DURABILITY_MULTIPLIER, new EntityAttributeModifier(ShieldAttributes.SHIELD_DURABILITY_MULTIPLIER_MODIFIER_ID, material.getDurabilityMultiplier(), EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.ANY)
                .add(ShieldAttributes.SHIELD_MINIMUM_DURABILITY_DAMAGE, new EntityAttributeModifier(ShieldAttributes.SHIELD_MINIMUM_DURABILITY_DAMAGE_MODIFIER_ID, material.getMinDamageDurability(), EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.ANY)
                .add(ShieldAttributes.SHIELD_BLOCKING_SPEED, new EntityAttributeModifier(ShieldAttributes.SHIELD_BLOCKING_SPEED_MODIFIER_ID, material.getMovementSpeedMultiplier(), EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.ANY)
                .build().withShowInTooltip(false);
    }
}
