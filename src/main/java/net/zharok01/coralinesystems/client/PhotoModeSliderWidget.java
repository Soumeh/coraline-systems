package net.zharok01.coralinesystems.client;

import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;

public class PhotoModeSliderWidget extends AbstractSliderButton {

	public boolean isDragging = false;
	public double value;

	protected PhotoModeSliderWidget(int x, int y, int width, int height, Component text, double value) {
		super(x, y, width, height, text, value);
		this.value = value;
	}

	@Override
	protected void updateMessage() {
	}

	protected void setText(Component text) {
		this.setMessage(text);
	}

	@Override
	protected void applyValue() {
		this.value = super.value;
	}

	@Override
	public void onRelease(double mouseX, double mouseY) {
		this.isDragging = false;
		super.onRelease(mouseX, mouseY);
	}

	@Override
	protected void onDrag(double mouseX, double mouseY, double deltaX, double deltaY) {
		this.isDragging = true;
		super.onDrag(mouseX, mouseY, deltaX, deltaY);
	}

	@Override
	public void onClick(double mouseX, double mouseY) {
		this.isDragging = true;
		super.onClick(mouseX, mouseY);
	}

	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		this.isDragging = true;
		return super.keyPressed(keyCode, scanCode, modifiers);
	}

}