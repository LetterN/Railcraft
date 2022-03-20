package mods.railcraft.world.inventory;

import mods.railcraft.world.level.block.entity.module.BlastFurnaceModule;
import mods.railcraft.world.level.block.entity.multiblock.BlastFurnaceBlockEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

public final class BlastFurnaceMenu extends CrafterMenu {

  private final BlastFurnaceBlockEntity blastFurnace;

  public BlastFurnaceMenu(int id, Inventory inventory, BlastFurnaceBlockEntity blastFurnace) {
    super(RailcraftMenuTypes.BLAST_FURNACE.get(), id, inventory.player, blastFurnace.getBlastFurnaceModule());

    this.blastFurnace = blastFurnace;

    var logic = blastFurnace.getBlastFurnaceModule();
    this.addSlot(new Slot(this.logic, BlastFurnaceModule.SLOT_INPUT, 56, 17));
    this.addSlot(new ItemFilterSlot(item -> logic.getItemBurnTime(item) > 0, this.logic,
        BlastFurnaceModule.SLOT_FUEL, 56, 53));
    this.addSlot(new OutputSlot(this.logic, BlastFurnaceModule.SLOT_OUTPUT, 116, 21));
    this.addSlot(new OutputSlot(this.logic, BlastFurnaceModule.SLOT_SLAG, 116, 53));

    this.addInventorySlots(inventory);

    this.addDataSlot(new SimpleDataSlot(logic::getBurnTime, logic::setBurnTime));
    this.addDataSlot(
        new SimpleDataSlot(logic::getCurrentItemBurnTime, logic::setCurrentItemBurnTime));
  }

  public BlastFurnaceBlockEntity getBlastFurnace() {
    return this.blastFurnace;
  }
}
