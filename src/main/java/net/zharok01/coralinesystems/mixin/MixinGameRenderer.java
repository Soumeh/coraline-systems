package net.zharok01.coralinesystems.mixin;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import net.zharok01.coralinesystems.client.PhotoModeScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GameRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public abstract class MixinGameRenderer {

	@Shadow public abstract float getDepthFar();

//	@Unique protected float delta = 0.0F;

	@Inject(method = "bobView", at = @At("HEAD"), cancellable = true)
	private void injectBobView(PoseStack poseStack, float partialTick, CallbackInfo ci, @Share("delta") LocalFloatRef delta) {
//		this.delta = partialTick;
		delta.set(partialTick);
		if (Minecraft.getInstance().screen instanceof PhotoModeScreen) ci.cancel();
	}

	@Inject(method = "renderItemInHand", at = @At("HEAD"), cancellable = true)
	private void injectRenderHand(CallbackInfo ci) {
		if (Minecraft.getInstance().screen instanceof PhotoModeScreen) ci.cancel();
	}

	@Inject(method = "getProjectionMatrix", at = @At("RETURN"), cancellable = true)
	private void injectGetBasicProjectionMatrix(CallbackInfoReturnable<Matrix4f> cir, @Share("delta") LocalFloatRef delta) {
		if (Minecraft.getInstance().screen instanceof PhotoModeScreen photoModeScreen) {
			float div = (float) Math.pow(2.0, photoModeScreen.getZoom(delta.get()));
			float width = Minecraft.getInstance().getWindow().getWidth() / div;
			float height = Minecraft.getInstance().getWindow().getHeight() / div;
			PoseStack poseStack = new PoseStack();
			try {
				poseStack.last().pose().identity();
				cir.setReturnValue(poseStack.last().pose().mul((new Matrix4f())
						.setOrtho(-width, width, -height, height, this.getDepthFar() * -2.0f, this.getDepthFar() * 2.0f)
						.translate(photoModeScreen.getPanX(delta.get()), -photoModeScreen.getPanY(delta.get()), 0.0F)));
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;render(Lnet/minecraft/client/gui/GuiGraphics;F)V"))
	private void photoMode$forceHideHud(Gui instance, GuiGraphics guiGraphics, float partialTick) {
		if (!(Minecraft.getInstance().screen instanceof PhotoModeScreen)) instance.render(guiGraphics, partialTick);
	}

}