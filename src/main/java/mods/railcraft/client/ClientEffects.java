package mods.railcraft.client;

import java.util.Random;
import java.util.Set;
import mods.railcraft.api.charge.Charge;
import mods.railcraft.api.signal.TuningAuraHelper;
import mods.railcraft.api.signal.SignalTools;
import mods.railcraft.particle.RailcraftParticles;
import mods.railcraft.season.Seasons;
import mods.railcraft.sounds.RailcraftSoundEvents;
import mods.railcraft.world.item.GogglesItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.ParticleStatus;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

/**
 * @author CovertJaguar <https://www.railcraft.info>
 */
public enum ClientEffects implements TuningAuraHelper, Charge.IZapEffectRenderer {

  INSTANCE;

  public static final short TELEPORT_PARTICLES = 64;
  public static final short TRACKING_DISTANCE = 32 * 32;

  private final Minecraft minecraft = Minecraft.getInstance();
  private final Random rand = new Random();

  private ClientEffects() {
    SignalTools.effectManager = this;
    Charge.internalSetEffects(this);
  }

  public void readTeleport(PacketBuffer data) {
    World world = this.minecraft.level;
    if (world == null) {
      return;
    }

    Vector3d start = new Vector3d(data.readDouble(), data.readDouble(), data.readDouble());
    Vector3d destination = new Vector3d(data.readDouble(), data.readDouble(), data.readDouble());
    for (int i = 0; i < TELEPORT_PARTICLES; i++) {
      double travel = (double) i / ((double) TELEPORT_PARTICLES - 1.0D);
      float vX = (rand.nextFloat() - 0.5F) * 0.2F;
      float vY = (rand.nextFloat() - 0.5F) * 0.2F;
      float vZ = (rand.nextFloat() - 0.5F) * 0.2F;
      double pX = start.x + (destination.x - start.x) * travel + (rand.nextDouble() - 0.5D) * 2.0D;
      double pY = start.y + (destination.y - start.y) * travel + (rand.nextDouble() - 0.5D) * 2.0D;
      double pZ = start.z + (destination.z - start.z) * travel + (rand.nextDouble() - 0.5D) * 2.0D;
      spawnParticle(ParticleTypes.PORTAL, pX, pY, pZ, vX, vY, vZ);
    }
  }

  public void readForceSpawn(PacketBuffer data) {
    if (thinParticles(true)) {
      return;
    }

    // BlockPos pos = data.readBlockPos();
    // int color = data.readInt();
    // int x = pos.getX();
    // int y = pos.getY();
    // int z = pos.getZ();
    // double vx = RANDOM.nextGaussian() * 0.1;
    // double vy = RANDOM.nextDouble() * 0.01;
    // double vz = RANDOM.nextGaussian() * 0.1;
    // Vector3d vel = new Vector3d(0, 0, 0);

    // spawnParticle(new ParticleForceSpawn(world, new Vector3d(x + 0.1, y, z +
    // 0.1), vel, color));
    // spawnParticle(new ParticleForceSpawn(world, new Vector3d(x + 0.9, y, z +
    // 0.1), vel, color));
    // spawnParticle(new ParticleForceSpawn(world, new Vector3d(x + 0.1, y, z +
    // 0.9), vel, color));
    // spawnParticle(new ParticleForceSpawn(world, new Vector3d(x + 0.9, y, z +
    // 0.9), vel, color));
  }

  @Override
  public boolean isTuningAuraActive() {
    return this.isGoggleAuraActive(GogglesItem.Aura.TUNING) || this.isGoggleAuraActive(GogglesItem.Aura.SIGNALLING);
  }

  public boolean isGoggleAuraActive(GogglesItem.Aura aura) {
    ItemStack itemStack = this.minecraft.player.getItemBySlot(EquipmentSlotType.HEAD);
    return itemStack.getItem() instanceof GogglesItem && GogglesItem.getAura(itemStack) == aura;
  }

  private double getRandomParticleOffset() {
    return 0.5 + rand.nextGaussian() * 0.1;
  }

  @Override
  public void spawnTuningAuraParticles(TileEntity start, TileEntity dest) {
    // if (thinParticles(false))
    // return;
    // if (rand.nextInt(2) == 0) {
    // BlockPos pos = start.getPos();
    // double px = pos.getX() + getRandomParticleOffset();
    // double py = pos.getY() + getRandomParticleOffset();
    // double pz = pos.getZ() + getRandomParticleOffset();
    //
    // TESRSignals.ColorProfile colorProfile =
    // TESRSignals.ColorProfile.COORD_RAINBOW;
    // if (isGoggleAuraActive(GoggleAura.SIGNALLING))
    // colorProfile = TESRSignals.ColorProfile.CONTROLLER_ASPECT;
    //
    // int color = colorProfile.getColor(start, start.getPos(), dest.getPos());
    //
    // Particle particle = new ParticleTuningAura(start.getWorld(), new Vector3d(px,
    // py, pz),
    // EffectManager.getEffectSource(start), EffectManager.getEffectSource(dest),
    // color);
    // spawnParticle(particle);
    // }
  }

  public void trailEffect(BlockPos start, TileEntity dest, long colorSeed) {
    // if (thinParticles(false))
    // return;
    // if (mc.player.getDistanceSq(start) > TRACKING_DISTANCE)
    // return;
    // if (rand.nextInt(3) == 0) {
    // double px = start.getX() + 0.5 + rand.nextGaussian() * 0.1;
    // double py = start.getY() + 0.5 + rand.nextGaussian() * 0.1;
    // double pz = start.getZ() + 0.5 + rand.nextGaussian() * 0.1;
    // Particle particle =
    // new ParticleHeatTrail(dest.getWorld(), new Vector3d(px, py, pz), colorSeed,
    // EffectManager.getEffectSource(dest));
    // spawnParticle(particle);
    // }
  }

  public void fireSparkEffect(World world, Vector3d start, Vector3d end) {
    // if (thinParticles(false))
    // return;
    // IEffectSource es = EffectManager.getEffectSource(start);
    // Particle particle = new ParticleFireSpark(world, start, end);
    // spawnParticle(particle);
    // SoundHelper.playSoundClient(world, es.getPos(), SoundEvents.BLOCK_LAVA_POP,
    // SoundCategory.BLOCKS, .2F + rand.nextFloat() * .2F, .9F + rand.nextFloat() *
    // .15F);
  }

  public void readFireSpark(PacketBuffer data) {
    Vector3d start = new Vector3d(data.readDouble(), data.readDouble(), data.readDouble());
    Vector3d destination = new Vector3d(data.readDouble(), data.readDouble(), data.readDouble());
    fireSparkEffect(minecraft.level, start, destination);
  }

  public void chunkLoaderEffect(World world, Object source, Set<ChunkPos> chunks) {
    // if (!isGoggleAuraActive(GoggleAura.WORLDSPIKE))
    // return;
    // IEffectSource es = EffectManager.getEffectSource(source);
    //
    // Vector3d sourcePos = es.getPosF();
    // if (FMLClientHandler.instance().getClient().player.getDistanceSq(sourcePos.x,
    // sourcePos.y,
    // sourcePos.z) > 25600)
    // return;
    //
    // for (ChunkPos chunk : chunks) {
    // int xCorner = chunk.x * 16;
    // int zCorner = chunk.z * 16;
    // double yCorner = sourcePos.y - 8;
    //
    // // System.out.println(xCorner + ", " + zCorner);
    // if (rand.nextInt(3) == 0) {
    // if (thinParticles(false))
    // continue;
    // double xParticle = xCorner + rand.nextFloat() * 16;
    // double yParticle = yCorner + rand.nextFloat() * 16;
    // double zParticle = zCorner + rand.nextFloat() * 16;
    //
    // Particle particle =
    // new ParticleChunkLoader(world, new Vector3d(xParticle, yParticle, zParticle),
    // es);
    // spawnParticle(particle);
    // }
    // }
  }

  /**
   * Special particle for polar express easter egg.
   *
   * @param x
   * @param y
   * @param z
   */
  public void snowEffect(double x, double y, double z) {
    if (thinParticles(true)) {
      return;
    }
    double vx = rand.nextGaussian() * 0.1;
    double vy = rand.nextDouble() * 0.01;
    double vz = rand.nextGaussian() * 0.1;
    spawnParticle(ParticleTypes.ITEM_SNOWBALL, x, y, z, vx, vy, vz);
  }

  /**
   * Creates a steam effect on the wheel-side of the trains. Yvel is between 0.02
   * to 0.03
   *
   * @param x  X Coordinate.
   * @param y  Y Coordinate.
   * @param z  Z Coordinate.
   * @param vx Velocity in the X coordinate
   * @param vy Velocity in the Y coordinate
   */
  public void steamEffect(double x, double y, double z, double vx, double vz) {
    if (thinParticles(true)) {
      return;
    }
    BasicParticleType steam = new BasicParticleType(false) {
      @Override
      public BasicParticleType getType() {
        return RailcraftParticles.STEAM.get();
      }
    };
    // vel Y 0.02-0.03
    spawnParticle(steam, x, y, z, vx, 0.02 + (rand.nextDouble() * 0.01), vz);
  }

  /**
   * Same as <code>steamEffect</code>, but moves the particle faster (as we're
   * venting the pressure tank)
   *
   * @param x  X Coordinate.
   * @param y  Y Coordinate.
   * @param z  Z Coordinate.
   * @param vx Velocity in the X coordinate
   * @param vy Velocity in the Y coordinate
   * @see mods.railcraft.client.ClientEffects#steamEffect() steamEffect
   */
  public void steamJetEffect(double x, double y, double z, double vx, double vz) {
    if (thinParticles(true)) {
      return;
    }
    this.steamEffect(x, y, z, vx * 1.5, vz * 1.5);
  }

  public void chimneyEffect(ClientWorld world, double x, double y, double z, int color) {
    // if (thinParticles(false))
    // return;
    // spawnParticle(new ParticleChimney(world, x, y, z, color));
  }

  /**
   * Effect when the boiler is burning stuff to make steam. Spawns on the train's
   * smokestack
   *
   * @param x
   * @param y
   * @param z
   */
  public void locomotiveEffect(double x, double y, double z) {
    if (thinParticles(false)) {
      return;
    }
    if (Seasons.HALLOWEEN && rand.nextInt(4) == 0) { // 20%?
      BasicParticleType spook = new BasicParticleType(false) {
        @Override
        public BasicParticleType getType() {
          return RailcraftParticles.PUMPKIN.get();
        }
      };
      spawnParticle(spook, x, y, z, 0, 0.02, 0);
      return;
    }
    spawnParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, x, y, z, 0, 0.02, 0); // smog obviously.
  }

  @Override
  public void zapEffectPoint(World world, Vector3d source) {
    if (thinParticles(false)) {
      return;
    }

    BasicParticleType spark = new BasicParticleType(false) {
      @Override
      public BasicParticleType getType() {
        return RailcraftParticles.SPARK.get();
      }
    };

    spawnParticle(spark, source.x, source.y, source.z, rand.nextDouble() - 0.5D, rand.nextDouble() - 0.5D,
        rand.nextDouble() - 0.5D);

    world.playLocalSound(source.x, source.y, source.z, RailcraftSoundEvents.ZAP.get(), SoundCategory.BLOCKS, 0.2F,
        0.75F, false);
  }

  @Override
  public void zapEffectDeath(World world, Vector3d source) {
    if (!world.isClientSide()) {
      return;
    }
    if (thinParticles(false)) {
      return;
    }

    world.playLocalSound(source.x, source.y, source.z, RailcraftSoundEvents.ZAP.get(), SoundCategory.BLOCKS, 3F, 0.75F,
        false);

    BasicParticleType spark = new BasicParticleType(false) {
      @Override
      public BasicParticleType getType() {
        return RailcraftParticles.SPARK.get();
      }
    };

    for (int i = 0; i < 20; i++) {
      spawnParticle(spark, source.x, source.y, source.z, rand.nextDouble() - 0.5D, rand.nextDouble() - 0.5D,
          rand.nextDouble() - 0.5D);
    }
  }

  public void readZapDeath(PacketBuffer data) {
    Vector3d pos = new Vector3d(data.readDouble(), data.readDouble(), data.readDouble());
    zapEffectDeath(minecraft.level, pos);
  }

  @Override
  public void zapEffectSurface(BlockState stateIn, World worldIn, BlockPos pos) {
    if (thinParticles(false)) {
      return;
    }

    worldIn.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), RailcraftSoundEvents.ZAP.get(), SoundCategory.BLOCKS,
        0.1F + rand.nextFloat() * 0.2F, 0.9F + rand.nextFloat() * 0.15F, false);

    BasicParticleType spark = new BasicParticleType(false) {
      @Override
      public BasicParticleType getType() {
        return RailcraftParticles.SPARK.get();
      }
    };

    for (Direction side : Direction.values()) {
      if (!Block.shouldRenderFace(stateIn, worldIn, pos, side)) {
        continue;
      }
      Vector3d normal = Vector3d.atLowerCornerOf(side.getNormal());
      Vector3d variance = new Vector3d((rand.nextGaussian() - 0.5) * 0.2, (rand.nextGaussian() - 0.5) * 0.2,
          (rand.nextGaussian() - 0.5) * 0.2);
      Vector3d vel = normal.add(variance);
      // TODO This should probably use the bounding box or something. Its got to be
      // wrong for
      // tracks
      // atm.
      Vector3d start = new Vector3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5).add(normal.scale(0.5));
      switch (side.getAxis()) {
        case X:
          start = start.add(new Vector3d(0.0, rand.nextDouble() - 0.5, rand.nextDouble() - 0.5));
          break;
        case Y:
          start = start.add(new Vector3d(rand.nextDouble() - 0.5, 0.0, rand.nextDouble() - 0.5));
          break;
        case Z:
          start = start.add(new Vector3d(rand.nextDouble() - 0.5, rand.nextDouble() - 0.5, 0.0));
          break;
        default:
          break;
      }
      spawnParticle(
        spark, start.x(), start.y(), start.z(),
        vel.x(), vel.y(), vel.z());
    }
  }

  public void blockParticle(ClientWorld world, Object source, Vector3d pos, Vector3d velocity,
      BlockState state, boolean blockDust) {
    // ParticleBlockCrack particle = new ParticleBlockCrack(world, pos.x, pos.y, pos.z, velocity.x,
    // velocity.y, velocity.z, state);
    //
    // particle.init(EffectManager.getEffectSource(source).getPos());
    //
    // if (blockDust) {
    // particle.setVelocity(velocity);
    // }
    //
    // spawnParticle(particle);
  }

  public void readBlockParticle(PacketBuffer data) {
    BlockPos block = data.readBlockPos();
    Vector3d pos = new Vector3d(data.readDouble(), data.readDouble(), data.readDouble());
    Vector3d velocity = new Vector3d(data.readDouble(), data.readDouble(), data.readDouble());
    BlockState state = Block.stateById(data.readVarInt());
    boolean blockDust = data.readBoolean();
    blockParticle(minecraft.level, block, pos, velocity, state, blockDust);
  }

  /**
   * Checks if you should NOT spawn a particle
   * @param canDisable Can you disable it based on the user's particle setting?
   * @return true when you should not render it
   */
  private boolean thinParticles(boolean canDisable) {
    ParticleStatus particleSetting = minecraft.options.particles;
    switch (particleSetting) {
      case ALL:
        // "yes"
        return false;
      case DECREASED:
        // if we cannot disable it, get a prob80, else get a prob50
        return !((!canDisable && (rand.nextGaussian() > 0.20)) || (rand.nextGaussian() > 0.5));
      case MINIMAL:
        // if we cannot disable it, get a prob50, else just dont.
        return !(!canDisable && (rand.nextGaussian() > 0.5));
      default:
        return true;
    }
  }

  protected void spawnParticle(IParticleData particle, double pX, double pY, double pZ, double vX, double vY, double vZ) {
    minecraft.particleEngine.add(minecraft.particleEngine.createParticle(particle, pX, pY, pZ, vX, vY, vZ));
  }
}
