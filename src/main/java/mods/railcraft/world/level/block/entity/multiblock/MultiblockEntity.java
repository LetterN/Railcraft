package mods.railcraft.world.level.block.entity.multiblock;

import com.google.common.collect.Lists;

import java.util.Collection;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import mods.railcraft.world.level.block.entity.RailcraftTickableBlockEntity;

import net.minecraft.block.BlockState;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class MultiblockEntity<T extends MultiblockEntity<T>>
    extends RailcraftTickableBlockEntity implements INamedContainerProvider {

  private static final Logger logger =
      LogManager.getLogger("Railcraft/MultiblockEntity");

  private boolean formed = false;
  private BlockPos parentPos = BlockPos.ZERO;
  @Nullable
  private BlockPos normal;
  private T entityCache;
  private final MultiblockPattern pattern;

  public MultiblockEntity(TileEntityType<?> tileEntityType, MultiblockPattern pattern) {
    super(tileEntityType);
    this.pattern = pattern;
  }

  /**
   * Try to make this tile (entity) a parent.
   * @return true if ok, false if not.
   */
  public boolean tryToMakeParent(Direction facingDir) {
    if (this.level.isClientSide()) {
      logger.info("TryToMakeParent - Denied, clientside.");
      return false;
    }
    if (this.isParent()) {
      logger.info("TryToMakeParent - Denied, this is already a parent.");
      return false;
    }
    if (facingDir.getStepY() != 0) {
      logger.info("TryToMakeParent - Denied, face is either UP/DOWN.");
      return false;
    }
    logger.info("TryToMakeParent - Trying to create THIS as a parent.");

    // rotate 90 degrees, facing AWAY from the user
    BlockPos normal = new BlockPos(facingDir.getClockWise().getClockWise().getNormal());
    logger.debug("TryToMakeParent - Normal: " + normal.toShortString());
    if (!this.isMultiblockPatternValid(normal)) {
      logger.info("TryToMakeParent - Fail, pattern invalid.");
      return false;
    }

    for (T tileEntity : this.getPatternEntities(normal)) {
      tileEntity.setParent(this.worldPosition);
    }
    this.setParent(BlockPos.ZERO);
    this.normal = normal; // ok you see, setparent delinks and sets default normal to nul
    logger.info("TryToMakeParent - Success.");

    return true;
  }

  /**
   * Handles the pattern detection.
   * @return true if pattern detection is ok, false if not.
   */
  public boolean isMultiblockPatternValid(BlockPos normal) {
    return this.pattern.verifyPattern(this.getBlockPos(), normal, this.getLevel());
  }

  /**
   * Gathers all of the block's tileentities. Required.
   * @return List of TileEntity we gathered
   */
  @SuppressWarnings("unchecked")
  public Collection<T> getPatternEntities(BlockPos normal) {
    Collection<T> teCollection = Lists.newArrayList();
    for (BlockPos pos : this.pattern.getPatternPos(this.getBlockPos(), normal)) {
      @Nullable
      TileEntity te = this.getLevel().getBlockEntity(pos);
      if (te == null) {
        continue; // might be air. multiblocks have air sometimes.
      }
      teCollection.add((T)te);
    }
    return teCollection;
  }

  /**
   * Sets this tilentity's parent, delinking first.
   * @param parentPos - The position of the parent in the world.
   */
  public void setParent(BlockPos parentPos) {
    logger.info(
        "setParent - Setting (" + parentPos.toShortString() + ") as our parentPos.");
    this.delink(); // iirc we should not have linked stuffed before
    this.parentPos = parentPos;
    // we do not precache the ref here, though i can.
    this.formed = true;
    this.setChanged();
  }

  /**
   * Is this a parent.
   * @return TRUE if yes.
   */
  public boolean isParent() {
    if (this.getLevel().isClientSide() || !this.formed) {
      return false;
    }
    return this.parentPos.equals(BlockPos.ZERO);
  }

  /**
   * Revalidate the cached parent TE.
   */
  private T stateWhileRevalidate(Supplier<T> getValidEntity) {
    if (this.entityCache == null || this.entityCache.isRemoved()) {
      this.entityCache = getValidEntity.get();
    }
    return this.entityCache;
  }

  /**
   * Gets the parent of this multiblock, or null if not. Does delinking and cacheing.
   */
  @SuppressWarnings("unchecked")
  @Nullable
  public T getParent() {
    if (!this.formed) {
      if (this.entityCache != null) {
        // this should NOT happen!
        logger.warn("Multiblock has entityCache while it isn't formed.");
        this.entityCache = null;
      }
      return null;
    }
    // us
    if (this.isParent()) {
      return this.stateWhileRevalidate(() -> (T) this);
    }
    // not us
    return this.stateWhileRevalidate(() -> {
      @Nullable
      TileEntity doesItExist = this.getLevel().getBlockEntity(parentPos);

      if (doesItExist == null || !((doesItExist.getTileEntity()) instanceof MultiblockEntity<?>)) {
        logger.info(
            "getParent - Parent does not exist OR not the same type. Type or Null: "
            + ((doesItExist == null) ? "null" : doesItExist.toString()));
        this.delink();
        return null;
      }
      return (T) doesItExist.getTileEntity();
    });
  }

  /**
   * Delinks the parent softref, also makes us "unformed".
   */
  public void delink() {
    logger.info(
        "delink - Delink, last parent pos: (" + this.parentPos.toShortString() + ")");
    // if parent pos == zero then drop item
    this.formed = false;
    this.parentPos = BlockPos.ZERO;
    this.normal = null;
    this.entityCache = null;
    this.setChanged();
  }

  /**
   * Check the link validity.
   * 1. Checks if the parent exists
   * 2. checks if the pattern is right (if parent)
   * If any fails, delinking will happen
   */
  public boolean verifyLink() {
    if (!this.formed) {
      return false;
    }
    logger.info(
        "verifyLink -Veryfing link. "
        + "Parent: (" + this.parentPos.toShortString() + ")"
        + "Us: (" + this.worldPosition.toShortString() + ")");
    if (this.getParent() == null) {
      this.delink();
      logger.info("verifyLink - Parent didnt exist anymore.");
      return false;
    }
    // we do not save normals if we arent the parent.
    if (this.normal == null) {
      return true;
    }

    if (this.getParent() == this && !this.isMultiblockPatternValid(this.normal)) {
      logger.info("verifyLink - Parent is valid (it is us), however pattern broke.");

      for (T tileEntity : this.getPatternEntities(this.normal)) {
        tileEntity.delink();
      }
      return false;
    }
    return true;
  }

  public boolean isFormed() {
    return this.formed;
  }

  @Nullable
  public BlockPos getNormal() {
    return this.normal;
  }

  @Override
  public void setRemoved() {
    if (this.getLevel().isClientSide()) {
      super.setRemoved(); // run basic deltion
      logger.warn("Clientside delink, ignored.");
      return; //do not run deletion clientside.
    }

    logger.warn("Serverside delink.");

    @Nullable
    MultiblockEntity<T> theParent = this.getParent();
    if (theParent == null) {
      logger.warn("Multiblock has no parent, apparently.");
      return;
    }

    for (T tileEntity : theParent.getPatternEntities(theParent.getNormal())) {
      tileEntity.delink();
    }
    super.setRemoved(); // intentional, this MUST RUN LAST.
    return;
  }

  @Override
  protected void load() {
    this.verifyLink();
  }

  @Override
  public void load(BlockState blockState, CompoundNBT data) {
    super.load(blockState, data);

    this.formed = data.getBoolean("formed");
    this.parentPos = NBTUtil.readBlockPos(data.getCompound("parentPos"));

    CompoundNBT norm;
    try {
      norm = data.getCompound("normal");
    } catch (Exception e) {
      return;
    }
    if (norm == null) {
      return;
    }
    this.normal = NBTUtil.readBlockPos(norm);
  }

  @Override
  public CompoundNBT save(CompoundNBT data) {
    super.save(data);
    data.putBoolean("formed", this.formed);
    data.put("parentPos", NBTUtil.writeBlockPos(this.parentPos));
    // do not save the normal when it's null.
    if (this.normal != null) {
      data.put("normal", NBTUtil.writeBlockPos(this.normal));
    }
    return data;
  }
}
