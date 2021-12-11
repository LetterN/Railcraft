package mods.railcraft.world.item.crafting;

import mods.railcraft.gui.widget.FluidGaugeWidget;
import mods.railcraft.world.inventory.RailcraftMenu;
import mods.railcraft.world.inventory.RailcraftMenuTypes;
import mods.railcraft.world.level.block.entity.multiblock.CokeOvenBlockEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.FurnaceResultSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.Level;

public class CokeOvenMenu extends RailcraftMenu {

  private final Level level;
  private final ContainerData data;
  private final CokeOvenBlockEntity cokeOvenInventory;
  private static final int INTERNAL_CONTAINER_SLOTS = 2;
  private final FluidGaugeWidget fluidGauge;

  public CokeOvenMenu(int id, Inventory inventory,
      CokeOvenBlockEntity cokeOvenStorageEntity) {
    this(id, inventory, cokeOvenStorageEntity, new SimpleContainerData(2));
  }

  /**
   * The Menu for the Coke Oven.
   */
  public CokeOvenMenu(int containerID, Inventory playerInventory,
      Container cokeOvenStorageEntity, ContainerData dataAccess) {
    super(RailcraftMenuTypes.COKE_OVEN.get(), containerID, playerInventory);
    checkContainerSize(cokeOvenStorageEntity, INTERNAL_CONTAINER_SLOTS);
    checkContainerDataCount(dataAccess, 2);
    this.cokeOvenInventory = (CokeOvenBlockEntity) cokeOvenStorageEntity;
    this.data = dataAccess;
    this.level = playerInventory.player.level;

    // fluid widget
    this.addWidget(
        this.fluidGauge = new FluidGaugeWidget(this.cokeOvenInventory.getTankManager().get(0),
          90, 22, 176, 0, 48, 47));

    // our inventory
    this.addSlot(new Slot(cokeOvenStorageEntity, 0, 16, 43));
    this.addSlot(new FurnaceResultSlot(playerInventory.player, cokeOvenStorageEntity, 1, 62, 43));
    // fluid slot

    // generic player inventory
    for (int i = 0; i < 3; i++) {
      for (int k = 0; k < 9; k++) {
        this.addSlot(new Slot(playerInventory, k + i * 9 + 9, 8 + k * 18, 84 + i * 18));
      }
    }

    for (int j = 0; j < 9; j++) {
      this.addSlot(new Slot(playerInventory, j, 8 + j * 18, 142));
    }

    this.addDataSlots(dataAccess);
  }

  public FluidGaugeWidget getFluidGauge() {
    return this.fluidGauge;
  }

  @Override
  public boolean stillValid(Player player) {
    return this.cokeOvenInventory.stillValid(player);
  }

  @Override
  public ItemStack quickMoveStack(Player playerEntity, int slotID) {
    Slot slot = this.slots.get(slotID);
    if (slot == null || !slot.hasItem()) {
      return ItemStack.EMPTY;
    }

    ItemStack itemstack = slot.getItem();
    ItemStack itemstack1 = itemstack.copy();
    if (slotID == 1) {
      itemstack1.getItem().onCraftedBy(itemstack1, this.level, playerEntity);
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
          new SimpleContainer(itemStack), this.level).isPresent();
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
