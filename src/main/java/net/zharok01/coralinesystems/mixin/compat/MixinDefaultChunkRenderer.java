package net.zharok01.coralinesystems.mixin.compat;

import me.jellysquid.mods.sodium.client.model.quad.properties.ModelQuadFacing;
import me.jellysquid.mods.sodium.client.render.chunk.DefaultChunkRenderer;
import net.minecraft.client.Minecraft;
import net.zharok01.coralinesystems.client.PhotoModeScreen;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DefaultChunkRenderer.class)
public abstract class MixinDefaultChunkRenderer {

    @Inject(method = "getVisibleFaces", at = @At("HEAD"), remap = false, cancellable = true)
    private static void photoMode$sodium05x$getVisibleFaces(int originX, int originY, int originZ, int chunkX, int chunkY, int chunkZ, CallbackInfoReturnable<Integer> cir) {
        if (Minecraft.getInstance().screen instanceof PhotoModeScreen) cir.setReturnValue(ModelQuadFacing.ALL);
    }

}
