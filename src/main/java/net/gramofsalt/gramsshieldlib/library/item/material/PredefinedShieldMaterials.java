package net.gramofsalt.gramsshieldlib.library.item.material;

import com.google.common.base.Suppliers;
import net.gramofsalt.gramsshieldlib.library.config.GramsShieldLibConfig;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

import java.util.function.Supplier;

public enum PredefinedShieldMaterials implements ShieldMaterial{
    VANILLA_SHIELD(GramsShieldLibConfig.vanilla_shield_durability, GramsShieldLibConfig.vanilla_shield_cooldown, GramsShieldLibConfig.vanilla_shield_delay, GramsShieldLibConfig.vanilla_shield_max_damage, GramsShieldLibConfig.vanilla_shield_damage_resistance, GramsShieldLibConfig.vanilla_shield_knockback_resistance, GramsShieldLibConfig.vanilla_shield_explosion_resistance, GramsShieldLibConfig.vanilla_shield_durability_multiplier, GramsShieldLibConfig.vanilla_shield_min_durability_damage, GramsShieldLibConfig.vanilla_shield_movement_speed_multiplier, SoundEvents.ITEM_SHIELD_BLOCK, () -> Ingredient.EMPTY, GramsShieldLibConfig.vanilla_shield_enchantablity, () -> Ingredient.fromTag(ItemTags.PLANKS));

    private final int itemDurability;
    private final int coolDownTicks;
    private final int blockingDelay;
    private final float maximumDamage;
    private final float damageResistance;
    private final float knockbackResistance;
    private final float explosionResistance;
    private final float durabilityMultiplier;
    private final float minDamageDurability;
    private final float movementSpeedMultiplier;
    private final SoundEvent blockSound;
    private final Supplier<Ingredient> trimDarkenMaterials;
    private final int enchantability;
    private final Supplier<Ingredient> repairIngredient;

    PredefinedShieldMaterials(int itemDurability, int coolDownTicks, int blockingDelay,
                              float maximumDamage, float damageResistance, float knockbackResistance, float explosionResistance,
                              float durabilityMultiplier, float minDamageDurability, float movementSpeedMultiplier,
                              SoundEvent blockSound, Supplier<Ingredient> trimDarkenMaterials, int enchantability, Supplier<Ingredient> repairIngredient)
    {
        this.itemDurability = itemDurability;
        this.coolDownTicks = coolDownTicks;
        this.blockingDelay = blockingDelay;
        this.maximumDamage = maximumDamage;
        this.damageResistance = damageResistance;
        this.knockbackResistance = knockbackResistance;
        this.explosionResistance = explosionResistance;
        this.durabilityMultiplier = durabilityMultiplier;
        this.minDamageDurability = minDamageDurability;
        this.movementSpeedMultiplier = movementSpeedMultiplier;
        this.blockSound = blockSound;
        this.trimDarkenMaterials = trimDarkenMaterials;
        this.enchantability = enchantability;
        this.repairIngredient = Suppliers.memoize(repairIngredient::get);
    }

    @Override
    public int getDurability() {
        return itemDurability;
    }

    @Override
    public int getCoolDownTicks() {
        return coolDownTicks;
    }

    @Override
    public int getBlockingDelay() {
        return blockingDelay;
    }

    @Override
    public float getMaximumDamage() {
        return maximumDamage;
    }

    @Override
    public float getDamageResistance() {
        return damageResistance;
    }

    @Override
    public float getKnockbackResistance() {
        return knockbackResistance;
    }

    @Override
    public float getExplosionResistance() {
        return explosionResistance;
    }

    @Override
    public float getDurabilityMultiplier() {
        return durabilityMultiplier;
    }

    @Override
    public float getMinDamageDurability() {
        return minDamageDurability;
    }

    @Override
    public float getMovementSpeedMultiplier() {
        return movementSpeedMultiplier;
    }

    @Override
    public SoundEvent getBlockSound() {
        return blockSound;
    }

    @Override
    public Ingredient getTrimDarkenMaterials() {
        return trimDarkenMaterials.get();
    }

    @Override
    public int getEnchantability() {
        return enchantability;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return repairIngredient.get();
    }
}
