package mods.railcraft.world.level.block.track.behaivor;

import mods.railcraft.api.charge.Charge;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by CovertJaguar on 8/7/2016 for Railcraft.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public enum CollisionHandler {

  NULL,
  ELECTRIC {
    @Override
    public void entityInside(World level, BlockPos pos, BlockState blockState, Entity entity) {
      if (entity instanceof LivingEntity)
        Charge.distribution.network(level).access(pos).zap(entity, Charge.DamageOrigin.TRACK, 2F);
    }
  };

  public void entityInside(World level, BlockPos pos, BlockState blockState, Entity entity) {}
}
