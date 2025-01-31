package mods.railcraft.util.inventory;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.function.Predicate;

/**
 * This Interface represents an abstract inventory slot. It provides a unified interface for
 * interfacing with Inventories.
 *
 * @author CovertJaguar <https://www.railcraft.info/>
 */
public interface IInvSlot {

  boolean canPutStackInSlot(ItemStack stack);

  boolean canTakeStackFromSlot();

  default boolean hasStack() {
    return !getStack().isEmpty();
  }

  default boolean containsItem(Item item) {
    ItemStack stack = getStack();
    return !stack.isEmpty() && stack.getItem() == item;
  }

  default boolean matches(Predicate<ItemStack> filter) {
    return filter.test(getStack());
  }

  /**
   * Removes a single item from an inventory slot and returns it in a new stack.
   */
  ItemStack decreaseStack();

  ItemStack removeFromSlot(int amount, boolean simulate);

  /**
   * Add as much of the given ItemStack to the slot as possible.
   *
   * @return the remaining items that were not added
   */
  ItemStack addToSlot(ItemStack stack, boolean simulate);

  /**
   * It is not legal to edit the stack returned from this function.
   */
  ItemStack getStack();

  // void setStack(ItemStack stack);

  int getIndex();

  int maxSlotStackSize();

  default int getMaxStackSize() {
    return Math.min(maxSlotStackSize(), getStack().getMaxStackSize());
  }
}
