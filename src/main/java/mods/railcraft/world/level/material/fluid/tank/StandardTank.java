package mods.railcraft.world.level.material.fluid.tank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

/**
 * Our fancy type of tank. Use this preferably over forge's default one
 *
 * @see net.minecraftforge.fluids.capability.templates.FluidTank Forge FluidTank
 * @author CovertJaguar (https://www.railcraft.info)
 */
public class StandardTank extends FluidTank {

  public static final int DEFAULT_COLOR = 0xFFFFFF;

  private int tankIndex;
  private boolean hidden;

  @Nullable
  protected Supplier<FluidStack> filter;

  @Nullable
  private Consumer<StandardTank> updateCallback;

  private boolean disableDrain;
  private boolean disableFill;

  private List<Component> tooltip;

  public StandardTank(int capacity) {
    super(capacity);
  }

  @Override
  public int fill(FluidStack resource, FluidAction action) {
    return this.disableFill ? 0 : super.fill(resource, action);
  }


  @Override
  public FluidStack drain(FluidStack resource, FluidAction action) {
    return this.disableDrain ? FluidStack.EMPTY : super.drain(resource, action);
  }

  @Override
  public FluidStack drain(int maxDrain, FluidAction action) {
    return this.disableDrain ? FluidStack.EMPTY : super.drain(maxDrain, action);
  }

  /**
   * Internal fill function which IGNORES disablefill made by us.
   *
   * @param resource FluidStack representing the Fluid and maximum amount of fluid to be
   * @param action If SIMULATE, fill will only be simulated.
   * @return Amount of resource that was (or would have been, if simulated) filled.
   * @see net.minecraftforge.fluids.capability.templates.FluidTank#fill() Forge FluidTank#fill()
   */
  public int internalFill(FluidStack resource, FluidAction action) {
    return super.fill(resource, action);
  }

  /**
   * Internal drain function which IGNORES disableDrain made by us.
   * 
   * @param resource FluidStack representing the Fluid and maximum amount of fluid to be drained.
   * @param action If SIMULATE, fill will only be simulated.
   * @return FluidStack representing the Fluid and amount that was (or would have been, if
   *         simulated) drained.
   * @see net.minecraftforge.fluids.capability.templates.FluidTank#drain() Forge FluidTank#drain()
   */
  public FluidStack internalDrain(FluidStack resource, FluidAction action) {
    return super.drain(resource, action);
  }

  /**
   * Internal drain function which IGNORES disablefill made by us.
   *
   * @param maxDrain Maximum amount of fluid to drain.
   * @param action If SIMULATE, fill will only be simulated.
   * @return FluidStack representing the Fluid and amount that was (or would have been, if
   *         simulated) drained.
   * @see net.minecraftforge.fluids.capability.templates.FluidTank#drain() Forge FluidTank#drain()
   */
  public FluidStack internalDrain(int maxDrain, FluidAction action) {
    return super.drain(maxDrain, action);
  }

  /**
   * Disables draning of our tank. Blocks drain() from draining.
   *
   * @see mods.railcraft.world.level.material.fluid.tank.StandardTank#drain() Drain Function
   * @see mods.railcraft.world.level.material.fluid.tank.StandardTank#internalDrain() Bypassed Drain
   *      Function
   */
  public StandardTank disableDrain() {
    this.disableDrain = true;
    return this;
  }

  /**
   * Disables filling of our tank.
   *
   * @see mods.railcraft.world.level.material.fluid.tank.StandardTank#fill() Fill Function
   * @see mods.railcraft.world.level.material.fluid.tank.StandardTank#internalFill() Bypassed Fill
   *      Function
   */
  public StandardTank disableFill() {
    this.disableFill = true;
    return this;
  }

  /**
   * Sets the tank's visibility.
   */
  public void setHidden(boolean hidden) {
    this.hidden = hidden;
  }

  public StandardTank setUpdateCallback(@Nullable Consumer<StandardTank> callback) {
    this.updateCallback = callback;
    return this;
  }

  public int getTankIndex() {
    return tankIndex;
  }

  public void setTankIndex(int index) {
    this.tankIndex = index;
  }

  public int getColor() {
    Fluid f = getFluidType();
    if (f == null) {
      return DEFAULT_COLOR;
    }
    return f.getAttributes().getColor(getFluid());
  }

  public boolean isFull() {
    return getFluid().getAmount() == getCapacity();
  }

  public int getRemainingSpace() {
    return getCapacity() - getFluidAmount();
  }

  /**
   * Get the fluid type we currently have.
   *
   * @return Fluid type or <code>Fluids.EMPTY</code> if empty
   */
  public Fluid getFluidType() {
    return getFluid().getFluid();
  }

  @Override
  public void setFluid(@Nullable FluidStack resource) {
    if (!isFluidValid(resource)) {
      return;
    }
    super.setFluid(resource);
    this.onContentsChanged();
  }

  @Override
  protected void onContentsChanged() {
    this.refreshTooltip();
    if (updateCallback != null) {
      updateCallback.accept(this);
    }
  }

  public List<Component> getTooltip() {
    return this.tooltip;
  }

  protected void refreshTooltip() {
    List<Component> tooltip = new ArrayList<>();
    int amount = getFluidAmount();
    FluidStack fluidStack = getFluid();

    if (fluidStack.isEmpty() && this.filter != null) {
      fluidStack = this.filter.get();
    }

    if (!fluidStack.isEmpty()) {
      tooltip.add(this.getFluidNameToolTip(fluidStack));
    }

    tooltip.add(new TextComponent(String.format(Locale.ENGLISH, "%,d / %,d", amount, getCapacity()))
        .withStyle(ChatFormatting.GRAY));

    this.tooltip = Collections.unmodifiableList(tooltip);
  }

  protected Component getFluidNameToolTip(FluidStack fluidStack) {
    Rarity rarity = fluidStack.getFluid().getAttributes().getRarity(fluidStack);
    if (rarity == null) {
      rarity = Rarity.COMMON;
    }
    return fluidStack.getDisplayName().copy().withStyle(rarity.color);
  }

  public boolean isHidden() {
    return hidden;
  }
}
