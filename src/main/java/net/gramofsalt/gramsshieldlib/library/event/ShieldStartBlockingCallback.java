package net.gramofsalt.gramsshieldlib.library.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;

/**
 * Can be used to run code whenever an entity starts blocking with a shield
 */
public interface ShieldStartBlockingCallback {
    Event<ShieldStartBlockingCallback> EVENT = EventFactory.createArrayBacked(ShieldStartBlockingCallback.class,
            (listeners) -> (activeItemstack, world, user) -> {
                for (ShieldStartBlockingCallback listener : listeners) {
                    ActionResult result = listener.startBlocking(activeItemstack, world, user);

                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }

                return ActionResult.PASS;
            });

    ActionResult startBlocking(ItemStack activeItemstack, World world, LivingEntity user);
}
