package net.zharok01.coralinesystems.client;

import com.google.gson.JsonSyntaxException;
import net.minecraft.client.Minecraft;
import net.zharok01.coralinesystems.CoralineSystems;
import net.zharok01.coralinesystems.mixin.AccessGameRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.util.Arrays;

public class PhotoModeUtils {

	static final ResourceLocation[] SHADER_PROGRAMS = new ResourceLocation[] {
			new ResourceLocation("photomode", "shaders/post/blur.json"),
			new ResourceLocation("photomode", "shaders/post/silhouette.json"),
			new ResourceLocation("photomode", "shaders/post/vignette.json"),
			new ResourceLocation("photomode", "shaders/post/tiltshift.json"),
			new ResourceLocation("photomode", "shaders/post/outline.json"),
			new ResourceLocation("photomode", "shaders/post/outline2.json"),
			new ResourceLocation("photomode", "shaders/post/eerie.json"),
			new ResourceLocation("photomode", "shaders/post/sepia.json"),
			new ResourceLocation("photomode", "shaders/post/inverted.json"),
			new ResourceLocation("photomode", "shaders/post/distantblur.json")
	};

	static final int SHADER_PROGRAM_COUNT = SHADER_PROGRAMS.length;

	public static void init() {
		CoralineSystems.LOGGER.info("Photo Mode initialising.");
	}

	public static void loadPMPostProcessor(PhotoModeScreen photoModeScreen, GameRenderer gr, ResourceLocation id) {
		if (gr.currentEffect() != null) {
			gr.currentEffect().close();
		}

		try {
			PhotoModePostEffectProcessor processor = new PhotoModePostEffectProcessor(photoModeScreen, Minecraft.getInstance().getTextureManager(), Minecraft.getInstance().getResourceManager(), Minecraft.getInstance().getMainRenderTarget(), id);
			((AccessGameRenderer) gr).photoMode$setPostChain(processor);
			gr.currentEffect().resize(Minecraft.getInstance().getWindow().getWidth(), Minecraft.getInstance().getWindow().getHeight());
			((AccessGameRenderer) gr).photoMode$setEffectActive(true);
		} catch (IOException ioE) {
			CoralineSystems.LOGGER.warn("Failed to load shader: {}", id, ioE);
			((AccessGameRenderer) gr).photoMode$setEffectActive(false);
		} catch (JsonSyntaxException jSE) {
			CoralineSystems.LOGGER.warn("Failed to parse shader: {}", id, jSE);
			((AccessGameRenderer) gr).photoMode$setEffectActive(false);
		}
	}

}