package mods.railcraft.client.gui.screen.inventory;

import com.mojang.blaze3d.vertex.PoseStack;
import mods.railcraft.Railcraft;
import mods.railcraft.world.inventory.BlastFurnaceMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class BlastFurnaceScreen extends RailcraftMenuScreen<BlastFurnaceMenu> {

  private static final ResourceLocation WIDGETS_TEXTURE =
      new ResourceLocation(Railcraft.ID, "textures/gui/container/blast_furnace.png");

  public BlastFurnaceScreen(BlastFurnaceMenu menu, Inventory inventory, Component title) {
    super(menu, inventory, title);
  }

  @Override
  protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
    super.renderBg(poseStack, partialTicks, mouseX, mouseY);
    int x = (this.width - this.imageWidth) / 2;
    int y = (this.height - this.imageHeight) / 2;

    var logic = this.menu.getBlastFurnace().getLogic();
    if (logic.isBurning()) {
      int burnProgressScale = logic.getBurnProgressScaled(12);
      this.blit(poseStack, x + 56, y + 36 + 12 - burnProgressScale, 176, 12 - burnProgressScale, 14,
          burnProgressScale + 2);
    }

    int progressScale = (int) (logic.getProgressPercent() * 24);
    this.blit(poseStack, x + 79, y + 34, 176, 14, progressScale + 1, 16);
  }

  @Override
  public ResourceLocation getWidgetsTexture() {
    return WIDGETS_TEXTURE;
  }
}
