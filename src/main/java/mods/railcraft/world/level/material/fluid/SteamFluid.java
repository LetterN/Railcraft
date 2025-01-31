package mods.railcraft.world.level.material.fluid;

import mods.railcraft.Railcraft;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraftforge.fluids.FluidAttributes;

public class SteamFluid extends Fluid {

  @Override
  public Item getBucket() {
    return Items.AIR;
  }

  @Override
  protected boolean canBeReplacedWith(FluidState fluidState, IBlockReader reader,
      BlockPos blockPos, Fluid fluid, Direction direction) {
    return false;
  }

  @Override
  protected Vector3d getFlow(IBlockReader reader, BlockPos blockPos,
      FluidState fluidState) {
    return Vector3d.ZERO;
  }

  @Override
  public int getTickDelay(IWorldReader worldReader) {
    return 0;
  }

  @Override
  protected float getExplosionResistance() {
    return 0;
  }

  @Override
  public float getHeight(FluidState fluidState, IBlockReader blockReader, BlockPos blockPos) {
    return 0;
  }

  @Override
  public float getOwnHeight(FluidState fluidState) {
    return 0;
  }

  @Override
  protected BlockState createLegacyBlock(FluidState fluidState) {
    return Blocks.AIR.defaultBlockState();
  }

  @Override
  public boolean isSource(FluidState fluidState) {
    return true;
  }

  @Override
  public int getAmount(FluidState fluidState) {
    return 0;
  }

  @Override
  public VoxelShape getShape(FluidState fluidState, IBlockReader blockReader,
      BlockPos blockPos) {
    return VoxelShapes.empty();
  }

  @Override
  protected FluidAttributes createAttributes() {
    return FluidAttributes
        .builder(
            new ResourceLocation(Railcraft.ID, "block/steam_still"),
            new ResourceLocation(Railcraft.ID, "block/steam_still"))
        .color(0xFFF5F5F5) // color is now ARGB
        .gaseous()
        .temperature(423) // in kelvin, 150c
        .build(this);
  }
}
