package com.ghostskin.mixin;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

/** Erzeugt Geister-Partikel um alle Spieler, die gerade unsichtbar sind. */
@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin {

    @Inject(method = "tick(Ljava/util/function/BooleanSupplier;)V", at = @At("TAIL"))
    private void ghostskin$ghostParticles(BooleanSupplier hasTimeLeft, CallbackInfo ci) {
        ClientLevel level = (ClientLevel) (Object) this;
        RandomSource random = level.getRandom();

        for (AbstractClientPlayer player : level.players()) {
            if (!player.hasEffect(MobEffects.INVISIBILITY)) {
                continue;
            }
            // weisse Aschepartikel (haeufig)
            if (random.nextInt(2) == 0) {
                level.addParticle(ParticleTypes.WHITE_ASH,
                    player.getRandomX(0.7), player.getRandomY(), player.getRandomZ(0.7),
                    0.0, 0.02, 0.0);
            }
            // aufsteigende Seelen (selten)
            if (random.nextInt(8) == 0) {
                level.addParticle(ParticleTypes.SCULK_SOUL,
                    player.getRandomX(0.5), player.getRandomY(), player.getRandomZ(0.5),
                    0.0, 0.04, 0.0);
            }
        }
    }
}
