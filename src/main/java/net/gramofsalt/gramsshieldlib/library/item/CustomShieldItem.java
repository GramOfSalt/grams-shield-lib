package net.gramofsalt.gramsshieldlib.library.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.gramofsalt.gramsshieldlib.library.util.ShieldAttributes;
import net.gramofsalt.gramsshieldlib.library.item.material.ShieldMaterial;
import net.minecraft.block.DispenserBlock;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.BannerItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.util.List;

/**
 * Pre-made class for making custom shields.
 */
public class CustomShieldItem extends Item implements ModShield{
    private final int enchantability;
    private final Ingredient repairIngredient;
    private final SoundEvent blockSound;
    private final Ingredient trimDarkenMaterials;

    /**
     * Constructor method
     * @param material - ShieldMaterial used to define durability, enchantability, repair ingredients, and block sound
     * @param settings - Item.Settings
     */
    public CustomShieldItem(ShieldMaterial material, Item.Settings settings) {
        super(settings.maxDamage(material.getDurability()));
        this.enchantability = material.getEnchantability();
        this.repairIngredient = material.getRepairIngredient();
        this.blockSound = material.getBlockSound();
        this.trimDarkenMaterials= material.getTrimDarkenMaterials();

        DispenserBlock.registerBehavior(this, ArmorItem.DISPENSER_BEHAVIOR);

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            this.RegisterModelPredicate();
        }
    }

    /**
     * Constructor method
     * @param material - ShieldMaterial used to define enchantability, repair ingredients, and block sound
     * @param durability - Int used to determine the shield's max durability
     * @param settings - Item.Settings
     */
    public CustomShieldItem(ShieldMaterial material, int durability, Item.Settings settings) {
        super(settings.maxDamage(durability));
        this.enchantability = material.getEnchantability();
        this.repairIngredient = material.getRepairIngredient();
        this.blockSound = material.getBlockSound();
        this.trimDarkenMaterials= material.getTrimDarkenMaterials();

        DispenserBlock.registerBehavior(this, ArmorItem.DISPENSER_BEHAVIOR);

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            this.RegisterModelPredicate();
        }
    }

    /**
     * Constructor method
     * @param durability - Int used to determine the shield's max durability
     * @param enchantability - Int used to determine the shield's enchantability
     * @param repairIngredient - Ingredients used to repair the item
     * @param blockSound - SoundEvent used when an attack is blocked
     * @param trimDarkenMaterials - Ingredients matching trim materials that are darkened when an armor trim is applied
     * @param settings - Item.Settings
     */
    public CustomShieldItem(int durability, int enchantability, Ingredient repairIngredient, SoundEvent blockSound, Ingredient trimDarkenMaterials, Item.Settings settings) {
        super(settings.maxDamage(durability));
        this.enchantability = enchantability;
        this.repairIngredient = repairIngredient;
        this.blockSound = blockSound;
        this.trimDarkenMaterials= trimDarkenMaterials;

        DispenserBlock.registerBehavior(this, ArmorItem.DISPENSER_BEHAVIOR);

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
            this.RegisterModelPredicate();
        }
    }

    /**
     * Registers a model predicate that allows the shields model to change while blocking.
     */
    private void RegisterModelPredicate() {
        ModelPredicateProviderRegistry.register(Identifier.of("blocking"), (itemStack, clientWorld, livingEntity, i) -> {
            return livingEntity != null && livingEntity.isUsingItem() && livingEntity.getActiveItem() == itemStack ? 1.0F : 0.0F;
        });
    }

    /**
     * Adds a tooltip to the shield to inform users on what banner patterns are on the shield.
     */
    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        BannerItem.appendBannerTooltip(stack, tooltip);
    }

    /**
     * Changes translation key depending on the base banner color of the shield.
     */
    @Override
    public String getTranslationKey(ItemStack stack) {
        DyeColor dyeColor = stack.get(DataComponentTypes.BASE_COLOR);
        if (dyeColor != null) {
            return this.getTranslationKey() + "." + dyeColor.getName();
        }
        return super.getTranslationKey(stack);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if(user.isBlocking()) blockTick(world, user, stack, remainingUseTicks);
        super.usageTick(world, user, stack, remainingUseTicks);
    }

    /**
     * Called on both the server and client side every tick while an entity is blocking. Not in use by default, but can be overridden for custom shield items.
     *
     * @param world - World the user of the item is interacting with
     * @param user - Entity using the item
     * @param stack - Item in use
     * @param remainingUseTicks - Ticks left until the item finishes being used
     */
    public void blockTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
    }

    /**
     * Called on both server and client side when a shield is hit by an attack. Not in use by default, but can be overridden for custom shield items.
     *
     * @param stack - Shield in use that blocked attack
     * @param world - World the user of the shield is interacting with
     * @param user - Entity using the shield
     * @param amount - Amount of damage dealt by the blocked attack
     * @param source - Damage Source of the blocked attack
     */
    @Override
    public void onShieldHit(ItemStack stack, World world, LivingEntity user, double amount, DamageSource source) {
    }

    /**
     * Called on both server and client side when a shield is disabled by an attack. Not in use by default, but can be overridden for custom shield items.
     *
     * @param stack - Shield in use that was disabled
     * @param world - World the user of the shield is interacting with
     * @param user - Entity using the shield
     */
    @Override
    public void onShieldDisable(ItemStack stack, World world, LivingEntity user) {
    }

    /**
     * Called on both server and client side when a player starts blocking with a shield. Not in use by default, but can be overridden for custom shield items.
     *
     * @param stack - Shield in use
     * @param world - World the user of the shield is interacting with
     * @param user - Entity using the shield
     */
    @Override
    public void onStartBlocking(ItemStack stack, World world, LivingEntity user) {
    }

    /**
     * Called on both server and client side when a player stops blocking with a shield. Not in use by default, but can be overridden for custom shield items.
     *
     * @param stack - Shield in use
     * @param world - World the user of the shield is interacting with
     * @param user - Entity using the shield
     */
    @Override
    public void onStoppedBlocking(ItemStack stack, World world, LivingEntity user) {
    }

    /**
     * Determines the use action type for the shield, which makes it possible for it to block attacks.
     */
    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BLOCK;
    }

    /**
     * Sets maximum use time very high so the shield can be used for a long duration.
     */
    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 72000;
    }

    /**
     * Sets active item and active hand while using the shield.
     */
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        user.setCurrentHand(hand);
        return TypedActionResult.consume(itemStack);
    }

    /**
     * Allows the shield to be used in an enchantment table if there are no enchantments on it.
     */
    @Override
    public boolean isEnchantable(ItemStack stack) {
        return !stack.hasEnchantments();
    }

    /**
     * Sets Enchantability based on constructor.
     */
    @Override
    public int getEnchantability() {
        return this.enchantability;
    }

    /**
     * Sets Block Sound based on constructor.
     */
    @Override
    public SoundEvent getBlockSound() {
        return this.blockSound;
    }

    /**
     * Sets Trim Darken Materials based on constructor.
     */
    @Override
    public Ingredient getTrimDarkenMaterials() {
        return this.trimDarkenMaterials;
    }

    /**
     * Allows the shield to be repaired if the repair item matches the repair item determined in the constructor.
     */
    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return this.repairIngredient.test(ingredient) || super.canRepair(stack, ingredient);
    }

    /**
     * Used on item registration. Determines the base attributes of the shield item.
     * Determines attributes solely based on material.
     *
     * @param material - ShieldMaterial of registered item
     * @return AttributeModifiersComponent - All attribute modifiers to be added to the weapon
     */
    public static AttributeModifiersComponent createAttributeModifiers(ShieldMaterial material) {
        return createAttributeModifiers(
                material.getCoolDownTicks(),
                material.getBlockingDelay(),
                material.getMaximumDamage(),
                material.getDamageResistance(),
                material.getKnockbackResistance(),
                material.getExplosionResistance(),
                material.getDurabilityMultiplier(),
                material.getMinDamageDurability(),
                material.getMovementSpeedMultiplier());
    }

    /**
     * Used on item registration. Determines the base attributes of the shield item.
     * Determines attributes based on material and parameter values.
     *
     * @param material - ShieldMaterial of registered item
     * @param cooldownTicks - Int value added to value defined in ShieldMaterial
     * @param delayTicks - Int value added to value defined in ShieldMaterial
     * @return AttributeModifiersComponent - All attribute modifiers to be added to the weapon
     */
    public static AttributeModifiersComponent createAttributeModifiers(ShieldMaterial material, int cooldownTicks, int delayTicks) {
        return createAttributeModifiers(
                cooldownTicks + material.getCoolDownTicks(),
                delayTicks + material.getBlockingDelay(),
                material.getMaximumDamage(),
                material.getDamageResistance(),
                material.getKnockbackResistance(),
                material.getExplosionResistance(),
                material.getDurabilityMultiplier(),
                material.getMinDamageDurability(),
                material.getMovementSpeedMultiplier());
    }

    /**
     * Used on item registration. Determines the base attributes of the shield item.
     * Determines attributes based on material and parameter values.
     *
     * @param material - ShieldMaterial of registered item
     * @param cooldownTicks - Int value added to value defined in ShieldMaterial
     * @param delayTicks - Int value added to value defined in ShieldMaterial
     * @param maxDamage - Float value added to value defined in ShieldMaterial
     * @param damageResistance - Float value added to value defined in ShieldMaterial
     * @return AttributeModifiersComponent - All attribute modifiers to be added to the weapon
     */
    public static AttributeModifiersComponent createAttributeModifiers(ShieldMaterial material, int cooldownTicks, int delayTicks, float maxDamage, float damageResistance, float knockbackResistance) {
        return createAttributeModifiers(
                cooldownTicks + material.getCoolDownTicks(),
                delayTicks + material.getBlockingDelay(),
                maxDamage + material.getMaximumDamage(),
                damageResistance + material.getDamageResistance(),
                knockbackResistance + material.getKnockbackResistance(),
                material.getExplosionResistance(),
                material.getDurabilityMultiplier(),
                material.getMinDamageDurability(),
                material.getMovementSpeedMultiplier());
    }

    /**
     * Used on item registration. Determines the base attributes of the shield item.
     * Determines attributes based on material and parameter values.
     *
     * @param material - ShieldMaterial of registered item
     * @param cooldownTicks - Int value added to value defined in ShieldMaterial
     * @param delayTicks - Int value added to value defined in ShieldMaterial
     * @param explosionResistance - Float value added to value defined in ShieldMaterial
     * @param knockbackResistance - Float value added to value defined in ShieldMaterial
     * @return AttributeModifiersComponent - All attribute modifiers to be added to the weapon
     */
    public static AttributeModifiersComponent createAttributeModifiers(ShieldMaterial material, int cooldownTicks, int delayTicks, float knockbackResistance, float explosionResistance) {
        return createAttributeModifiers(
                cooldownTicks + material.getCoolDownTicks(),
                delayTicks + material.getBlockingDelay(),
                material.getMaximumDamage(),
                material.getDamageResistance(),
                knockbackResistance + material.getKnockbackResistance(),
                explosionResistance + material.getExplosionResistance(),
                material.getDurabilityMultiplier(),
                material.getMinDamageDurability(),
                material.getMovementSpeedMultiplier());
    }

    /**
     * Used on item registration. Determines the base attributes of the shield item.
     * Determines attributes based on material and parameter values.
     *
     * @param material - ShieldMaterial of registered item
     * @param cooldownTicks - Int value added to value defined in ShieldMaterial
     * @param delayTicks - Int value added to value defined in ShieldMaterial
     * @param maxDamage - Float value added to value defined in ShieldMaterial
     * @param explosionResistance - Float value added to value defined in ShieldMaterial
     * @param knockbackResistance - Float value added to value defined in ShieldMaterial
     * @param durabilityMultiplier - Float value added to value defined in ShieldMaterial
     * @param minDamageDurability - Float value added to value defined in ShieldMaterial
     * @param movementSpeedMultiplier - Float value added to value defined in ShieldMaterial
     * @return AttributeModifiersComponent - All attribute modifiers to be added to the weapon
     */
    public static AttributeModifiersComponent createAttributeModifiers(ShieldMaterial material, int cooldownTicks, int delayTicks, float maxDamage, float damageResistance, float knockbackResistance, float explosionResistance, float durabilityMultiplier, float minDamageDurability, float movementSpeedMultiplier) {
        return createAttributeModifiers(
                cooldownTicks + material.getCoolDownTicks(),
                delayTicks + material.getBlockingDelay(),
                maxDamage + material.getMaximumDamage(),
                damageResistance + material.getDamageResistance(),
                knockbackResistance + material.getKnockbackResistance(),
                explosionResistance + material.getExplosionResistance(),
                durabilityMultiplier + material.getDurabilityMultiplier(),
                minDamageDurability + material.getMinDamageDurability(),
                movementSpeedMultiplier + material.getMovementSpeedMultiplier());
    }

    /**
     * Used on item registration. Determines the base attributes of the shield item.
     * Determines attributes based on parameter values, independant of shield material.
     *
     * @param cooldownTicks - Int value that defines the shield cooldown
     * @param delayTicks - Int value that defines the blocking delay
     * @param maxDamage - Float value that defines maximum damage
     * @param explosionResistance - Float value that defines explosion resistance
     * @param knockbackResistance - Float value that defines knockback resistance
     * @param durabilityMultiplier - Float value that defines the durability multiplier
     * @param minDamageDurability - Float value that defines min damage required for durability deduction
     * @param movementSpeedMultiplier - Float value that defines the movement speed multiplier
     * @return AttributeModifiersComponent - All attribute modifiers to be added to the weapon
     */
    public static AttributeModifiersComponent createAttributeModifiers(int cooldownTicks, int delayTicks, float maxDamage, float damageResistance, float knockbackResistance, float explosionResistance, float durabilityMultiplier, float minDamageDurability, float movementSpeedMultiplier) {
        return AttributeModifiersComponent.builder()
                .add(ShieldAttributes.SHIELD_COOLDOWN, new EntityAttributeModifier(ShieldAttributes.SHIELD_COOLDOWN_MODIFIER_ID, cooldownTicks, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.ANY)
                .add(ShieldAttributes.SHIELD_DELAY, new EntityAttributeModifier(ShieldAttributes.SHIELD_DELAY_MODIFIER_ID, delayTicks, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.ANY)
                .add(ShieldAttributes.SHIELD_MAXIMUM_DAMAGE, new EntityAttributeModifier(ShieldAttributes.SHIELD_MAXIMUM_DAMAGE_MODIFIER_ID, maxDamage, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.ANY)
                .add(ShieldAttributes.SHIELD_GENERIC_DAMAGE_RESISTANCE, new EntityAttributeModifier(ShieldAttributes.SHIELD_GENERIC_DAMAGE_RESISTANCE_MODIFIER_ID, damageResistance, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.ANY)
                .add(ShieldAttributes.SHIELD_KNOCKBACK_RESISTANCE, new EntityAttributeModifier(ShieldAttributes.SHIELD_KNOCKBACK_RESISTANCE_MODIFIER_ID, knockbackResistance, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.ANY)
                .add(ShieldAttributes.SHIELD_EXPLOSION_RESISTANCE, new EntityAttributeModifier(ShieldAttributes.SHIELD_EXPLOSION_RESISTANCE_MODIFIER_ID, explosionResistance, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.ANY)
                .add(ShieldAttributes.SHIELD_DURABILITY_MULTIPLIER, new EntityAttributeModifier(ShieldAttributes.SHIELD_DURABILITY_MULTIPLIER_MODIFIER_ID, durabilityMultiplier, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.ANY)
                .add(ShieldAttributes.SHIELD_MINIMUM_DURABILITY_DAMAGE, new EntityAttributeModifier(ShieldAttributes.SHIELD_MINIMUM_DURABILITY_DAMAGE_MODIFIER_ID, minDamageDurability, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.ANY)
                .add(ShieldAttributes.SHIELD_BLOCKING_SPEED, new EntityAttributeModifier(ShieldAttributes.SHIELD_BLOCKING_SPEED_MODIFIER_ID, movementSpeedMultiplier, EntityAttributeModifier.Operation.ADD_VALUE), AttributeModifierSlot.ANY)
                .build().withShowInTooltip(false);
    }
}
