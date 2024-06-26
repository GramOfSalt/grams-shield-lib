package net.gramofsalt.gramsshieldlib.library.util;

import net.gramofsalt.gramsshieldlib.GramsShieldLib;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

/**
 * Register shield attributes that determine their behavior. Can be modified upon registering a new shield item or through enchantments.
 */
public class ShieldAttributes {
    /**
     * Objects that can be used to retrieve the UUID of a given attribute.
     */
    public static final Identifier SHIELD_COOLDOWN_MODIFIER_ID = Identifier.of(GramsShieldLib.MODID, "shield_cooldown");
    public static final Identifier SHIELD_DELAY_MODIFIER_ID = Identifier.of(GramsShieldLib.MODID, "shield_delay");
    public static final Identifier SHIELD_MAXIMUM_DAMAGE_MODIFIER_ID = Identifier.of(GramsShieldLib.MODID, "shield_maximum_damage");
    public static final Identifier SHIELD_GENERIC_DAMAGE_RESISTANCE_MODIFIER_ID = Identifier.of(GramsShieldLib.MODID, "shield_generic_damage_resistance");
    public static final Identifier SHIELD_KNOCKBACK_RESISTANCE_MODIFIER_ID = Identifier.of(GramsShieldLib.MODID, "shield_knockback_resistance");
    public static final Identifier SHIELD_EXPLOSION_RESISTANCE_MODIFIER_ID = Identifier.of(GramsShieldLib.MODID, "shield_explosion_resistance");
    public static final Identifier SHIELD_DURABILITY_MULTIPLIER_MODIFIER_ID = Identifier.of(GramsShieldLib.MODID, "shield_durability_multiplier");
    public static final Identifier SHIELD_MINIMUM_DURABILITY_DAMAGE_MODIFIER_ID = Identifier.of(GramsShieldLib.MODID, "shield_minimum_durability_damage");
    public static final Identifier SHIELD_BLOCKING_SPEED_MODIFIER_ID = Identifier.of(GramsShieldLib.MODID, "shield_blocking_speed");

    /**
     * Shield Cooldown - Attribute that determines the base cooldown duration, in ticks, after blocking a damage source that disables shields.
     * Min Value: 0.0
     * Max Value: 1200.0
     * Vanilla Value: 100
     */
    public static final RegistryEntry<EntityAttribute> SHIELD_COOLDOWN = ShieldAttributes.register("shield.cooldown", new ClampedEntityAttribute("attribute.name.shield.cooldown", 100.0, 0.0, 1200.0));

    /**
     * Shield Delay - Attribute that determines the number of ticks required to start blocking after using the shield item.
     * Min Value: 0.0
     * Max Value: 1200.0
     * Vanilla Value: 5
     */
    public static final RegistryEntry<EntityAttribute> SHIELD_DELAY = ShieldAttributes.register("shield.delay", new ClampedEntityAttribute("attribute.name.shield.delay", 5.0, 0.0, 1200.0));

    /**
     * Shield Maximum Damage - Attribute that determines the maximum damage value that can be blocked. Values greater than the attribute value will be subtracted
     *                         by the attribute value. Negative values result in no maximum value.
     * Min Value: -1.0
     * Max Value: 1000.0
     * Vanilla Value: N/A
     */
    public static final RegistryEntry<EntityAttribute> SHIELD_MAXIMUM_DAMAGE = ShieldAttributes.register("shield.maximum_damage",  new ClampedEntityAttribute("attribute.name.shield.maximum_damage", 1000.0, 0.0, 1000.0));

    /**
     * Shield Generic Damage Resistance - Attribute that reduces damage from all sources. Multiplied by the base damage, after maximum damage reduction is applied, to
     *                                    determine the final value.
     * Min Value: 0.0
     * Max Value: 1.0
     * Vanilla Value: 1.0
     */
    public static final RegistryEntry<EntityAttribute> SHIELD_GENERIC_DAMAGE_RESISTANCE = ShieldAttributes.register("shield.damage_resistance",  new ClampedEntityAttribute("attribute.name.shield.damage_resistance", 0.0, 0.0, 1.0));

    /**
     * Shield Knockback Resistance - Attribute that reduces knockback from blocked attacks. Multiplied by base knockback strength to determine the final value.
     *                               Independent of EntityAttribute.GENERIC_KNOCKBACK_RESISTANCE.
     * Min Value: 0.0
     * Max Value: 1.0
     * Vanilla Value: 1.0
     */
    public static final RegistryEntry<EntityAttribute> SHIELD_KNOCKBACK_RESISTANCE = ShieldAttributes.register("shield.knockback_resistance", new ClampedEntityAttribute("attribute.name.shield.knockback_resistance", 0.0, 0.0, 1.0));

    /**
     * Shield Explosion Resistance - Attribute that reduces damage from explosions. Multiplied by base explosion damage to determine the final value. Does nothing if
     *                               Shield Maximum Damage and Shield Generic Damage Resistance are enabled.
     * Min Value: 0.0
     * Max Value: 1.0
     * Vanilla Value: 1.0
     */
    public static final RegistryEntry<EntityAttribute> SHIELD_EXPLOSION_RESISTANCE = ShieldAttributes.register("shield.explosion_resistance", new ClampedEntityAttribute("attribute.name.shield.explosion_resistance", 0.0, 0.0, 1.0));

    /**
     * Shield Durability Multiplier - Attribute that determines the amount of durability a shield loses upon blocking an attack. Multiplied by the attack's damage to
     *                                determine durability deducted. The final value can be no less than 1.
     * Min Value: 0.0
     * Max Value: 100.0
     * Vanilla Value: 1.0
     */
    public static final RegistryEntry<EntityAttribute> SHIELD_DURABILITY_MULTIPLIER = ShieldAttributes.register("shield.durability_multiplier", new ClampedEntityAttribute("attribute.name.shield.durability_multiplier", 1.0, 0.0, 100.0));

    /**
     * Shield Minimum Durability Damage - Attribute that determines the minimum damage an attack needs to deal to damage the shield. Unaffected by Shield Durability
     *                                    Multiplier.
     * Min Value: 0.0
     * Max Value: 1000.0
     * Vanilla Value: 3.0
     */
    public static final RegistryEntry<EntityAttribute> SHIELD_MINIMUM_DURABILITY_DAMAGE = ShieldAttributes.register("shield.minimum_durability_damage", new ClampedEntityAttribute("attribute.name.shield.minimum_durability_damage", 3.0, 0.0, 1000.0));

    /**
     * Shield Blocking Speed - Attribute that determines movement speed while blocking. Multiplied by the base movement speed of the user to determine movement speed
     *                         while blocking.
     * Min Value: 0.0
     * Max Value: 1.0
     * Vanilla Value: 0.2
     */
    public static final RegistryEntry<EntityAttribute> SHIELD_BLOCKING_SPEED = ShieldAttributes.register("shield.blocking_speed", new ClampedEntityAttribute("attribute.name.shield.blocking_speed", 0.2, 0.0, 1.0));

    /**
     * Registers custom attribute modifiers.
     *
     * @param id - String ID of attribute modifier
     * @param attribute - EntityAttribute object of the attribute modifier
     * @return RegistryEntry<EntityAttribute>
     */
    public static RegistryEntry<EntityAttribute> register(String id, EntityAttribute attribute) {
        return Registry.registerReference(Registries.ATTRIBUTE, Identifier.of(id), attribute);
    }

    /**
     * Easily access attribute modifiers on a given item.
     *
     * @param item - ItemStack being accessed
     * @param ID - String Identifier of attribute modifier that the method is attempting to retrieve a value for
     * @return double - Returns 0 if the attribute modifier is not present on the item
     */
    public static double getModifierValue(ItemStack item, Identifier ID) {
        AttributeModifiersComponent modifier = item.getOrDefault(DataComponentTypes.ATTRIBUTE_MODIFIERS, AttributeModifiersComponent.DEFAULT);
        for (AttributeModifiersComponent.Entry entry : modifier.modifiers()) {
            if (entry.modifier().idMatches(ID)) {
                return entry.modifier().value();
            }
        }
        return 0;
    }

    /**
     * Easily determine if an item has a specific attribute modifier.
     *
     * @param item - ItemStack being accessed
     * @param ID - UUID of attribute modifier that the method is checking for.
     * @return boolean - Outcome of check
     */
    public static boolean containsModifier(ItemStack item, Identifier ID) {
        AttributeModifiersComponent modifier = item.getOrDefault(DataComponentTypes.ATTRIBUTE_MODIFIERS, AttributeModifiersComponent.DEFAULT);
        for (AttributeModifiersComponent.Entry entry : modifier.modifiers()) {
            if (entry.modifier().idMatches(ID)) {
                return true;
            }
        }
        return false;
    }
}
