package net.gramofsalt.gramsshieldlib.library.config;

import eu.midnightdust.lib.config.MidnightConfig;

public class GramsShieldLibConfig extends MidnightConfig {

    @Entry(category = "global")
    public static boolean enable_tooltip = true;

    @Entry(category = "global")
    public static boolean enable_delay_tooltip = true;

    @Entry(category = "global")
    public static boolean enable_cooldown_tooltip = true;

    @Entry(category = "global")
    public static boolean enable_max_damage_tooltip = true;

    @Entry(category = "global")
    public static boolean enable_damage_resistance_tooltip = true;

    @Entry(category = "global")
    public static boolean enable_knockback_resistance_tooltip = true;

    @Entry(category = "global")
    public static boolean enable_explosion_resistance_tooltip = true;

    @Entry(category = "global")
    public static boolean enable_durability_multiplier_tooltip = true;

    @Entry(category = "global")
    public static boolean enable_min_durability_damage_tooltip = false;

    @Entry(category = "global")
    public static boolean enable_movement_speed_multiplier_tooltip = false;

    @Entry(category = "global")
    public static BehaviorEnum shield_behavior = BehaviorEnum.VANILLA;
    public enum BehaviorEnum {
        VANILLA, FULL_NERF, SEMI_NERF
    }

    @Comment(category = "global")
    public static Comment behavior_comment;

    @Entry(category = "vanilla_shield")
    public static int vanilla_shield_durability = 570;

    @Entry(category = "vanilla_shield", min = 0, max = 1200)
    public static int vanilla_shield_cooldown = 100;

    @Entry(category = "vanilla_shield", min = 0, max = 1200)
    public static int vanilla_shield_delay = 5;

    @Entry(category = "vanilla_shield", min = 0, max = 100)
    public static float vanilla_shield_max_damage = 0F;

    @Entry(category = "vanilla_shield", isSlider = true, min = 0, max = 1.0)
    public static float vanilla_shield_damage_resistance = 0.5F;

    @Entry(category = "vanilla_shield", isSlider = true, min = 0, max = 1.0)
    public static float vanilla_shield_knockback_resistance = 0.5F;

    @Entry(category = "vanilla_shield", isSlider = true, min = 0, max = 1.0)
    public static float vanilla_shield_explosion_resistance = 0.5F;

    @Entry(category = "vanilla_shield", min = 0, max = 100)
    public static float vanilla_shield_durability_multiplier = 1.0F;

    @Entry(category = "vanilla_shield", min = 0, max = 1000)
    public static float vanilla_shield_min_durability_damage = 3F;

    @Entry(category = "vanilla_shield", isSlider = true, min = 0, max = 1.0)
    public static float vanilla_shield_movement_speed_multiplier = 0.2F;

    @Entry(category = "vanilla_shield")
    public static int vanilla_shield_enchantablity = 15;
}
