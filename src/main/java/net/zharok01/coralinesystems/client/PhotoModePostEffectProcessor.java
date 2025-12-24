package net.zharok01.coralinesystems.client;

import com.google.gson.JsonSyntaxException;
import net.zharok01.coralinesystems.mixin.AccessPostEffectProcessor;
import com.mojang.blaze3d.pipeline.RenderTarget;
import net.minecraft.client.renderer.PostPass;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;

public class PhotoModePostEffectProcessor extends PostChain {

	private static PhotoModeScreen photoModeScreen;

	public PhotoModePostEffectProcessor(PhotoModeScreen screen, TextureManager textureManager, ResourceManager resourceManager, RenderTarget framebuffer, ResourceLocation id) throws IOException, JsonSyntaxException {
		super(hack(screen, textureManager), resourceManager, framebuffer, id);
	}

	private static TextureManager hack(PhotoModeScreen screen, TextureManager textureManager) {
		photoModeScreen = screen;
		return textureManager;
	}

	@Override
	public PostPass addPass(String programName, RenderTarget source, RenderTarget dest) throws IOException {
		AccessPostEffectProcessor access = (AccessPostEffectProcessor) this;

		PhotoModePostEffectPass postEffectPass = new PhotoModePostEffectPass(photoModeScreen, access.photoMode$getResourceManager(), programName, source, dest);
		access.photoMode$getPasses().add(access.photoMode$getPasses().size(), postEffectPass);
		return postEffectPass;
	}

}