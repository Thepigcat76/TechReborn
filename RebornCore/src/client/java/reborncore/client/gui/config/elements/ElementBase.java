/*
 * This file is part of RebornCore, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2021 TeamReborn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package reborncore.client.gui.config.elements;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.text.Text;
import reborncore.client.gui.GuiBase;
import reborncore.client.gui.GuiSprites;
import reborncore.client.gui.Theme;
import reborncore.client.gui.ThemeManager;

public class ElementBase {
	private final int x;
	private final int y;
	private SpriteIdentifier sprite;
	protected final Theme theme;

	public ElementBase(int x, int y, SpriteIdentifier sprite) {
		this.sprite = sprite;
		this.x = x;
		this.y = y;
		theme = ThemeManager.getTheme();
	}

	protected void setSprite(SpriteIdentifier sprite) {
		this.sprite = sprite;
	}

	public void draw(DrawContext drawContext, GuiBase<?> gui, int mouseX, int mouseY) {
		drawSprite(drawContext, gui, sprite, x, y);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return GuiBase.getSprite(sprite).getContents().getWidth();
	}

	public int getHeight() {
		return GuiBase.getSprite(sprite).getContents().getHeight();
	}

	public boolean onClick(GuiBase<?> gui, double mouseX, double mouseY) {
		return false;
	}

	public int adjustX(GuiBase<?> gui, int x) {
		return gui.getGuiLeft() + x;
	}

	public int adjustY(GuiBase<?> gui, int y) {
		return gui.getGuiTop() + y;
	}

	public boolean isMouseWithinRect(GuiBase<?> gui, double mouseX, double mouseY) {
		return isInRect(gui, getX(), getY(), getWidth(), getHeight(), mouseX, mouseY);
	}

	public static boolean isInRect(GuiBase<?> gui, int x, int y, int xSize, int ySize, double mouseX, double mouseY) {
		return gui.isPointInRect(x + gui.getGuiLeft(), y + gui.getGuiTop(), xSize, ySize, mouseX, mouseY);
	}

	public void drawText(DrawContext drawContext, GuiBase<?> gui, Text text, int x, int y, int color) {
		x = adjustX(gui, x);
		y = adjustY(gui, y);
		drawContext.drawText(gui.getTextRenderer(), text, x, y, color, false);
	}

	public void drawSprite(DrawContext drawContext, GuiBase<?> gui, SpriteIdentifier spriteIdentifier, int x, int y) {
		GuiSprites.drawSprite(drawContext, spriteIdentifier, x + gui.getGuiLeft(), y + gui.getGuiTop());
	}
}
