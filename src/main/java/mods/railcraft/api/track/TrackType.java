/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.track;

import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

/**
 * Created by CovertJaguar on 8/10/2016 for Railcraft.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public class TrackType extends ForgeRegistryEntry<TrackType> {

  private final Supplier<? extends AbstractRailBlock> baseBlock;
  private final boolean highSpeed;
  private final boolean electric;
  private final int maxSupportDistance;
  private final EventHandler eventHandler;

  public TrackType(Supplier<? extends AbstractRailBlock> baseBlock,
      boolean highSpeed, boolean electric, int maxSupportDistance, EventHandler eventHandler) {
    this.baseBlock = baseBlock;
    this.highSpeed = highSpeed;
    this.electric = electric;
    this.maxSupportDistance = maxSupportDistance;
    this.eventHandler = eventHandler;
  }

  public AbstractRailBlock getBaseBlock() {
    return this.baseBlock.get();
  }

  public boolean isHighSpeed() {
    return this.highSpeed;
  }

  public boolean isElectric() {
    return this.electric;
  }

  public int getMaxSupportDistance() {
    return this.maxSupportDistance;
  }

  public EventHandler getEventHandler() {
    return this.eventHandler;
  }

  public ItemStack getItemStack() {
    return this.getItemStack(1);
  }

  public ItemStack getItemStack(int qty) {
    return new ItemStack(this.getBaseBlock(), qty);
  }

  public static final class Builder {

    private final Supplier<? extends AbstractRailBlock> baseBlock;
    private boolean highSpeed;
    private boolean electric;
    private int maxSupportDistance;
    private EventHandler eventHandler = new EventHandler() {};

    public Builder(Supplier<? extends AbstractRailBlock> baseBlock) {
      this.baseBlock = baseBlock;
    }

    public Builder setHighSpeed(boolean highSpeed) {
      this.highSpeed = highSpeed;
      return this;
    }

    public Builder setElectric(boolean electric) {
      this.electric = electric;
      return this;
    }

    public Builder setMaxSupportDistance(int maxSupportDistance) {
      this.maxSupportDistance = maxSupportDistance;
      return this;
    }

    public Builder setEventHandler(EventHandler eventHandler) {
      this.eventHandler = eventHandler;
      return this;
    }

    public TrackType build() {
      return new TrackType(this.baseBlock, this.highSpeed,
          this.electric, this.maxSupportDistance, this.eventHandler);
    }
  }

  /**
   * Event handler for tracks
   */
  public interface EventHandler {

    /**
     * Invokes after a minecart has passed over us
     *
     * @param level - the level.
     * @param cart - The {@link AbstractMinecartEntity} that passed us.
     * @param pos - our position.
     */
    default void minecartPass(World level, AbstractMinecartEntity cart, BlockPos pos) {}

    @Nullable
    default RailShape getRailShapeOverride(IBlockReader level, BlockPos pos,
        BlockState blockState, @Nullable AbstractMinecartEntity cart) {
      return null;
    }

    /**
     * Event handler for when a mob collides with us over (this) rail
     *
     * @see mods.railcraft.world.level.block.track.behaivor.CollisionHandler CollisionHandler
     * @param level The world.
     * @param pos Block's position in world
     * @param state The state of the track
     * @param entity Entity colliding with us
     */
    default void entityInside(World level, BlockPos pos, BlockState state, Entity entity) {}

    /**
     * Returns the max speed of the rail at the specified position.
     *
     * @see mods.railcraft.world.level.block.track.behaivor.SpeedController SpeedController
     * @param level The world.
     * @param cart The cart on the rail, may be null.
     * @param pos Block's position in world
     * @return The max speed of the current rail.
     */
    default double getMaxSpeed(World level, @Nullable AbstractMinecartEntity cart, BlockPos pos) {
      return 0.4D;
    }
  }
}
