package mods.railcraft.gui.widget;

import mods.railcraft.world.inventory.RailcraftMenu;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.FriendlyByteBuf;

/**
 * @author CovertJaguar (https://www.railcraft.info/)
 */
public class Widget {

  public final int x;
  public final int y;
  public final int u;
  public final int v;
  public final int w;
  public final int h;
  public boolean hidden;
  protected RailcraftMenu container;
  private byte widgetId;

  public Widget(int x, int y, int u, int v, int w, int h) {
    this.x = x;
    this.y = y;
    this.u = u;
    this.v = v;
    this.w = w;
    this.h = h;
  }

  public void added(RailcraftMenu container) {
    this.container = container;
    widgetId = (byte) container.getWidgets().indexOf(this);
  }

  public byte getId() {
    return widgetId;
  }

  public boolean requiresSync(ServerPlayer player) {
    return false;
  }

  public void writeToBuf(ServerPlayer player, FriendlyByteBuf out) {}

  public void readFromBuf(FriendlyByteBuf in) {}
}
