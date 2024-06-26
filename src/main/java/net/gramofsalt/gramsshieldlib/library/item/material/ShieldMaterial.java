package net.gramofsalt.gramsshieldlib.library.item.material;

import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;

/**
 * Used to identify shield materials. Materials serve as an easy to way to define the base attributes and settings on a shield.
 */
public interface ShieldMaterial {
    /**
     * Returns the total amount of durability a ModShieldItem has.
     *
     * @return int - The maximum durability of a ModShieldItem
     */
    int getDurability();

    /**
     * Returns the total amount of durability a ModShieldItem has.
     *
     * @return int - The maximum durability of a ModShieldItem
     */
    int getCoolDownTicks();

    /**
     * Returns the blocking delay of a shield. Blocking delay is the amount of time it takes to start blocking after initial use of the shield.
     *
     * @return int - Blocking delay in ticks
     */
    int getBlockingDelay();

    /**
     * Returns the maximum damage an attack can deal before the shield stops blocking all damage. Attacks greater than this value are subtracted by it.
     *
     * @return float - Maximum damage
     */
    float getMaximumDamage();

    /**
     * Returns a value that is multiplied by the damage of an attack to determine the final damage amount.
     *
     * @return float - Knockback strength multiplier
     */
    float getDamageResistance();

    /**
     * Returns a value that is multiplied by the knockback strength of an attack to determine the final knockback strength.
     *
     * @return float - Knockback strength multiplier
     */
    float getKnockbackResistance();

    /**
     * Returns a value that is multiplied by the damage of an explosion to determine the final damage amount.
     *
     * @return float - Explosion damage multiplier
     */
    float getExplosionResistance();

    /**
     * Returns a value that is multiplied by the damage of a blocked attack to determine the durability deduction on the shield.
     *
     * @return float - Durability multiplier
     */
    float getDurabilityMultiplier();

    /**
     * Returns the minimum damage an attack needs to deal to cause the durability deduction on the shield.
     *
     * @return float - Minimum required damage
     */
    float getMinDamageDurability();

    /**
     * Returns a value that is multiplied by the base movement speed of the user to determine their movement speed while blocking.
     *
     * @return float - Movement Speed Multiplier
     */
    float getMovementSpeedMultiplier();

    /**
     * Returns the sound effect used when an attack is blocked by the shield
     *
     * @return SoundEvent - Block sound effect
     */
    SoundEvent getBlockSound();

    /**
     * Returns material types that are darkened when applying an armor trim texture
     *
     * @return SoundEvent - Block sound effect
     */
    Ingredient getTrimDarkenMaterials();

    /**
     * Returns the enchantability of the shield.
     *
     * @return int - Enchantability
     */
    int getEnchantability();

    /**
     * Returns the repair ingredients of the shield.
     *
     * @return Ingredient - Repair ingredients
     */
    Ingredient getRepairIngredient();
}
