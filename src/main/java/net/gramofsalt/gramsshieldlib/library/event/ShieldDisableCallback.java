package net.gramofsalt.gramsshieldlib.library.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;

/**
 * Can be used to run code whenever a shield is disabled by an attack
 */
public interface ShieldDisableCallback {
    Event<ShieldDisableCallback> EVENT = EventFactory.createArrayBacked(ShieldDisableCallback.class,
            (listeners) -> (activeItemstack, world, user) -> {
                for (ShieldDisableCallback listener : listeners) {
                    ActionResult result = listener.shieldDisabled(activeItemstack, world, user);

                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }

                return ActionResult.PASS;
            });

    ActionResult shieldDisabled(ItemStack activeItemstack, World world, LivingEntity user);
}
