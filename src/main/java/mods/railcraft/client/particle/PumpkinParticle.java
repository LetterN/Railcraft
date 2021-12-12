package mods.railcraft.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

/**
 * Created by CovertJaguar on 7/31/2016 for Railcraft.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public class PumpkinParticle extends SteamParticle {

  public PumpkinParticle(ClientLevel level, double x, double y, double z, double dx, double dy,
      double dz) {
    super(level, x, y, z, dx, dy, dz);
    this.gravity = -0.01F;
    this.lifetime = (int) (16.0D / (this.random.nextGaussian() * 0.8D + 0.2D));
  }

  @Override
  public float getQuadSize(float partialTicks) {
    return this.quadSize * Mth.sin(
        Mth.clamp((this.age + partialTicks) / this.lifetime, 0.0F, 1.0F) * (float) Math.PI);
  }

  public static class Provider implements ParticleProvider<SimpleParticleType> {

    private final SpriteSet spriteSet;

    public Provider(SpriteSet spriteSet) {
      this.spriteSet = spriteSet;
    }

    @Override
    public Particle createParticle(SimpleParticleType type, ClientLevel level,
        double x, double y, double z, double dx, double dy, double dz) {
      PumpkinParticle steam = new PumpkinParticle(level, x, y, z, dx, dy, dz);
      steam.pickSprite(this.spriteSet);
      return steam;
    }
  }
}
