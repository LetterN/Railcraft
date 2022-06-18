/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.charge;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraftforge.energy.IEnergyStorage;

/**
 * Batteries the heart of the Charge Network.
 *
 * <p>
 * Consumers and 'wires' don't need batteries, but generators and battery blocks do.
 *
 * <p>
 * You don't to have a Tile Entity to provide a battery for the network, serialization and ticking
 * is handled by the network itself.
 *
 * <p>
 * Generators should add their power output directly to its battery object.
 *
 * <p>
 * <b>You shouldn't hold onto battery objects for longer than you need them.</b> The API makes no
 * guarantee that the battery object assigned to a specific coordinate will always be the same
 * object.
 *
 *
 * <p>
 * Such that sometimes:
 * 
 * <pre>
 * {@code
 * IBatteryBlock bat1 = Charge.distribution.network(world).access(pos).getBattery();
 * IBatteryBlock bat2 = Charge.distribution.network(world).access(pos).getBattery();
 * bat1 != bat2
 * }
 * </pre>
 *
 * <p>
 * Created by CovertJaguar on 10/27/2018 for Railcraft.
 *
 * @author CovertJaguar (https://www.railcraft.info)
 */
public interface ChargeStorage extends IEnergyStorage {

  enum State {
    /**
     * Infinite Batteries will supply an infinite amount of power to the network.
     */
    INFINITE("tile.railcraft.battery.state.infinite"),
    /**
     * Source batteries are used in generators and transformers. The charge they hold will be used
     * to charge the rechargeable batteries in the network, but they themselves will not be load
     * balanced.
     */
    SOURCE("tile.railcraft.battery.state.source"),
    /**
     * Rechargeable batteries can be filled and drained indefinitely. The charge network will
     * balance the change level between all the rechargeable batteries in the network.
     *
     * <p>
     * Generators should posses a small rechargeable battery just large enough to hold the
     * generator's max per tick output with a similar draw level and 100% efficiency.
     */
    RECHARGEABLE("tile.railcraft.battery.state.rechargeable"),
    /**
     * Disposable batteries are excluded from the charge network's level balancing. They will be
     * drained after rechargeable batteries.
     */
    DISPOSABLE("tile.railcraft.battery.state.disposable"),
    /**
     * Disabled batteries are ignored by the network. Use for redstone switching or multiblock
     * logic, etc.
     */
    DISABLED("tile.railcraft.battery.state.disabled");

    private State(String locTag) {}
  }

  BlockPos getBlockPos();

  int getMaxDraw();

  default int getPotentialDraw() {
    return Mth.clamp(this.getMaxDraw(), 0, this.getEnergyStored());
  }

  default int getAvailableCharge() {
    return this.getEnergyStored();
  }

  default float getEfficiency() {
    return 1.0F;
  }

  /**
   * True if and only if {@code this.getEnergyStored() >= this.getMaxEnergyStored()}.
   *
   * @return {@code this.getEnergyStored() >= this.getMaxEnergyStored()}
   */
  default boolean isFull() {
    return this.getEnergyStored() >= this.getMaxEnergyStored();
  }

  /**
   * Gets the current state of the battery.
   *
   * @return The battery's state.
   */
  default State getState() {
    return State.RECHARGEABLE;
  }

  /**
   * Sets the current state of the battery.
   *
   * <p>
   * The state of a battery is always under the control of the client, the network will never change
   * it for you.
   *
   * @param state - the battery's new state.
   */
  default void setState(State state) {}

  /**
   * Storage spec.
   * 
   * @param initialState The initial state of the battery.
   * @param capacity The capacity of the battery.
   * @param maxDraw How much charge can be drawn from this battery per tick.
   * @param efficiency How efficient it is to draw from this battery. Generators/Converters should
   *        generally have this set to 1.0. Other types of blocks will vary.
   */
  record Spec(State initialState, int capacity, int maxDraw, float efficiency) {}
}
