package mods.railcraft.world.level.block.entity;

import java.util.List;
import javax.annotation.Nullable;
import it.unimi.dsi.fastutil.chars.CharList;
import mods.railcraft.world.inventory.CokeOvenMenu;
import mods.railcraft.world.level.block.CokeOvenBricksBlock;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.entity.multiblock.BlockPredicate;
import mods.railcraft.world.level.block.entity.multiblock.MultiblockBlockEntity;
import mods.railcraft.world.level.block.entity.multiblock.MultiblockPattern;
import mods.railcraft.world.module.CokeOvenModule;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;

public class CokeOvenBlockEntity extends MultiblockBlockEntity<CokeOvenBlockEntity, Void> {

  private static final Component MENU_TITLE =
      new TranslatableComponent("container.railcraft.coke_oven");

  private static final MultiblockPattern<Void> PATTERN = Util.make(() -> {
    final var bricks = BlockPredicate.of(RailcraftBlocks.COKE_OVEN_BRICKS);

    final var topAndBottom = List.of(
        CharList.of('B', 'B', 'B'),
        CharList.of('B', 'B', 'B'),
        CharList.of('B', 'B', 'B'));

    return MultiblockPattern.<Void>builder(2, 1, 2)
        .layer(topAndBottom)
        .layer(List.of(
            CharList.of('B', 'W', 'B'),
            CharList.of('W', 'A', 'W'),
            CharList.of('B', 'W', 'B')))
        .layer(topAndBottom)
        .predicate('B', bricks)
        .predicate('W', bricks)
        .predicate('A', BlockPredicate.AIR)
        .build();
  });

  private final CokeOvenModule cokeOvenModule;

  public CokeOvenBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.COKE_OVEN.get(), blockPos, blockState,
        CokeOvenBlockEntity.class, PATTERN);
    this.cokeOvenModule = this.moduleDispatcher.registerModule("coke_oven",
        new CokeOvenModule(this));
  }

  public CokeOvenModule getCokeOvenModule() {
    return this.cokeOvenModule;
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> capability,
      @Nullable Direction direction) {
    return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && this.isFormed()
        ? this.cokeOvenModule.getItemHandler().cast()
        : LazyOptional.empty();
  }

  @Override
  protected boolean isBlockEntity(MultiblockPattern.Element element) {
    return element.marker() == 'B' || element.marker() == 'W';
  }

  @Override
  protected void membershipChanged(@Nullable Membership<CokeOvenBlockEntity> membership) {
    if (membership == null) {
      this.level.setBlock(this.getBlockPos(),
          this.getBlockState()
              .setValue(CokeOvenBricksBlock.WINDOW, false)
              .setValue(CokeOvenBricksBlock.LIT, false),
          Block.UPDATE_ALL);
      Containers.dropContents(this.level, this.getBlockPos(), this.cokeOvenModule);
    } else {
      this.level.setBlock(this.getBlockPos(),
          this.getBlockState().setValue(CokeOvenBricksBlock.WINDOW,
              membership.patternElement().marker() == 'W'),
          Block.UPDATE_ALL);
    }
  }

  @Override
  public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
    return new CokeOvenMenu(id, inventory, this);
  }

  @Override
  public Component getDisplayName() {
    return MENU_TITLE;
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState,
      CokeOvenBlockEntity blockEntity) {

    blockEntity.serverTick();

    blockEntity.moduleDispatcher.serverTick();

    blockEntity.getMembership()
        .map(Membership::master)
        .ifPresent(master -> {
          var lit = master.cokeOvenModule.isProcessing();
          if (lit != blockState.getValue(CokeOvenBricksBlock.LIT)) {
            level.setBlockAndUpdate(blockPos,
                blockState.setValue(CokeOvenBricksBlock.LIT, lit));
          }
        });

    if (blockEntity.isMaster()) {
      blockEntity.cokeOvenModule.serverTick();
    }
  }
}