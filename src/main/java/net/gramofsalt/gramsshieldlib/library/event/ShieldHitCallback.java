package net.gramofsalt.gramsshieldlib.library.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;

/**
 * Can be used to run code whenever a shield is hit by an attack
 */
public interface ShieldHitCallback {
    Event<ShieldHitCallback> EVENT = EventFactory.createArrayBacked(ShieldHitCallback.class,
            (listeners) -> (activeItemstack, world, user, amount, source) -> {
                for (ShieldHitCallback listener : listeners) {
                    ActionResult result = listener.shieldHit(activeItemstack, world, user, amount, source);

                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }

                return ActionResult.PASS;
            });

    ActionResult shieldHit(ItemStack activeItemstack, World world, LivingEntity user, double amount, DamageSource source);
}
