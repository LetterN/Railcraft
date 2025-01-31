package mods.railcraft.world.item.crafting;

import mods.railcraft.world.inventory.RailcraftMenuTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.FurnaceResultSlot;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.world.World;

public class CokeOvenMenu extends Container {

  private final World level;
  private final IIntArray data;
  private final IInventory cokeOvenInventory;
  private static final int INTERNAL_CONTAINER_SLOTS = 2;

  public CokeOvenMenu(int id, PlayerInventory inventory) {
    this(id, inventory, new Inventory(INTERNAL_CONTAINER_SLOTS), new IntArray(3));
  }

  /**
   * The Menu for the Coke Oven.
   */
  public CokeOvenMenu(int containerID, PlayerInventory playerInventory,
      IInventory cokeOvenStorageEntity, IIntArray dataAccess) {
    super(RailcraftMenuTypes.COKE_OVEN.get(), containerID);
    checkContainerSize(cokeOvenStorageEntity, INTERNAL_CONTAINER_SLOTS);
    checkContainerDataCount(dataAccess, 3);
    this.cokeOvenInventory = cokeOvenStorageEntity;
    this.data = dataAccess;
    this.level = playerInventory.player.level;

    // our inventory
    this.addSlot(new Slot(cokeOvenStorageEntity, 0, 16, 43));
    this.addSlot(new FurnaceResultSlot(playerInventory.player, cokeOvenStorageEntity, 1, 62, 43));
    // fluid slot

    // generic player inventory
    for (int i = 0; i < 3; ++i) {
      for (int j = 0; j < 9; ++j) {
        this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
      }
    }

    for (int k = 0; k < 9; ++k) {
      this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
    }

    this.addDataSlots(dataAccess);
  }


  @Override
  public boolean stillValid(PlayerEntity player) {
    return this.cokeOvenInventory.stillValid(player);
  }

  @Override
  public ItemStack quickMoveStack(PlayerEntity playerEntity, int slotID) {
    Slot slot = this.slots.get(slotID);
    if (slot == null || !slot.hasItem()) {
      return ItemStack.EMPTY;
    }

    ItemStack itemstack = slot.getItem();
    ItemStack itemstack1 = itemstack.copy();
    if (slotID == 1) {
      if (!this.moveItemStackTo(itemstack1, 1, (36 + INTERNAL_CONTAINER_SLOTS), true)) {
        return ItemStack.EMPTY;
      }

      slot.onQuickCraft(itemstack1, itemstack);
      slot.onTake(playerEntity, itemstack1);
    }
    if (slotID > (INTERNAL_CONTAINER_SLOTS - 1)) {
      if (this.canSmelt(itemstack1)) {
        if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
          return ItemStack.EMPTY;
        }
      } else if (slotID >= INTERNAL_CONTAINER_SLOTS && slotID < (27 + INTERNAL_CONTAINER_SLOTS)) {
        if (!this.moveItemStackTo(itemstack1,
            (27 + INTERNAL_CONTAINER_SLOTS),
            (36 + INTERNAL_CONTAINER_SLOTS), false)) {
          return ItemStack.EMPTY;
        }
      } else if (slotID >= (27 + INTERNAL_CONTAINER_SLOTS)
          && slotID < (36 + INTERNAL_CONTAINER_SLOTS)
          && !this.moveItemStackTo(itemstack1, 2, (27 + INTERNAL_CONTAINER_SLOTS), false)) {
        return ItemStack.EMPTY;
      }
    } else if (!this.moveItemStackTo(itemstack1, 2, (36 + INTERNAL_CONTAINER_SLOTS), false)) {
      return ItemStack.EMPTY;
    }

    if (itemstack1.isEmpty()) {
      slot.set(ItemStack.EMPTY);
    } else {
      slot.setChanged();
    }

    if (itemstack1.getCount() == itemstack.getCount()) {
      return ItemStack.EMPTY;
    }

    slot.onTake(playerEntity, itemstack1);

    return itemstack;
  }

  protected boolean canSmelt(ItemStack itemStack) {
    return this.level.getRecipeManager()
      .getRecipeFor(RailcraftRecipeTypes.COKE_OVEN_COOKING,
          new Inventory(itemStack), this.level).isPresent();
  }

  /**
   * Get the burn progress in float percent.
   */
  public float getBurnProgress() {
    int reqTime = this.data.get(0);
    int currentTick = this.data.get(1);
    if (reqTime == 0) {
      return 0F;
    }
    return Math.max(Math.min((float)currentTick / (float)reqTime, 1F), 0F);
  }

  public boolean isActualyBurning() {
    return this.data.get(0) != 0;
  }

  public float tankFillProgress() {
    int tankUsed = this.data.get(2);
    return Math.max(Math.min((float)tankUsed / 10000F, 1F), 0F);
  }
}
