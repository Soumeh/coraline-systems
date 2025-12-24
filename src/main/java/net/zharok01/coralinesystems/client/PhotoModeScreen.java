package net.zharok01.coralinesystems.client;

import net.zharok01.coralinesystems.CoralineSystems;
import net.zharok01.coralinesystems.mixin.AccessGameRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.Screenshot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class PhotoModeScreen extends Screen {
	public boolean playerVisible = true;
	private float cameraRotation = 0.0f;
	private float cameraZoom = 1.0f;
	private float cameraTilt = 30.0f;
	private float cameraFog = 1.0f;
	private float cameraPanX = 0.0f;
	private float cameraPanY = 0.0f;
	private float lastCameraRotation = 0.0f;
	private float lastCameraZoom = 1.0f;
	private float lastCameraTilt = 30.0f;
	private float lastCameraFog = 1.0f;
	private float lastCameraPanX = 0.0f;
	private float lastCameraPanY = 0.0f;
	private float cameraRotationGoal = 0.0f;
	private float cameraZoomGoal = 1.0f;
	private float cameraTiltGoal = 30.0f;
	private float cameraFogGoal = 1.0f;
	private float cameraPanXGoal = 0.0f;
	private float cameraPanYGoal = 0.0f;
	private float lastPanXEnd;
	private float lastPanYEnd;
	private float lastRotationEnd;
	public float shaderIntensity = 1.0F;
	private long lastGuiUpdateTime = 0L;
	private double initMouseX;
	private double initMouseY;
	private long oldTime;
	private long selectedTime = -1L;
	private long selectedDay = -1L;
	private boolean showInfoText = true;
	private boolean isTakingScreenshot = false;
	private final boolean wasHudHidden = Minecraft.getInstance().options.hideGui;
	private final boolean wasChunkCullingEnabled = Minecraft.getInstance().smartCull;

	private int currentShader = -1;

	Button centerScreen;
	Button showPlayer;
	Button shader;
	PhotoModeSliderWidget tiltSlider;
	PhotoModeSliderWidget timeSlider;
	PhotoModeSliderWidget fogSlider;
	PhotoModeSliderWidget intensitySlider;

	public PhotoModeScreen(Component title) {
		super(title);
	}

	@Override
	protected void init() {
		super.init();
		initWidgets();
		updateGui();

		assert minecraft != null;

		minecraft.options.hideGui = true;
		minecraft.smartCull = false;
		minecraft.gameRenderer.setRenderHand(false);
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
		try {
			assert minecraft != null;
			if (isTakingScreenshot) {
				Screenshot.grab(minecraft.gameDirectory, minecraft.getMainRenderTarget(), text -> {
				});
				isTakingScreenshot = false;
			} else {
				super.render(guiGraphics, mouseX, mouseY, partialTick);
				if (tiltSlider.isDragging) {
					cameraTiltGoal = (float) (int) (tiltSlider.value * 90.0f);
				}

				if (fogSlider.isDragging) {
					cameraFogGoal = (float) Math.pow(2.0, 8.0f * fogSlider.value - 8.0f);
				}

				if (timeSlider.isDragging) {
					long time = (long) (timeSlider.value * 24000.0f);
					selectedTime = timeSlider.value == 0.0f ? oldTime % 24000L : time;
					assert minecraft.level != null;
					minecraft.level.setDayTime(selectedDay + selectedTime);
					minecraft.gameRenderer.tick();
				}

				if (intensitySlider.isDragging) {
					shaderIntensity = (float) intensitySlider.value;
				}

				long currentTime = System.currentTimeMillis();
				if (currentTime > lastGuiUpdateTime + 5L) {
					lastCameraRotation = cameraRotation;
					if (cameraRotation != cameraRotationGoal) {
						cameraRotation += (cameraRotationGoal - cameraRotation) * 0.08F;
						if (Math.abs(cameraRotation - cameraRotationGoal) < 5.0E-4f) {
							cameraRotation = cameraRotationGoal;
						}
					}

					lastCameraTilt = cameraTilt;
					if (cameraTilt != cameraTiltGoal) {
						cameraTilt += (cameraTiltGoal - cameraTilt) * 0.08F;
						if (Math.abs(cameraTilt - cameraTiltGoal) < 0.01f) {
							cameraTilt = cameraTiltGoal;
						}
					}

					lastCameraZoom = cameraZoom;
					if (cameraZoom != cameraZoomGoal) {
						cameraZoom += (cameraZoomGoal - cameraZoom) * 0.08F;
						if (Math.abs(cameraZoom - cameraZoomGoal) < 5.0E-4f) {
							cameraZoom = cameraZoomGoal;
						}
					}

					lastCameraFog = cameraFog;
					if (cameraFog != cameraFogGoal) {
						cameraFog += (cameraFogGoal - cameraFog) * 0.02f;
						if (Math.abs(cameraFog - cameraFogGoal) < 5.0E-5f) {
							cameraFog = cameraFogGoal;
						}
					}

					lastCameraPanX = cameraPanX;
					if (cameraPanX != cameraPanXGoal) {
						cameraPanX += (cameraPanXGoal - cameraPanX) * 0.4F;
						if (Math.abs(cameraPanX - cameraPanXGoal) < 0.01F) {
							cameraPanX = cameraPanXGoal;
						}
					}

					lastCameraPanY = cameraPanY;
					if (cameraPanY != cameraPanYGoal) {
						cameraPanY += (cameraPanYGoal - cameraPanY) * 0.4F;
						if (Math.abs(cameraPanY - cameraPanYGoal) < 0.01F) {
							cameraPanY = cameraPanYGoal;
						}
					}

					lastGuiUpdateTime = currentTime;
				}

				if (showInfoText) {
					guiGraphics.drawCenteredString(font, Component.translatable("gui.photomode.helpText"), width / 2, height - 56, 0xFFFFFF);
				}
			}
			updateGui();
		} catch (Exception exception) {
			CoralineSystems.LOGGER.error("Error while rendering Photo Mode screen", exception);
		}
	}

	public void cycleShader() {
		assert minecraft != null;

		if (minecraft.getCameraEntity() instanceof Player) {
			GameRenderer gr = minecraft.gameRenderer;

			if (gr.currentEffect() != null) {
				gr.currentEffect().close();
			}

			currentShader = (currentShader + 1) % (PhotoModeUtils.SHADER_PROGRAM_COUNT + 1);
			if (hasControlDown() || currentShader == PhotoModeUtils.SHADER_PROGRAM_COUNT) {
				((AccessGameRenderer) gr).photoMode$setPostChain(null);
				currentShader = -1;
			} else {
				PhotoModeUtils.loadPMPostProcessor(this, gr, PhotoModeUtils.SHADER_PROGRAMS[currentShader]);
			}
		}
	}

	private void initWidgets() {
		addRenderableWidget(centerScreen = Button.builder(Component.translatable("gui.photomode.centerCamera"), (button) -> {
			cameraPanXGoal = 0.0F;
			cameraPanYGoal = 0.0F;
			cameraRotationGoal = 0.0F;
		}).pos(width - 150, 0).build());

		addRenderableWidget(showPlayer = Button.builder(Component.translatable("gui.photomode.showPlayer", CommonComponents.OPTION_ON), (button) ->
				playerVisible = !playerVisible).pos(width - 150, 0).build());

		timeSlider = new PhotoModeSliderWidget(width - 150, 0, 150, 20, Component.translatable("gui.photomode.time"), 0.0f);
		fogSlider = new PhotoModeSliderWidget(width - 150, 0, 150, 20, Component.translatable("gui.photomode.fog"), 1.0f);
		tiltSlider = new PhotoModeSliderWidget(width - 150, 0, 150, 20, Component.translatable("gui.photomode.tilt"), 0.33333334f);
		addRenderableWidget(tiltSlider);
		assert minecraft != null;
		if (minecraft.hasSingleplayerServer()) {
			addRenderableWidget(timeSlider);
			addRenderableWidget(fogSlider);
		}

		addRenderableWidget(shader = Button.builder(Component.translatable("gui.photomode.shader", Component.translatable("gui.photomode.none")), (button) -> {
			cycleShader();
			this.intensitySlider.active = minecraft.gameRenderer.currentEffect() != null;
		}).pos(width - 150, 0).build());

		intensitySlider = new PhotoModeSliderWidget(width - 150, 0, 150, 20, Component.translatable("gui.photomode.intensity"), shaderIntensity);
		intensitySlider.active = minecraft.gameRenderer.currentEffect() != null;
		addRenderableWidget(intensitySlider);

		int i = 0;
		for (Object button : children()) {
			((AbstractWidget)button).setPosition(((AbstractWidget) button).getX(), i++ * 21);
		}


		addRenderableWidget(Button.builder(Component.translatable("gui.photomode.takescreenshot"), (button) ->
				isTakingScreenshot = true).pos(width / 2 - 49, height - 20).width(98).build());

		addRenderableWidget(Button.builder(Component.literal("X"), (button) -> {
			onPhotoModeClose();
			minecraft.setScreen(new PauseScreen(true));
		}).pos(0, 0).width(20).build());

		addRenderableWidget(Button.builder(Component.literal("<"), (button) -> {
			cameraRotationGoal++;
			cameraRotationGoal = (int)cameraRotationGoal;
		}).pos(width / 2 - 49 - 2 - 20, height - 20).width(20).build());
		addRenderableWidget(Button.builder(Component.literal(">"), (button) -> {
			cameraRotationGoal--;
			cameraRotationGoal = (int)cameraRotationGoal;
		}).pos(width / 2 + 49 + 2, height - 20).width(20).build());

		assert minecraft.level != null;
		oldTime = minecraft.level.getDayTime();
		if (timeSlider.value != 0.0F) {
			if (selectedTime == -1L) {
				selectedTime = oldTime % 24000L;
			} else {
				timeSlider.value = (float) selectedTime / 24000.0f;
			}
			if (selectedDay == -1L) {
				selectedDay = oldTime / 24000L;
			}
		}
	}

	private void updateGui() {
		timeSlider.setText(Component.translatable("gui.photomode.time", timeSlider.value == 0.0f ? Component.translatable("gui.photomode.default") : (long)(timeSlider.value * 24000.0f)));
		fogSlider.setText(Component.translatable("gui.photomode.fog", (int)(fogSlider.value * 100.0f)));
		tiltSlider.setText(Component.translatable("gui.photomode.tilt", (int)(tiltSlider.value * 90.0f) == 30 ? Component.translatable("gui.photomode.default") : (int)(tiltSlider.value * 90.0f)).append(CommonComponents.SPACE).append((int)(tiltSlider.value * 90.0f) == 30 ? CommonComponents.EMPTY : Component.translatable("gui.photomode.degrees")));
		showPlayer.setMessage(Component.translatable("gui.photomode.showPlayer", CommonComponents.optionStatus(playerVisible)));

		assert minecraft != null;
		PostChain postChain = minecraft.gameRenderer.currentEffect();
		Component shaderName = Component.translatable("gui.photomode.none");
		if (postChain != null) {
			String[] splitPath = postChain.getName().split("/");

			String name = splitPath[splitPath.length - 1];
			name = name.substring(0, name.indexOf("."));
			shaderName = Component.translatable("photomode.shader." + name);
		}

		shader.setMessage(Component.translatable("gui.photomode.shader", shaderName));
		intensitySlider.setText(Component.translatable("gui.photomode.intensity", (int)(intensitySlider.value * 100.0F)));

		centerScreen.active = (cameraPanX != 0.0F || cameraPanY != 0.0F || cameraRotation != 0.0F) && (cameraPanXGoal != 0.0F || cameraPanYGoal != 0.0F || cameraRotationGoal != 0.0F);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
		if (delta < 0) {
			cameraZoomGoal -= 0.25f;
		} else if (delta > 0) {
			cameraZoomGoal += 0.25f;
		}
		return true;
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		if (!super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY) && !showInfoText) {
			if (button == 0) {
				assert minecraft != null;
				float div = (float) Math.pow(2.0, cameraZoom) / (float) minecraft.options.guiScale().get();
				cameraPanXGoal = lastPanXEnd + (float) (mouseX - initMouseX) / div;
				cameraPanYGoal = lastPanYEnd + (float) (mouseY - initMouseY) / div;
			} else {
				cameraRotationGoal = lastRotationEnd + (float) (mouseX - initMouseX) / 128.0F;
			}
		}
		return true;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (!super.mouseClicked(mouseX, mouseY, button)) {
			showInfoText = false;
			initMouseX = mouseX;
			initMouseY = mouseY;
		}
		return true;
	}

	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int button) {
		if (!super.mouseReleased(mouseX, mouseY, button)) {
			lastPanXEnd = cameraPanX;
			lastPanYEnd = cameraPanY;
			lastRotationEnd = cameraRotation;
		}
		return true;
	}

	public float getRotation(float delta) {
		return lastCameraRotation + (cameraRotation - lastCameraRotation) * delta;
	}

	public float getZoom(float delta) {
		return lastCameraZoom + (cameraZoom - lastCameraZoom) * delta;
	}

	public float getTilt(float delta) {
		return lastCameraTilt + (cameraTilt - lastCameraTilt) * delta;
	}

	public float getFog(float delta) {
		return lastCameraFog + (cameraFog - lastCameraFog) * delta;
	}

	public float getPanX(float delta) {
		return lastCameraPanX + (cameraPanX - lastCameraPanX) * delta;
	}

	public float getPanY(float delta) {
		return lastCameraPanY + (cameraPanY - lastCameraPanY) * delta;
	}

	@Override
	public void onClose() {
		super.onClose();
		onPhotoModeClose();
	}

	private void onPhotoModeClose() {
		assert minecraft != null;
		assert minecraft.level != null;

		minecraft.level.setDayTime(oldTime);
		minecraft.options.hideGui = wasHudHidden;
		minecraft.smartCull = wasChunkCullingEnabled;
		minecraft.gameRenderer.shutdownEffect();
		minecraft.gameRenderer.setRenderHand(true);
	}
}