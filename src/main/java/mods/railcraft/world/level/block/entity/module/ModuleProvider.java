package mods.railcraft.world.level.block.entity.module;

import java.util.Optional;
import java.util.function.Predicate;
import mods.railcraft.util.container.manipulator.CompositeContainerManipulator;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface ModuleProvider {

  <T extends Module> Optional<T> getModule(Class<T> type);

  /**
   * Sync all modules to the client.
   */
  void syncToClient();

  /**
   * Marks all modules as requiring a save.
   */
  void save();

  boolean stillValid(Player player);

  BlockPos getBlockPos();

  Level getLevel();

  default CompositeContainerManipulator<?> getAdjacentContainers() {
    return CompositeContainerManipulator.findAdjacent(this.getLevel(), this.getBlockPos());
  }

  default CompositeContainerManipulator<?> getAdjacentContainers(Predicate<BlockEntity> filter) {
    return CompositeContainerManipulator.findAdjacent(this.getLevel(), this.getBlockPos(), filter);
  }
}
