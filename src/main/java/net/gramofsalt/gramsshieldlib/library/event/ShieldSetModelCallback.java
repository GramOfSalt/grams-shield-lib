package net.gramofsalt.gramsshieldlib.library.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.util.ActionResult;

/**
 * Used to set the model of BuiltinModel Shields
 */
public interface ShieldSetModelCallback {
    Event<ShieldSetModelCallback> EVENT = EventFactory.createArrayBacked(ShieldSetModelCallback.class,
            (listeners) -> (loader) -> {
                for (ShieldSetModelCallback listener : listeners) {
                    ActionResult result = listener.setModel(loader);

                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }

                return ActionResult.PASS;
            });

    ActionResult setModel(EntityModelLoader loader);
}
