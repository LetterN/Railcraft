package mods.railcraft.util.inventory;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.function.Predicate;

/**
 * This interface extends IInvSlot by allowing you to modify a slot directly. This is only valid on
 * inventories backed by IInventory.
 * <p/>
 * <p/>
 * Created by CovertJaguar on 3/16/2016 for Railcraft.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public interface IExtInvSlot extends IInvSlot {

  /**
   * Sets the current ItemStack in the slot.
   */
  void setStack(ItemStack stack);

  default ItemStack clear() {
    ItemStack stack = getStack();
    setStack(ItemStack.EMPTY);
    return stack;
  }

  default void validate(World world, BlockPos pos) {
    this.validate(world, pos, this::canPutStackInSlot);
  }

  default void validate(World world, BlockPos pos, Predicate<ItemStack> filter) {
    ItemStack stack = getStack();
    if (!stack.isEmpty() && !filter.test(stack)) {
      clear();
      InvTools.dropItem(stack, world, pos);
    }
  }
}
