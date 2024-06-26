package net.gramofsalt.gramsshieldlib.example;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.gramofsalt.gramsshieldlib.GramsShieldLib;
import net.gramofsalt.gramsshieldlib.library.item.CustomShieldItem;
import net.gramofsalt.gramsshieldlib.library.item.material.PredefinedShieldMaterials;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

/**
 * Example class that registers custom shield items
 */
public class ExampleItems {

    /**
     * Registers a basic shield item.
     * Use CustomShieldItem.createAttributeModifiers to add the shield attributes
     */
    public static final Item EXAMPLE_SHIELD = registerItem("example_shield", new CustomShieldItem(336, 14, Ingredient.fromTag(ItemTags.PLANKS), SoundEvents.ITEM_SHIELD_BLOCK, Ingredient.EMPTY,
            (new Item.Settings()).attributeModifiers(CustomShieldItem.createAttributeModifiers(100, 5, 0F, 0.5F, 0.5F, 0.5F, 1.0F, 3.0F, 0.2F))));
    /**
     * Registers a banner shield item. Identical to a normal shield item, but requires the "decorable" tag to function.
     * DOES NOT USE THE SAME MODEL AS A BASIC SHIELD. Must use builtin_shield_model.json as a parent or use builtin/entity as a parent
     * Use CustomShieldItem.createAttributeModifiers to add the shield attributes
     */
    public static final Item EXAMPLE_BANNER_SHIELD = registerItem("example_banner_shield", new CustomShieldItem(PredefinedShieldMaterials.VANILLA_SHIELD, new Item.Settings().attributeModifiers(CustomShieldItem.createAttributeModifiers(PredefinedShieldMaterials.VANILLA_SHIELD))));

    /**
     * Registers a banner shield item. Identical to a normal shield item, but requires the "dyeable" tag to function.
     * DOES NOT USE THE SAME MODEL AS A BASIC SHIELD. Must use builtin_shield_model.json as a parent or use builtin/entity as a parent
     * Use CustomShieldItem.createAttributeModifiers to add the shield attributes
     */
    public static final Item EXAMPLE_DYEABLE_SHIELD = registerItem("example_dyeable_shield", new CustomShieldItem(PredefinedShieldMaterials.VANILLA_SHIELD, new Item.Settings().attributeModifiers(CustomShieldItem.createAttributeModifiers(PredefinedShieldMaterials.VANILLA_SHIELD))));

    /**
     * Registers a banner shield item. Identical to a normal shield item, but requires the "trimmable_shields" tag to function.
     * Unlike Banner and Dye Shields, trim shields will also work with other types.
     * DOES NOT USE THE SAME MODEL AS A BASIC SHIELD. Must use builtin_shield_model.json as a parent or use builtin/entity as a parent
     * Use CustomShieldItem.createAttributeModifiers to add the shield attributes
     */
    public static final Item EXAMPLE_TRIM_SHIELD = registerItem("example_trim_shield", new CustomShieldItem(PredefinedShieldMaterials.VANILLA_SHIELD, new Item.Settings().attributeModifiers(CustomShieldItem.createAttributeModifiers(PredefinedShieldMaterials.VANILLA_SHIELD))));

    /**
     * Adds items to combat creative tabs.
     */
    private static void addToCombatGroup(FabricItemGroupEntries entries) {
        entries.add(EXAMPLE_SHIELD);
        entries.add(EXAMPLE_BANNER_SHIELD);
        entries.add(EXAMPLE_DYEABLE_SHIELD);
        entries.add(EXAMPLE_TRIM_SHIELD);
    }

    /**
     * Register item method
     *
     * @param name - Item ID
     * @param item - Item class
     * @return Item - Registered item
     */
    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(GramsShieldLib.MODID, name), item);
    }

    /**
     * Method called in main method on initialization.
     * Registers items and adds items to creative tabs.
     */
    public static void registerModItems() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(ExampleItems::addToCombatGroup);
    }
}
