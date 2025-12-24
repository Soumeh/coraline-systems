package net.zharok01.coralinesystems.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.PostChain;
import net.zharok01.coralinesystems.client.PhotoModeScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class MixinWorldRenderer {

    @Inject(method = "renderSky", at = @At("HEAD"), cancellable = true)
    private void injectRenderSky(CallbackInfo ci) {
        if (Minecraft.getInstance().screen instanceof PhotoModeScreen) ci.cancel();
    }

    @Inject(method = "renderClouds", at = @At("HEAD"), cancellable = true)
    public void injectRenderClouds(CallbackInfo ci) {
        if (Minecraft.getInstance().screen instanceof PhotoModeScreen) ci.cancel();
    }

    /**
     * Fixes depth buffer being messed up in fabulous graphics... Somewhat, as depth information is not present for translucent surfaces.
     * @param instance Effect processor
     * @param tickDelta Tick delta
     */
    @Redirect(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/PostChain;process(F)V", ordinal = 1))
    private void photoMode$fixFabulousDepthSomewhat(PostChain instance, float tickDelta) {
        RenderSystem.depthMask(false);
        instance.process(tickDelta);
        RenderSystem.depthMask(true);
    }

}
