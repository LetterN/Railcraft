package mods.railcraft.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraftforge.common.ToolType;

/**
 * @author CovertJaguar <https://www.railcraft.info/>
 */
public final class HarvestUtil {

  private HarvestUtil() {}

  // public static void setToolClass(Item item, String toolClass, int level) {
  // item.setHarvestLevel(toolClass, level);
  // }

  // public static void setBlockHarvestLevel(String toolClass, int level,
  // IContainerBlock blockContainer) {
  // Block block = blockContainer.block();
  // if (block != null)
  // setBlockHarvestLevel(toolClass, level, block);
  // }

  // public static void setBlockHarvestLevel(String toolClass, int level, Block block) {
  // block.setHarvestLevel(toolClass, level);
  // }

  // public static void setStateHarvestLevel(String toolClassLevel, IContainerState stateContainer)
  // {
  // String[] tokens = toolClassLevel.split(":");
  // if (tokens.length != 2)
  // throw new IllegalArgumentException(
  // "Tool class string must be of the format: <toolClass>:<level>");
  // String toolClass = tokens[0];
  // int level = Integer.parseInt(tokens[1]);
  // setStateHarvestLevel(toolClass, level, stateContainer);
  // }

  // public static void setStateHarvestLevel(String toolClass, int level,
  // IContainerState stateContainer) {
  // IBlockState state = stateContainer.getDefaultState();
  // if (state != null)
  // setStateHarvestLevel(toolClass, level, state);
  // }

  // public static void setStateHarvestLevel(String toolClass, int level,
  // @Nullable BlockState blockState) {
  // if (blockState != null)
  // blockState.getBlock().setHarvestLevel(toolClass, level, blockState);
  // }

  public static int getHarvestLevel(BlockState state, ToolType toolType) {
    Block block = state.getBlock();
    return block.isToolEffective(state, toolType) ? block.getHarvestLevel(state) : -1;
  }

  // public static ItemStack getSilkTouchDrop(BlockState state) {
  // // Block#getSilkTouchDrop
  // return Code.findMethod(Block.class, "func_180643_i", ItemStack.class, BlockState.class)
  // .invoke(state.getBlock(), state).orElse(ItemStack.EMPTY);
  // }
}
