package net.zharok01.coralinesystems.mixin;

import net.zharok01.coralinesystems.client.PhotoModeScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.culling.Frustum;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Frustum.class)
public abstract class MixinFrustum {

//	@Inject(method = "offsetToFullyIncludeCameraCube", at = @At("HEAD"), cancellable = true)
//	private void photoMode$coverBoxAroundSetPosition(int size, CallbackInfoReturnable<Frustum> cir) {
//		if (Minecraft.getInstance().screen instanceof PhotoModeScreen photoModeScreen) {
//			float div = (float) Math.pow(2.0, photoModeScreen.getZoom(Minecraft.getInstance().getFrameTime()));
//			float width = Minecraft.getInstance().getWindow().getWidth() / div;
//			float height = Minecraft.getInstance().getWindow().getHeight() / div;
//			if (width <= 10 || height <= 10) {
//				cir.setReturnValue((Frustum) ((Object) this));
//			}
//		}
//	}

}