package mods.railcraft.util.container;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import net.minecraft.world.item.ItemStack;

/**
 * Primary interface for inventories of all types.
 *
 * Supports treating multiple containers as a single object, enabling one-to-one, many-to-many,
 * many-to-one, and one-to-many interactions between inventories.
 *
 * Created by CovertJaguar on 5/28/2017 for Railcraft.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public interface CompositeContainer extends ContainerManipulator {

  /**
   * Each IInventoryComposite is comprised of a collection of InventoryAdaptor objects.
   *
   * This function provides an iterator for iterating over them.
   *
   * The default implementation assumes this interface is being implemented by an object that can be
   * wrapped by an InventoryAdaptor and will return a singleton iterator for that object.
   *
   * If the implementing object cannot be wrapped by an InventoryAdaptor, it will thrown an
   * exception and must override this function.
   *
   * @see ContainerAdaptor
   */
  Iterator<ContainerAdaptor> adaptors();

  default Iterable<ContainerAdaptor> iterable() {
    return this::adaptors;
  }

  @Override
  default int slotCount() {
    return stream().mapToInt(ContainerAdaptor::slotCount).sum();
  }

  /**
   * Attempts to move a single item from one inventory to another.
   *
   * @param dest the destination inventory
   * @param filter Predicate to match against
   * @return null if nothing was moved, the stack moved otherwise
   */
  @Override
  default ItemStack moveOneItemTo(ContainerManipulator dest, Predicate<ItemStack> filter) {
    return stream().map(src -> src.moveOneItemTo(dest, filter))
        .filter(StackFilter.nonEmpty())
        .findFirst()
        .orElse(ItemStack.EMPTY);
  }

  @Override
  default List<ItemStack> extractItems(int maxAmount, Predicate<ItemStack> filter,
      boolean simulate) {
    int amountNeeded = maxAmount;
    List<ItemStack> stacks = new ArrayList<>();
    for (ContainerAdaptor inv : iterable()) {
      List<ItemStack> tempStacks = inv.extractItems(amountNeeded, filter, simulate);
      amountNeeded -= tempStacks.stream().mapToInt(ItemStack::getCount).sum();
      stacks.addAll(tempStacks);
      if (amountNeeded <= 0)
        return stacks;
    }
    return stacks;
  }

  /**
   * Removes a specified number of items matching the filter, but only if the operation can be
   * completed. If the function returns false, the inventory will not be modified.
   *
   * @param amount the amount of items to remove
   * @param filter the filter to match against
   * @return true if there are enough items that can be removed, false otherwise.
   */
  default boolean removeItems(int amount, ItemStack... filter) {
    return removeItems(amount, StackFilter.anyOf(filter));
  }

  /**
   * Removes a specified number of items matching the filter, but only if the operation can be
   * completed. If the function returns false, the inventory will not be modified.
   *
   * @param amount the amount of items to remove
   * @param filter the filter to match against
   * @return true if there are enough items that can be removed, false otherwise.
   */
  default boolean removeItems(int amount, Predicate<ItemStack> filter) {
    if (ContainerTools.tryRemove(this, amount, filter, true))
      return ContainerTools.tryRemove(this, amount, filter, false);
    return false;
  }

  /**
   * Removed x items in one slot matching the filter.
   */
  @Override
  default ItemStack removeStack(int maxAmount, Predicate<ItemStack> filter, boolean simulate) {
    return stream().map(inv -> inv.removeStack(maxAmount, filter, simulate))
        .filter(StackFilter.nonEmpty())
        .findFirst()
        .orElse(ItemStack.EMPTY);
  }

  /**
   * Places an ItemStack in a destination Inventory. Will attempt to move as much of the stack as
   * possible, returning any remainder.
   *
   * @param stack The ItemStack to put in the inventory.
   * @return Null if itemStack was completely moved, a new itemStack with remaining stackSize if
   *         part or none of the stack was moved.
   */
  @Override
  default ItemStack addStack(ItemStack stack, boolean simulate) {
    for (ContainerAdaptor inv : iterable()) {
      stack = inv.addStack(stack, simulate);
      if (stack.isEmpty())
        return ItemStack.EMPTY;
    }
    return stack;
  }

  default Stream<ContainerAdaptor> stream() {
    return StreamSupport.stream(iterable().spliterator(), false);
  }

  @Override
  default Stream<? extends ContainerSlot> streamSlots() {
    return stream().flatMap(ContainerAdaptor::streamSlots);
  }

  @Override
  default Stream<ItemStack> streamStacks() {
    return stream().flatMap(ContainerAdaptor::streamStacks);
  }
}
