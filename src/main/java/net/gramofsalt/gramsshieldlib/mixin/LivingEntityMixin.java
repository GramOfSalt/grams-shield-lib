package net.gramofsalt.gramsshieldlib.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.gramofsalt.gramsshieldlib.library.config.GramsShieldLibConfig;
import net.gramofsalt.gramsshieldlib.library.event.ShieldHitCallback;
import net.gramofsalt.gramsshieldlib.library.event.ShieldStartBlockingCallback;
import net.gramofsalt.gramsshieldlib.library.event.ShieldStopBlockingCallback;
import net.gramofsalt.gramsshieldlib.library.util.ShieldAttributes;
import net.gramofsalt.gramsshieldlib.library.util.ShieldTags;
import net.gramofsalt.gramsshieldlib.library.item.ModShield;
import net.gramofsalt.gramsshieldlib.library.item.material.PredefinedShieldMaterials;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixin that heavily modifiers livingEntity code to work with new shield mechanics
 */
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow protected ItemStack activeItemStack;

    @Unique private final LivingEntity entity = (LivingEntity) (Object) this;
    @Unique private final World world = entity.getWorld();
    @Unique private ItemStack lastActiveItem = ItemStack.EMPTY;

    /**
     * Modify constant that sets damage amount to zero on shield hit. Now accounts for SHIELD_MAXIMUM_DAMAGE and SHIELD_EXPLOSION_RESISTANCE.
     */
    @ModifyConstant(method = "damage", constant = @Constant(floatValue = 0.0f), slice = @Slice(
            from = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;damageShield(F)V"),
            to = @At(value = "INVOKE", target = "Lnet/minecraft/entity/damage/DamageSource;isIn(Lnet/minecraft/registry/tag/TagKey;)Z", ordinal = 1)))
    private float explosionDamageModifier(float constant, DamageSource source, @Local(ordinal = 0, argsOnly = true) float amount) {
        ItemStack activeItem = entity.getActiveItem();
        if (activeItem == null || source.isIn(ShieldTags.DamageTypeTags.NEVER_DAMAGE_WHEN_BLOCKED)) return constant;
        float maximumDamage = (float) ShieldAttributes.getModifierValue(activeItem, ShieldAttributes.SHIELD_MAXIMUM_DAMAGE_MODIFIER_ID);
        float damageResistance = (float) ShieldAttributes.getModifierValue(activeItem, ShieldAttributes.SHIELD_GENERIC_DAMAGE_RESISTANCE_MODIFIER_ID);
        float explosionResistance = (float) ShieldAttributes.getModifierValue(activeItem, ShieldAttributes.SHIELD_EXPLOSION_RESISTANCE_MODIFIER_ID);
        if (GramsShieldLibConfig.shield_behavior == GramsShieldLibConfig.BehaviorEnum.FULL_NERF) {
            amount = Math.max(amount - maximumDamage, 0);
            if (amount > 0) amount *= (1 - damageResistance);
        } else if (source.isIn(DamageTypeTags.IS_EXPLOSION) && GramsShieldLibConfig.shield_behavior == GramsShieldLibConfig.BehaviorEnum.SEMI_NERF) {
            amount *= (1 - explosionResistance);
        } else amount = constant;
        return amount;
    }

    /**
     * Disables shield if the damages source is in the "disables shields" tag upon blocking an attack using a shield
     */
    @Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;damageShield(F)V"))
    private void disableShield(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (source.isIn(ShieldTags.DamageTypeTags.DISABLES_SHIELDS) && entity instanceof PlayerEntity player) {
            player.disableShield();
        }
    }

    /**
     * Modify constant that determines shield blocking delay. Now uses SHIELD_DELAY attribute to determine the delay
     */
    @ModifyConstant(method = "isBlocking", constant = @Constant(intValue = 5))
    private int shieldDelay(int constant) {
        ItemStack activeItem = entity.getActiveItem();
        if (ShieldAttributes.containsModifier(activeItem, ShieldAttributes.SHIELD_DELAY_MODIFIER_ID)) {
            constant = (int) ShieldAttributes.getModifierValue(activeItem, ShieldAttributes.SHIELD_DELAY_MODIFIER_ID);
        }
        return constant;
    }

    /**
     * Damage sources not under the "no shield knockback" tag will now cause a velocity update even if the user blocked the attack.
     */
    @Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/damage/DamageSource;isIn(Lnet/minecraft/registry/tag/TagKey;)Z", ordinal = 6))
    private void modifyKnockbackCriteria(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir, @Local(ordinal = 1) boolean bl) {
        if (!(source.isIn(DamageTypeTags.NO_IMPACT) || bl && source.isIn(ShieldTags.DamageTypeTags.NO_SHIELD_KNOCKBACK))) {
            entity.velocityModified = true;
        }
    }

    /**
     * Modifies the "takeKnockback" method to account for SHIELD_KNOCKBACK_RESISTANCE. Separate from GENERIC_KNOCKBACK_RESISTANCE calculation
     */
    @ModifyVariable(method = "takeKnockback", at = @At(value = "HEAD"), ordinal = 0, argsOnly = true)
    private double modifyKnockbackStrength(double strength) {
        ItemStack activeItem = entity.getActiveItem();
        if (ShieldAttributes.containsModifier(activeItem, ShieldAttributes.SHIELD_KNOCKBACK_RESISTANCE_MODIFIER_ID) && GramsShieldLibConfig.shield_behavior != GramsShieldLibConfig.BehaviorEnum.VANILLA) {
            strength *= 1.0f - ShieldAttributes.getModifierValue(activeItem, ShieldAttributes.SHIELD_KNOCKBACK_RESISTANCE_MODIFIER_ID);
        }
        return strength;
    }

    /**
     * Nullifies knockback from the "takeShieldHit" method since it was redundant and made knockback act strange.
     */
    @ModifyArg(method = "knockback", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;takeKnockback(DDD)V"), index = 0)
    private double nullifyRedundantKnockback(double strength) {
        return 0;
    }

    /**
     * Modifies "playSound" argument to use the sound associated with the active item, usually an instance of ModShield or vanilla shield, rather than only using the
     * vanilla shield sound.
     */
    @ModifyArg(method = "handleStatus", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;playSound(Lnet/minecraft/sound/SoundEvent;FF)V"))
    private SoundEvent modifyBlockSound(SoundEvent blockSound) {
        ItemStack activeItem = entity.getActiveItem();
        if (activeItem.getItem() instanceof ModShield shieldItem) {
            return shieldItem.getBlockSound();
        }
        if (activeItem.getItem() instanceof ShieldItem) {
            return PredefinedShieldMaterials.VANILLA_SHIELD.getBlockSound();
        }
        return blockSound;
    }

    /**
     * Handles the "onShieldHit" method.
     */
    @Inject(method = "damage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;damageShield(F)V"))
    public void onSuccessfulBlock(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (activeItemStack.getItem() instanceof ModShield shield) {
            shield.onShieldHit(activeItemStack, world, entity, amount, source);
            ShieldHitCallback.EVENT.invoker().shieldHit(activeItemStack, world, entity, amount, source);
        }
    }

    /**
     * Handles the "onStartBlocking" and "onStoppedBlocking" methods.
     */
    @Inject(method = "tick", at = @At(value = "TAIL"))
    public void onStoppedBlocking(CallbackInfo info) {
        if (entity.isBlocking()) {
            if (lastActiveItem == null || lastActiveItem.isEmpty()) {
                if (activeItemStack.getItem() instanceof ModShield shield) {
                    shield.onStartBlocking(activeItemStack, world, entity);
                    ShieldStartBlockingCallback.EVENT.invoker().startBlocking(activeItemStack, world, entity);
                }
                lastActiveItem = activeItemStack;
            } else if (activeItemStack != lastActiveItem) {
                if (activeItemStack.getItem() instanceof ModShield shield) {
                    shield.onStoppedBlocking(lastActiveItem, world, entity);
                    ShieldStopBlockingCallback.EVENT.invoker().stopBlocking(activeItemStack, world, entity);
                }
                lastActiveItem = ItemStack.EMPTY;
            }
        } else if (lastActiveItem != null && !lastActiveItem.isEmpty()) {
            if (lastActiveItem.getItem() instanceof ModShield shield) {
                shield.onStoppedBlocking(lastActiveItem, world, entity);
                ShieldStopBlockingCallback.EVENT.invoker().stopBlocking(activeItemStack, world, entity);
            }
            lastActiveItem = ItemStack.EMPTY;
        }
    }
}