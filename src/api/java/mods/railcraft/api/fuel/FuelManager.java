/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.fuel;

import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * @author CovertJaguar <https://www.railcraft.info>
 */
public interface FuelManager {

  /**
   * Call this from {@link FMLCommonSetupEvent}.
   */
  void addFuel(Fluid fluid, int heatValuePerBucket);

  float getFuelValue(Fluid fluid);

  default float getFuelValueForSize(FluidStack fluid) {
    return this.getFuelValue(fluid.getFluid()) * fluid.getAmount() / 1000.0F;
  }
}
