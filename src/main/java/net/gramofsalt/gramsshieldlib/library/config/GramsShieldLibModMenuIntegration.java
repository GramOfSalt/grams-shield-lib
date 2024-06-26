package net.gramofsalt.gramsshieldlib.library.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import eu.midnightdust.lib.config.MidnightConfig;
import net.gramofsalt.gramsshieldlib.GramsShieldLib;

public class GramsShieldLibModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> MidnightConfig.getScreen(parent, GramsShieldLib.MODID);
    }
}
