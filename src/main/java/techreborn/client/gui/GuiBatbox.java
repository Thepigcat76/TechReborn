package techreborn.client.gui;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import reborncore.common.powerSystem.PowerSystem;
import techreborn.client.container.builder.ContainerBuilder;
import techreborn.tiles.storage.TileBatBox;

public class GuiBatbox extends GuiBase {

	TileBatBox tile;

	public GuiBatbox(final EntityPlayer player, final TileBatBox tile) {
		super(player, tile, new ContainerBuilder("batbox").player(player.inventory).inventory().hotbar().addInventory().tile(tile).energySlot(0, 62, 45).energySlot(1, 98, 45).syncEnergyValue().addInventory().create());
		this.tile = tile;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY) {
		super.drawGuiContainerBackgroundLayer(f, mouseX, mouseY);
		Layer layer = Layer.BACKGROUND;

		drawSlot(62, 45, layer);
		drawSlot(98, 45, layer);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		super.drawGuiContainerForegroundLayer(mouseX, mouseY);
		Layer layer = Layer.FOREGROUND;

		builder.drawMultiEnergyBar(this, 81, 28, (int) tile.getEnergy(), (int) tile.getMaxPower(), mouseX, mouseY, 0, layer);
		GlStateManager.scale(0.6, 0.6, 5);
		drawCentredString(PowerSystem.getLocaliszedPowerFormattedNoSuffix((int) (tile.getEnergy())) + "/" + PowerSystem.getLocaliszedPowerFormattedNoSuffix((int) (tile.getMaxPower())) + " " + PowerSystem.getDisplayPower().abbreviation, 35, 0, 58, layer);
		GlStateManager.scale(1, 1, 1);
	}
}
