package net.gramofsalt.gramsshieldlib.mixin;

import net.gramofsalt.gramsshieldlib.GramsShieldLib;
import net.gramofsalt.gramsshieldlib.example.ExampleRenderer;
import net.gramofsalt.gramsshieldlib.library.util.ShieldUtils;
import net.minecraft.client.render.model.BakedModelManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Mixin that registers new atlases.
 * Library Dev only. To add new atlases, this mixin will have to be created independently.
 */
@Mixin(BakedModelManager.class)
public class BakedModelManagerMixin {
    @Mutable
    @Shadow @Final private static Map<Identifier, Identifier> LAYERS_TO_LOADERS;

    @Inject(method = "<clinit>", at = @At(value = "TAIL"))
    private static void registerCustomAtlas(CallbackInfo ci) {
        LAYERS_TO_LOADERS = new HashMap<>(LAYERS_TO_LOADERS);
        LAYERS_TO_LOADERS.put(ShieldUtils.VANILLA_SHIELD_PATTERNS_ATLAS, Identifier.of(GramsShieldLib.MODID, "vanilla_shield_patterns"));
        LAYERS_TO_LOADERS.put(ShieldUtils.VANILLA_SHIELD_TRIMS_ATLAS, Identifier.of(GramsShieldLib.MODID, "vanilla_shield_trims"));
        LAYERS_TO_LOADERS.put(ExampleRenderer.EXAMPLE_SHIELD_PATTERNS_ATLAS, Identifier.of(GramsShieldLib.MODID, "example_shield_patterns"));
    }
}
