package net.zharok01.coralinesystems.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Camera;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.zharok01.coralinesystems.client.PhotoModeScreen;

@Mixin(Camera.class)
public abstract class MixinCamera {

	@Shadow private boolean detached;

	@Shadow
	protected abstract void setRotation(float yaw, float pitch);

	@Inject(method = "setup", at = @At(value = "TAIL"))
	private void injectUpdate(BlockGetter level, Entity entity, boolean detached, boolean thirdPersonReverse, float partialTick, CallbackInfo ci) {
		if (Minecraft.getInstance().screen instanceof PhotoModeScreen photoModeScreen) {
			this.detached = photoModeScreen.playerVisible;
			this.setRotation(45.0f + 45.0f * photoModeScreen.getRotation(partialTick), photoModeScreen.getTilt(partialTick));
		}
	}

}