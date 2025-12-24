package net.zharok01.coralinesystems.mixin;

import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.client.renderer.PostPass;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PostPass.class)
public interface AccessPostEffectPass {

	@Accessor("effect")
	EffectInstance photoMode$getEffect();

}