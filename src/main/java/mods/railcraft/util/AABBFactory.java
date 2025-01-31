package mods.railcraft.util;

import javax.annotation.ParametersAreNonnullByDefault;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.IBlockReader;

/**
 * Created by CovertJaguar on 3/9/2016 for Railcraft.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class AABBFactory {

  public static final AxisAlignedBB FULL_BOX = start().box().build();
  public static final double PIXEL = 1.0 / 16.0;

  public double minX;
  public double minY;
  public double minZ;
  public double maxX;
  public double maxY;
  public double maxZ;

  private AABBFactory() {}

  public static AABBFactory start() {
    return new AABBFactory();
  }

  public AxisAlignedBB build() {
    return new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
  }

  public AABBFactory fromAABB(AxisAlignedBB box) {
    return setBounds(box.minX, box.minY, box.minZ, box.maxX, box.maxY, box.maxZ);
  }

  public AABBFactory box() {
    return setBounds(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
  }

  public AABBFactory setBounds(double x1, double y1, double z1, double x2, double y2, double z2) {
    minX = Math.min(x1, x2);
    minY = Math.min(y1, y2);
    minZ = Math.min(z1, z2);
    maxX = Math.max(x1, x2);
    maxY = Math.max(y1, y2);
    maxZ = Math.max(z1, z2);
    return this;
  }

  public AABBFactory setBoundsToPoint(Vector3d vector) {
    minX = vector.x;
    minY = vector.y;
    minZ = vector.z;
    maxX = vector.x;
    maxY = vector.y;
    maxZ = vector.z;
    return this;
  }

  public AABBFactory setBoundsFromBlock(BlockState state, IBlockReader world, BlockPos pos) {
    VoxelShape shape = state.getShape(world, pos);
    setBounds(
        pos.getX() + shape.min(Direction.Axis.X),
        pos.getY() + shape.min(Direction.Axis.Y),
        pos.getZ() + shape.min(Direction.Axis.Z),
        pos.getX() + shape.max(Direction.Axis.X),
        pos.getY() + shape.max(Direction.Axis.Y),
        pos.getZ() + shape.max(Direction.Axis.Z));
    return this;
  }

  public AABBFactory createBoxForTileAt(BlockPos pos) {
    setBounds(
        pos.getX(),
        pos.getY(),
        pos.getZ(),
        pos.getX() + 1,
        pos.getY() + 1,
        pos.getZ() + 1);
    return this;
  }

  public AABBFactory createBoxForTileAt(BlockPos pos, double grow) {
    setBounds(
        pos.getX() - grow,
        pos.getY() - grow,
        pos.getZ() - grow,
        pos.getX() + 1.0 + grow,
        pos.getY() + 1.0 + grow,
        pos.getZ() + 1.0 + grow);
    return this;
  }

  public AABBFactory grow(double distance) {
    minX -= distance;
    minY -= distance;
    minZ -= distance;
    maxX += distance;
    maxY += distance;
    maxZ += distance;
    return this;
  }

  public AABBFactory clampToWorld() {
    // if (minY < 0)
    // minY = 0; // cubic chunks
    return this;
  }

  public AABBFactory expandHorizontally(double distance) {
    minX -= distance;
    minZ -= distance;
    maxX += distance;
    maxZ += distance;
    return this;
  }

  public AABBFactory expandXAxis(double distance) {
    minX -= distance;
    maxX += distance;
    return this;
  }

  public AABBFactory expandYAxis(double distance) {
    minY -= distance;
    maxY += distance;
    return this;
  }

  public AABBFactory expandZAxis(double distance) {
    minZ -= distance;
    maxZ += distance;
    return this;
  }

  public AABBFactory expandAxis(Direction.Axis axis, double distance) {
    switch (axis) {
      case X:
        expandXAxis(distance);
        break;
      case Y:
        expandYAxis(distance);
        break;
      case Z:
        expandZAxis(distance);
        break;
    }
    return this;
  }

  public AABBFactory raiseFloor(double raise) {
    minY += raise;
    return this;
  }

  public AABBFactory raiseCeiling(double raise) {
    maxY += raise;
    return this;
  }

  public AABBFactory raiseCeilingPixel(int raise) {
    maxY += raise * PIXEL;
    return this;
  }

  public AABBFactory shiftY(double shift) {
    minY += shift;
    maxY += shift;
    return this;
  }

  public AABBFactory expandToCoordinate(Vector3d point) {
    return expandToCoordinate(point.x(), point.y(), point.z());
  }

  public AABBFactory expandToCoordinate(BlockPos point) {
    return expandToCoordinate(point.getX(), point.getY(), point.getZ());
  }

  public AABBFactory expandToCoordinate(double x, double y, double z) {
    if (x < minX)
      minX = x;
    else if (x > maxX)
      maxX = x;

    if (y < minY)
      minY = y;
    else if (y > maxY)
      maxY = y;

    if (z < minZ)
      minZ = z;
    else if (z > maxZ)
      maxZ = z;

    return this;
  }

  public AABBFactory offset(Vector3i pos) {
    return offset(pos.getX(), pos.getY(), pos.getZ());
  }

  public AABBFactory offset(double x, double y, double z) {
    minX += x;
    minY += y;
    minZ += z;
    maxX += x;
    maxY += y;
    maxZ += z;
    return this;
  }

  public AABBFactory setMinY(double minY) {
    this.minY = minY;
    return this;
  }

  public AABBFactory setMaxY(double maxY) {
    this.maxY = maxY;
    return this;
  }

  public AABBFactory upTo(double distance) {
    minX -= distance;
    minZ -= distance;
    maxX += distance;
    maxY += distance;
    maxZ += distance;
    return this;
  }

  public AABBFactory growFlat(double distance) {
    minX -= distance;
    minZ -= distance;
    maxX += distance;
    maxZ += distance;
    return this;
  }

  public boolean isUndefined() {
    return minX == maxX
        || minY == maxY
        || minZ == maxZ;
  }

  @Override
  public String toString() {
    return String.format("{%.1f,%.1f,%.1f} - {%.1f,%.1f,%.1f}", minX, minY, minZ, maxX, maxY, maxZ);
  }
}
