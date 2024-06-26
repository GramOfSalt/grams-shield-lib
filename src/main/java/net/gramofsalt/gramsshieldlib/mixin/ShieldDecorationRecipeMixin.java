package net.gramofsalt.gramsshieldlib.mixin;

import net.gramofsalt.gramsshieldlib.library.util.ShieldTags;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.ShieldDecorationRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Mixin that changes the banner decoration recipe to work an any item with the "decorable" tag
 */
@Mixin(ShieldDecorationRecipe.class)
public class ShieldDecorationRecipeMixin {

    /**
     * Changes the "matches" method to check for the tag instead of ShieldItem
     */
    @Redirect(method = "matches(Lnet/minecraft/recipe/input/CraftingRecipeInput;Lnet/minecraft/world/World;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"))
    private boolean matchesUsingBannerTag(ItemStack instance, Item item) {
        return instance.isIn(ShieldTags.ItemTags.DECORABLE);
    }

    /**
     * Changes the "craft" method to check for the tag instead of ShieldItem
     */
    @Redirect(method = "craft(Lnet/minecraft/recipe/input/CraftingRecipeInput;Lnet/minecraft/registry/RegistryWrapper$WrapperLookup;)Lnet/minecraft/item/ItemStack;", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"))
    private boolean craftUsingBannerTag(ItemStack instance, Item item) {
        return instance.isIn(ShieldTags.ItemTags.DECORABLE);
    }
}
