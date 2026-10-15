package com.ghostskin.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Geister-Look fuer Spieler mit dem Unsichtbarkeits-Effekt:
 *  1. Sie werden fuer ALLE durchsichtig gezeichnet (nicht nur fuer Teammitglieder).
 *  2. Ihr Skin wird mit Weiss ueberblendet (heller, "hellgrau/pastell").
 *  3. Die Durchsichtigkeit ist etwas sichtbarer als im Original (Vanilla: ca. 15 %).
 */
@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin {

    /** Wie stark der Skin mit Weiss ueberblendet wird (0.0 = gar nicht, 1.0 = maximal). */
    private static final float GHOST_WHITE_MIX = 0.6F;

    /** Deckkraft des Geists als ARGB-Farbe. 0x66 = ca. 40 % sichtbar. Vanilla: 0x26 (ca. 15 %). */
    private static final int GHOST_ALPHA_ARGB = 0x66FFFFFF;

    private static boolean ghostskin$isGhost(LivingEntityRenderState state) {
        return state instanceof AvatarRenderState && state.isInvisible;
    }

    /**
     * Vanilla zeichnet unsichtbare Spieler nur fuer Teammitglieder durchsichtig.
     * Hier markieren wir sie fuer jeden Betrachter als "durchsichtig sichtbar".
     */
    @Inject(
        method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V",
        at = @At("TAIL")
    )
    private void ghostskin$forceTranslucent(LivingEntity entity, LivingEntityRenderState state, float partialTick, CallbackInfo ci) {
        if (ghostskin$isGhost(state)) {
            state.isInvisibleToPlayer = false;
        }
    }

    /**
     * Skin heller machen: das ist derselbe Mechanismus wie das weisse Aufblitzen
     * eines Creepers, nur dauerhaft und nur teilweise.
     * require = 0: Falls die Methode in dieser Version anders heisst, startet das Spiel trotzdem.
     */
    @ModifyReturnValue(method = "getOverlayCoords", at = @At("RETURN"), require = 0)
    private static int ghostskin$whiteOverlay(int original, LivingEntityRenderState state, float whiteOverlayProgress) {
        if (ghostskin$isGhost(state)) {
            return OverlayTexture.pack(OverlayTexture.u(GHOST_WHITE_MIX), OverlayTexture.v(false));
        }
        return original;
    }

    /**
     * Durchsichtigkeit erhoehen: ersetzt die Vanilla-Konstante 0x26FFFFFF (654311423)
     * durch einen sichtbareren Wert. Beste Bemuehung, require = 0.
     */
    @ModifyConstant(method = {"submit", "render"}, constant = @Constant(intValue = 654311423), require = 0)
    private int ghostskin$moreVisible(int original) {
        return GHOST_ALPHA_ARGB;
    }
}
