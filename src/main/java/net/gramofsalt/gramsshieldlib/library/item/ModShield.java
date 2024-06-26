package net.gramofsalt.gramsshieldlib.library.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;

/**
 * Used to identify shield items.
 */
public interface ModShield {
    /**
     * Called on both the server and client side when an entity blocks an attack.
     *
     * @param stack - Shield in use that blocked attack
     * @param world - World the user of the shield is interacting with
     * @param user - Entity using the shield
     * @param amount - Amount of damage dealt by the blocked attack
     * @param source - Damage Source of the blocked attack
     */
    void onShieldHit(ItemStack stack, World world, LivingEntity user, double amount, DamageSource source);

    /**
     * Called whenever a players shield is disabled by an attack.
     *
     * @param stack - Shield in use that was disabled
     * @param world - World the user of the shield is interacting with
     * @param user - Entity using the shield
     */
    void onShieldDisable(ItemStack stack, World world, LivingEntity user);

    /**
     * Called on both the server and client side when an entity starts blocking.
     *
     * @param stack - Shield in use
     * @param world - World the user of the shield is interacting with
     * @param user - Entity using the shield
     */
    void onStartBlocking(ItemStack stack, World world, LivingEntity user);

    /**
     * Called on both the server and client side when an entity stops blocking. Runs the tick after the shield item is no longer in use.
     *
     * @param stack - Shield in use
     * @param world - World the user of the shield is interacting with
     * @param user - Entity using the shield
     */
    void onStoppedBlocking(ItemStack stack, World world, LivingEntity user);

    /**
     * Returns SoundEvent that will be used when an attack is blocked.
     * Vanilla shields use SoundEvents.ITEM_SHIELD_BLOCK.
     *
     * @return SoundEvent
     */
    SoundEvent getBlockSound();

    /**
     * Returns ingredients matching trim materials that are darkened when an armor trim is applied
     *
     * @return SoundEvent
     */
    Ingredient getTrimDarkenMaterials();

    /**
     * Returns if a shield will display its stats as a tooltip.
     * Set to true by default.
     *
     * @return boolean
     */
    default boolean displayTooltip() {
        return true;
    }
}