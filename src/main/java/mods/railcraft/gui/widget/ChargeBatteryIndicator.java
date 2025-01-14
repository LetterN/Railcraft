package mods.railcraft.gui.widget;

import java.util.ArrayList;
import java.util.List;

import mods.railcraft.util.HumanReadableNumberFormatter;
import net.minecraft.util.text.ITextProperties;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.energy.IEnergyStorage;

/*
 * @author CovertJaguar (https://www.railcraft.info/)
 */
public class ChargeBatteryIndicator implements Gauge {

  private int charge;
  private final IEnergyStorage battery;

  // alloc 2, charge% and nRF / mnRF CU
  private final List<ITextProperties> tooltip = new ArrayList<>(2);

  public ChargeBatteryIndicator(IEnergyStorage battery) {
    this.battery = battery;
  }

  @Override
  public void refresh() {
    int capacity = this.battery.getEnergyStored();
    int current = Math.min(this.charge, capacity);
    float chargeLevel = capacity <= 0.0F ? 0.0F : (current / capacity) * 100.0F;
    this.tooltip.clear();
    this.tooltip.add(new StringTextComponent(String.format("%.0f%%", chargeLevel)));
    this.tooltip.add(new StringTextComponent(
        HumanReadableNumberFormatter.format(current)
        + " / "
        + HumanReadableNumberFormatter.format(capacity)
        + " CU" // charge unit, railcraft energy unit: not eu
        ));
  }

  @Override
  public List<? extends ITextProperties> getTooltip() {
    return this.tooltip;
  }

  @Override
  public float getMeasurement() {
    int capacity = this.battery.getEnergyStored();
    if (capacity <= 0) {
      return 0.0F;
    }
    return (float)this.charge / (float)capacity;
  }

  @Override
  public float getServerValue() {
    return (float)this.battery.getEnergyStored();
  }

  @Override
  public float getClientValue() {
    return (float)this.battery.getEnergyStored();
  }

  @Override
  public void setClientValue(float value) {
    this.charge = (int)value;
  }
}
