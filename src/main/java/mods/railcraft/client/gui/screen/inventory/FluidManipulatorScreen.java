package mods.railcraft.client.gui.screen.inventory;

import mods.railcraft.Railcraft;
import mods.railcraft.client.gui.screen.inventory.widget.FluidGaugeWidgetRenderer;
import mods.railcraft.network.NetworkChannel;
import mods.railcraft.network.play.SetFluidManipulatorAttributesMessage;
import mods.railcraft.world.inventory.FluidManipulatorMenu;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class FluidManipulatorScreen extends ManipulatorScreen<FluidManipulatorMenu> {

  private static final ResourceLocation WIDGETS_TEXTURE_LOCATION =
      new ResourceLocation(Railcraft.ID, "textures/gui/container/fluid_manipulator.png");

  public FluidManipulatorScreen(
      FluidManipulatorMenu menu, PlayerInventory playerInventory,
      ITextComponent title) {
    super(menu, playerInventory, title);
    this.registerWidgetRenderer(new FluidGaugeWidgetRenderer(menu.getFluidGauge()));
  }

  @Override
  protected void sendAttributes() {
    NetworkChannel.PLAY.getSimpleChannel().sendToServer(new SetFluidManipulatorAttributesMessage(
        this.menu.getManipulator().getBlockPos(), this.menu.getManipulator().getRedstoneMode()));
  }

  @Override
  public ResourceLocation getWidgetsTexture() {
    return WIDGETS_TEXTURE_LOCATION;
  }
}
