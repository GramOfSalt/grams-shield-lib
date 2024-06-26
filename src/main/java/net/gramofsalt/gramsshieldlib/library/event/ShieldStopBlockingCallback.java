package net.gramofsalt.gramsshieldlib.library.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;

/**
 * Can be used to run code whenever an entity stops blocking with a shield
 */
public interface ShieldStopBlockingCallback {
    Event<ShieldStopBlockingCallback> EVENT = EventFactory.createArrayBacked(ShieldStopBlockingCallback.class,
            (listeners) -> (activeItemstack, world, user) -> {
                for (ShieldStopBlockingCallback listener : listeners) {
                    ActionResult result = listener.stopBlocking(activeItemstack, world, user);

                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }

                return ActionResult.PASS;
            });

    ActionResult stopBlocking(ItemStack activeItemstack, World world, LivingEntity user);
}
