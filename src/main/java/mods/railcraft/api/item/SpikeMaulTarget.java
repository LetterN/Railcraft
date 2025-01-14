/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.item;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by CovertJaguar on 3/6/2017 for Railcraft.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public interface SpikeMaulTarget {

  /**
   * A list for registering or changing spike maul targets.
   */
  List<SpikeMaulTarget> spikeMaulTargets = new ArrayList<>();

  /**
   * Returns true when the given state is your resulting state.
   *
   * @param blockState - the block state
   * @param level - the level
   * @param blockPos - the position
   * @return {@code true} if it matches, {@code false} otherwise
   */
  boolean matches(BlockState blockState, World level, BlockPos blockPos);

  /**
   * Returns true when you successfully set another state to your resulting state. Return false to
   * revert changes.
   *
   * @param context - the {@link ItemUseContext}
   * @return {@code true} if successful, {@code false} otherwise
   */
  boolean use(ItemUseContext context);

  public class Simple implements SpikeMaulTarget {

    private final Supplier<? extends Block> block;

    public Simple(Supplier<? extends Block> block) {
      this.block = block;
    }

    @Override
    public boolean matches(BlockState blockState, World level, BlockPos blockPos) {
      return blockState.is(this.block.get());
    }

    @Override
    public boolean use(ItemUseContext context) {
      return context.getLevel().setBlockAndUpdate(context.getClickedPos(),
          this.block.get().getStateForPlacement(new BlockItemUseContext(context)));
    }
  }
}
