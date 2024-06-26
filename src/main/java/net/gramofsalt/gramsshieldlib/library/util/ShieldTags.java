package net.gramofsalt.gramsshieldlib.library.util;

import net.gramofsalt.gramsshieldlib.GramsShieldLib;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

/**
 * Defines tags, so they can be called within projects
 */
public class ShieldTags {
    /**
     * Defines SHIELDS, SHIELDS_ENCHANTABLE, and DECORABLE
     * SHIELDS and SHIELDS_ENCHANTABLE are required so enchantments can be applied to shields.
     * DECORABLE allows an item (such as a shield) to be crafted with a banner to add banner pattern components
     */
    public interface ItemTags {
        public static final TagKey<Item> SHIELDS = createTag("shields");
        public static final TagKey<Item> SHIELDS_ENCHANTABLE = createTag("enchantable/shield");
        public static final TagKey<Item> DECORABLE = createTag("decorable");
        public static final TagKey<Item> TRIMMABLE_SHIELDS = createTag("trimmable_shields");

        private static TagKey<Item> createTag(String name) {
            return TagKey.of(RegistryKeys.ITEM, Identifier.of(GramsShieldLib.MODID, name));
        }
    }
    /**
     * Defines DISABLES_SHIELDS, NEVER_DAMAGE_WHEN_BLOCKED, and NO_SHIELD_KNOCKBACK.
     * These tags are used within mixins to influence shield behavior.
     */
    public interface DamageTypeTags {
        public static final TagKey<DamageType> DISABLES_SHIELDS = createTag("disables_shields");
        public static final TagKey<DamageType> NEVER_DAMAGE_WHEN_BLOCKED = createTag("never_damage_when_blocked");
        public static final TagKey<DamageType> NO_SHIELD_KNOCKBACK = createTag("no_shield_knockback");

        private static TagKey<DamageType> createTag(String name) {
            return TagKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of(GramsShieldLib.MODID, name));
        }
    }
}
