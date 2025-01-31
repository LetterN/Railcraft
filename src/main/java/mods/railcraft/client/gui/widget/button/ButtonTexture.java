package mods.railcraft.client.gui.widget.button;

/**
 * @author CovertJaguar <https://www.railcraft.info/>
 */
public enum ButtonTexture implements TexturePosition {

  LARGE_BUTTON(0, 88, 20, 200),
  SMALL_BUTTON(0, 168, 15, 200),
  LOCKED_BUTTON(240, 0, 16, 16),
  UNLOCKED_BUTTON(224, 0, 16, 16),
  LEFT_BUTTON(204, 0, 16, 10),
  RIGHT_BUTTON(214, 0, 16, 10),
  DICE_BUTTON(176, 0, 16, 16);

  private final int x, y, height, width;

  ButtonTexture(int x, int y, int height, int width) {
    this.x = x;
    this.y = y;
    this.height = height;
    this.width = width;
  }

  @Override
  public int getX() {
    return x;
  }

  @Override
  public int getY() {
    return y;
  }

  @Override
  public int getHeight() {
    return height;
  }

  @Override
  public int getWidth() {
    return width;
  }
}
