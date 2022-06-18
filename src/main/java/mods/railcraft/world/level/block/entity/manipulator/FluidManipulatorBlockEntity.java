package mods.railcraft.world.level.block.entity.manipulator;

import java.util.Optional;
import javax.annotation.Nullable;
import mods.railcraft.util.container.AdvancedContainer;
import mods.railcraft.util.container.ContainerTools;
import mods.railcraft.world.inventory.FluidManipulatorMenu;
import mods.railcraft.world.level.material.fluid.FluidItemHelper;
import mods.railcraft.world.level.material.fluid.FluidTools;
import mods.railcraft.world.level.material.fluid.TankManager;
import mods.railcraft.world.level.material.fluid.tank.FilteredTank;
import mods.railcraft.world.level.material.fluid.tank.StandardTank;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public abstract class FluidManipulatorBlockEntity extends ManipulatorBlockEntity
    implements WorldlyContainer, MenuProvider {

  protected static final int SLOT_INPUT = 0;
  protected static final int SLOT_PROCESSING = 1;
  protected static final int SLOT_OUTPUT = 2;
  protected static final int[] SLOTS = ContainerTools.buildSlotArray(0, 3);
  protected static final int CAPACITY = FluidTools.BUCKET_VOLUME * 32;

  protected final AdvancedContainer fluidFilterContainer =
      new AdvancedContainer(1).listener((Container) this).phantom();
  protected final TankManager tankManager = new TankManager();
  private final LazyOptional<IFluidHandler> fluidHandler = LazyOptional.of(() -> this.tankManager);
  protected final StandardTank tank = new FilteredTank(CAPACITY);
  private FluidTools.ProcessState processState = FluidTools.ProcessState.RESET;
  private int fluidProcessingTimer;

  protected FluidManipulatorBlockEntity(BlockEntityType<?> type, BlockPos blockPos,
      BlockState blockState) {
    super(type, blockPos, blockState);
    this.setContainerSize(3);
    this.tankManager.add(this.tank);
    this.tank.setValidator(
        fluidStack -> this.getFilterFluid().map(fluidStack::isFluidEqual).orElse(true));
    this.tank.setChangeListener(this::tankChanged);
  }

  protected void tankChanged() {
    this.syncToClient();
    this.setChanged();
  }

  public TankManager getTankManager() {
    return this.tankManager;
  }

  public AdvancedContainer getFluidFilter() {
    return this.fluidFilterContainer;
  }

  public Optional<FluidStack> getFilterFluid() {
    return FluidUtil.getFluidContained(this.fluidFilterContainer.getItem(0));
  }

  public FluidStack getFluidHandled() {
    return this.getFilterFluid().orElseGet(this.tank::getFluid);
  }

  @Nullable
  protected static IFluidHandler getFluidHandler(AbstractMinecart cart, Direction direction) {
    return cart.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, direction)
        .orElse(null);
  }

  public boolean use(Player player, InteractionHand hand) {
    return FluidTools.interactWithFluidHandler(player, hand, this.tank);
  }

  @Override
  public boolean canHandleCart(AbstractMinecart cart) {
    return cart
        .getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY,
            this.getFacing().getOpposite())
        .isPresent()
        && super.canHandleCart(cart);
  }

  @Override
  protected void upkeep() {
    super.upkeep();

    ContainerTools.dropIfInvalid(this.level, this.getBlockPos(), this, SLOT_INPUT);
    ContainerTools.drop(this.level, this.getBlockPos(), this, SLOT_PROCESSING,
        FluidItemHelper::isContainer);
    ContainerTools.drop(this.level, this.getBlockPos(), this, SLOT_OUTPUT,
        FluidItemHelper::isContainer);

    if (this.fluidProcessingTimer++ >= FluidTools.BUCKET_FILL_TIME) {
      this.fluidProcessingTimer = 0;
      this.processState =
          FluidTools.processContainer(this, tank, this.getProcessType(), this.processState);
    }
  }

  protected abstract FluidTools.ProcessType getProcessType();

  @Override
  public boolean canPlaceItem(int slot, ItemStack stack) {
    if (slot == SLOT_INPUT) {
      if (!FluidItemHelper.isContainer(stack)) {
        return false;
      }
      if (FluidItemHelper.isEmptyContainer(stack)) {
        return true;
      }
      return this.getFilterFluid().map(FluidStack::getFluid)
          .map(fluid -> FluidItemHelper.containsFluid(stack, fluid))
          .orElse(true);
    }
    return false;
  }

  @Override
  public int[] getSlotsForFace(Direction side) {
    return SLOTS;
  }

  @Override
  public boolean canPlaceItemThroughFace(int index, ItemStack itemStackIn, Direction direction) {
    return this.canPlaceItem(index, itemStackIn);
  }

  @Override
  public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
    return index == SLOT_OUTPUT;
  }

  @Override
  public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
    return new FluidManipulatorMenu(id, inventory, this);
  }

  @Override
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    tag.putString("processState", this.processState.getSerializedName());
    tag.put("tankManager", this.tankManager.serializeNBT());
    tag.put("invFilter", this.getFluidFilter().createTag());
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);
    this.processState = FluidTools.ProcessState.getByName(tag.getString("processState"))
        .orElse(FluidTools.ProcessState.RESET);
    this.tankManager.deserializeNBT(tag.getList("tankManager", Tag.TAG_COMPOUND));
    this.getFluidFilter().fromTag(tag.getList("invFilter", Tag.TAG_COMPOUND));
  }

  @Override
  public void writeToBuf(FriendlyByteBuf data) {
    super.writeToBuf(data);
    this.tankManager.writePacketData(data);
  }

  @Override
  public void readFromBuf(FriendlyByteBuf data) {
    super.readFromBuf(data);
    this.tankManager.readPacketData(data);
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
    return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY
        ? this.fluidHandler.cast()
        : super.getCapability(capability, facing);
  }
}
