package net.zharok01.coralinesystems.mixin.compat;

import me.jellysquid.mods.sodium.client.render.SodiumWorldRenderer;
import me.jellysquid.mods.sodium.client.render.chunk.RenderSectionManager;
import me.jellysquid.mods.sodium.client.render.viewport.Viewport;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.zharok01.coralinesystems.client.PhotoModeScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SodiumWorldRenderer.class)
public abstract class MixinSodiumWorldRenderer {

	@Shadow(remap = false) private RenderSectionManager renderSectionManager;

	@Unique private double lastCameraZoom = Double.MIN_VALUE;

	@Inject(method = "setupTerrain", at = @At(value = "HEAD"), remap = false)
	private void photoMode$hackDirtyFlag(CallbackInfo ci) {
        if (Minecraft.getInstance().screen instanceof PhotoModeScreen photoModeScreen) {
            double zoom = photoModeScreen.getZoom(Minecraft.getInstance().getFrameTime());
            if (lastCameraZoom != zoom) {
                lastCameraZoom = zoom;
				this.renderSectionManager.markGraphDirty();
            }
        }
	}

//    @ModifyVariable(method = "setupTerrain", at = @At(value = "LOAD"), ordinal = 2, remap = false)
//    private boolean photoMode$hackDirtyFlag(boolean dirty) {
//        if (Minecraft.getInstance().screen instanceof PhotoModeScreen photoModeScreen) {
//            double zoom = photoModeScreen.getZoom(Minecraft.getInstance().getFrameTime());
//            if (lastCameraZoom != zoom) {
//                lastCameraZoom = zoom;
//                return true;
//            }
//        }
//
//        return dirty;
//    }

}
