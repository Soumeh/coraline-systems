package net.zharok01.coralinesystems.client;

import net.zharok01.coralinesystems.mixin.AccessPostEffectPass;
import com.mojang.blaze3d.pipeline.RenderTarget;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.server.packs.resources.ResourceManager;

import java.io.IOException;

public class PhotoModePostEffectPass extends PostPass {

	private final PhotoModeScreen photoModeScreen;

	public PhotoModePostEffectPass(PhotoModeScreen screen, ResourceManager resourceManager, String programName, RenderTarget input, RenderTarget output) throws IOException {
		super(resourceManager, programName, input, output);
		photoModeScreen = screen;
	}

	@Override
	@SuppressWarnings("resource")
	public void process(float partialTick) {
		((AccessPostEffectPass) this).photoMode$getEffect().getUniform("Intensity").set(photoModeScreen.shaderIntensity);
		super.process(partialTick);
	}

}