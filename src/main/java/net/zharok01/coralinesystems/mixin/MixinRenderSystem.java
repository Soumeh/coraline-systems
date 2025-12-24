package net.zharok01.coralinesystems.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.zharok01.coralinesystems.client.PhotoModeScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(RenderSystem.class)
public abstract class MixinRenderSystem {

    @ModifyVariable(method = "setShaderFogStart", at = @At(value = "HEAD"), argsOnly = true, remap = false)
    private static float injectSetShaderFogStart(float value) {
        if (Minecraft.getInstance().screen instanceof PhotoModeScreen photoModeScreen && value != Float.MAX_VALUE) {
            float fogModifier = photoModeScreen.getFog(Minecraft.getInstance().getFrameTime());
            return value * fogModifier;
        }
        return value;
    }

    @ModifyVariable(method = "setShaderFogEnd", at = @At(value = "HEAD"), argsOnly = true, remap = false)
    private static float injectSetShaderFogEnd(float value) {
        if (Minecraft.getInstance().screen instanceof PhotoModeScreen photoModeScreen) {
            float fogModifier = photoModeScreen.getFog(Minecraft.getInstance().getFrameTime());
            return value * fogModifier;
        }
        return value;
    }

}
