package mods.railcraft.world.level.block.track;

import javax.annotation.Nullable;
import mods.railcraft.Railcraft;
import mods.railcraft.api.track.TrackType;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.track.behaivor.CollisionHandler;
import mods.railcraft.world.level.block.track.behaivor.SpeedController;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

public class TrackTypes {

  public static final DeferredRegister<TrackType> TRACK_TYPES =
      DeferredRegister.create(TrackType.class, Railcraft.ID);

  public static final Lazy<IForgeRegistry<TrackType>> registry =
      Lazy.of(TRACK_TYPES.makeRegistry("track_type", RegistryBuilder::new));

  public static final RegistryObject<TrackType> ABANDONED =
      TRACK_TYPES.register("abandoned",
          () -> new TrackType.Builder(RailcraftBlocks.ABANDONED_TRACK)
              .addSpikeMaulVariant(RailcraftBlocks.ABANDONED_TURNOUT_TRACK)
              .addSpikeMaulVariant(RailcraftBlocks.ABANDONED_WYE_TRACK)
              .addSpikeMaulVariant(RailcraftBlocks.ABANDONED_JUNCTION_TRACK)
              .setEventHandler(SpeedController.ABANDONED)
              .setMaxSupportDistance(2)
              .build());

  public static final RegistryObject<TrackType> ELECTRIC =
      TRACK_TYPES.register("electric",
          () -> new TrackType.Builder(RailcraftBlocks.ELECTRIC_TRACK)
              .addSpikeMaulVariant(RailcraftBlocks.ELECTRIC_TURNOUT_TRACK)
              .addSpikeMaulVariant(RailcraftBlocks.ELECTRIC_WYE_TRACK)
              .addSpikeMaulVariant(RailcraftBlocks.ELECTRIC_JUNCTION_TRACK)
              .setEventHandler(
                  new CompositeHandler(CollisionHandler.ELECTRIC, SpeedController.IRON))
              .setElectric(true)
              .build());

  public static final RegistryObject<TrackType> HIGH_SPEED =
      TRACK_TYPES.register("high_speed",
          () -> new TrackType.Builder(RailcraftBlocks.HIGH_SPEED_TRACK)
              .addSpikeMaulVariant(RailcraftBlocks.HIGH_SPEED_TURNOUT_TRACK)
              .addSpikeMaulVariant(RailcraftBlocks.HIGH_SPEED_WYE_TRACK)
              .addSpikeMaulVariant(RailcraftBlocks.HIGH_SPEED_JUNCTION_TRACK)
              .setEventHandler(SpeedController.HIGH_SPEED)
              .setHighSpeed(true)
              .build());

  public static final RegistryObject<TrackType> HIGH_SPEED_ELECTRIC =
      TRACK_TYPES.register("high_speed_electric",
          () -> new TrackType.Builder(RailcraftBlocks.HIGH_SPEED_ELECTRIC_TRACK)
              .addSpikeMaulVariant(RailcraftBlocks.HIGH_SPEED_ELECTRIC_TURNOUT_TRACK)
              .addSpikeMaulVariant(RailcraftBlocks.HIGH_SPEED_ELECTRIC_WYE_TRACK)
              .addSpikeMaulVariant(RailcraftBlocks.HIGH_SPEED_ELECTRIC_JUNCTION_TRACK)
              .setEventHandler(
                  new CompositeHandler(CollisionHandler.ELECTRIC, SpeedController.HIGH_SPEED))
              .setElectric(true)
              .setHighSpeed(true)
              .build());

  public static final RegistryObject<TrackType> IRON =
      TRACK_TYPES.register("iron",
          () -> new TrackType.Builder(() -> (BaseRailBlock) Blocks.RAIL)
              .addSpikeMaulVariant(RailcraftBlocks.IRON_TURNOUT_TRACK)
              .addSpikeMaulVariant(RailcraftBlocks.IRON_WYE_TRACK)
              .addSpikeMaulVariant(RailcraftBlocks.IRON_JUNCTION_TRACK)
              .setEventHandler(SpeedController.IRON)
              .build());

  public static final RegistryObject<TrackType> REINFORCED =
      TRACK_TYPES.register("reinforced",
          () -> new TrackType.Builder(RailcraftBlocks.REINFORCED_TRACK)
              .addSpikeMaulVariant(RailcraftBlocks.REINFORCED_TURNOUT_TRACK)
              .addSpikeMaulVariant(RailcraftBlocks.REINFORCED_WYE_TRACK)
              .addSpikeMaulVariant(RailcraftBlocks.REINFORCED_JUNCTION_TRACK)
              .setEventHandler(SpeedController.REINFORCED)
              .build());

  public static final RegistryObject<TrackType> STRAP_IRON =
      TRACK_TYPES.register("strap_iron",
          () -> new TrackType.Builder(RailcraftBlocks.STRAP_IRON_TRACK)
              .addSpikeMaulVariant(RailcraftBlocks.STRAP_IRON_TURNOUT_TRACK)
              .addSpikeMaulVariant(RailcraftBlocks.STRAP_IRON_WYE_TRACK)
              .addSpikeMaulVariant(RailcraftBlocks.STRAP_IRON_JUNCTION_TRACK)
              .setEventHandler(SpeedController.STRAP_IRON)
              .build());

  private static class CompositeHandler implements TrackType.EventHandler {

    private final CollisionHandler collisionHandler;
    private final SpeedController speedController;

    public CompositeHandler(CollisionHandler collisionHandler, SpeedController speedController) {
      this.collisionHandler = collisionHandler;
      this.speedController = speedController;
    }

    @Override
    public void minecartPass(Level level, AbstractMinecart cart, BlockPos pos) {
      this.speedController.minecartPass(level, cart, pos);
    }

    @Override
    public void entityInside(Level level, BlockPos pos, BlockState blockState, Entity entity) {
      this.collisionHandler.entityInside(level, pos, blockState, entity);
    }

    @Override
    @Nullable
    public RailShape getRailShapeOverride(BlockGetter level,
        BlockPos pos, BlockState blockState, @Nullable AbstractMinecart cart) {
      return this.speedController.getRailShapeOverride(level, pos, blockState, cart);
    }

    @Override
    public double getMaxSpeed(Level level, @Nullable AbstractMinecart cart, BlockPos pos) {
      return this.speedController.getMaxSpeed(level, cart, pos);
    }
  }
}
