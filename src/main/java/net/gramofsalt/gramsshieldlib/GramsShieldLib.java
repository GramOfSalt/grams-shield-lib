package net.gramofsalt.gramsshieldlib;

import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.loader.api.FabricLoader;
import net.gramofsalt.gramsshieldlib.example.ExampleItems;
import net.gramofsalt.gramsshieldlib.library.config.GramsShieldLibConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GramsShieldLib implements ModInitializer {

	/**
	 * Gram's Shield Lib ID
	 */
	public static final String MODID = "grams-shield-lib";

	/**
	 * Gram's Shield Lib logger
	 */
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing GramsShieldLib");

		MidnightConfig.init(MODID, GramsShieldLibConfig.class);

		// Dev environment code
		if(FabricLoader.getInstance().isDevelopmentEnvironment()) {
			LOGGER.warn("GRAMS SHIELD LIB DEV ENVIRONMENT CODE RAN! Test items will be present in-game!");
			ExampleItems.registerModItems();
		}
	}
}