package net.zharok01.coralinesystems.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalDoubleRef;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import net.zharok01.coralinesystems.client.PhotoModeScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LevelRenderer.class, priority = 999)
public abstract class MixinWorldRendererVanilla {

//	@Unique
//	private double lastCameraZoom = Double.MIN_VALUE;

	@ModifyExpressionValue(
			method = "setupRender",
			at = @At(value = "INVOKE", target = "Ljava/util/concurrent/atomic/AtomicBoolean;compareAndSet(ZZ)Z")
	)
	private boolean photoMode$shouldUpdateFrustum(boolean original, @Share("zoom") LocalDoubleRef lastZoom) {
		if (Minecraft.getInstance().screen instanceof PhotoModeScreen photoModeScreen) {
			double zoom = photoModeScreen.getZoom(Minecraft.getInstance().getFrameTime());
			return original || lastZoom.get() != zoom;
		} else return original;
	}

	@Inject(
			method = "setupRender",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/renderer/culling/Frustum;offsetToFullyIncludeCameraCube(I)Lnet/minecraft/client/renderer/culling/Frustum;",					shift = At.Shift.AFTER
			)
	)
	private void photoMode$updateLastZoom(CallbackInfo ci, @Share("zoom") LocalDoubleRef lastZoom) {
		if (Minecraft.getInstance().screen instanceof PhotoModeScreen photoModeScreen) {
			lastZoom.set(photoModeScreen.getZoom(Minecraft.getInstance().getFrameTime()));
		}
	}

}