package mods.railcraft.world.inventory;

import net.minecraft.tags.FluidTags;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

public class WaterSlot extends RailcraftSlot {

  public WaterSlot(Container iinventory, int slotIndex, int posX, int posY) {
    super(iinventory, slotIndex, posX, posY);
  }

  @SuppressWarnings("deprecation")
  @Override
  public boolean mayPlace(ItemStack stack) {
    return FluidUtil.getFluidContained(stack)
        .map(FluidStack::getFluid)
        .filter(fluid -> fluid.is(FluidTags.WATER))
        .isPresent();
  }
}
