package net.gramofsalt.gramsshieldlib;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.gramofsalt.gramsshieldlib.example.ExampleRenderer;
import net.gramofsalt.gramsshieldlib.library.util.ShieldUtils;

@Environment(value= EnvType.CLIENT)
public class GramsShieldLibClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        GramsShieldLib.LOGGER.info("Initializing client");

        ShieldUtils.registerVanillaShieldLayers();
        ShieldUtils.appendShieldTooltips();

        // Dev environment code
        if(FabricLoader.getInstance().isDevelopmentEnvironment()) {
            ExampleRenderer.renderExampleShields();
        }
    }
}