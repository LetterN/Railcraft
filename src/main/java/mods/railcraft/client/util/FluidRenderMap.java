package mods.railcraft.client.util;

import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenCustomHashMap;
import net.minecraftforge.fluids.FluidStack;

public class FluidRenderMap<V> extends Object2ObjectOpenCustomHashMap<FluidStack, V> {

  private static final long serialVersionUID = -3537113007055850603L;

  public FluidRenderMap() {
    super(FluidHashStrategy.INSTANCE);
  }

  /**
   * Implements equals & hashCode that ignore FluidStack#amount
   */
  public static class FluidHashStrategy implements Hash.Strategy<FluidStack> {

    public static final FluidHashStrategy INSTANCE = new FluidHashStrategy();

    @Override
    public int hashCode(FluidStack stack) {
      if (stack == null || stack.isEmpty()) {
        return 0;
      }
      int code = 1;
      code = 31 * code + stack.getFluid().hashCode();
      if (stack.hasTag()) {
        code = 31 * code + stack.getTag().hashCode();
      }
      return code;
    }

    @Override
    public boolean equals(FluidStack a, FluidStack b) {
      return a == null ? b == null : b != null && a.isFluidEqual(b);
    }
  }
}
