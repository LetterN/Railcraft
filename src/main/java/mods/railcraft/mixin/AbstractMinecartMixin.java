package mods.railcraft.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import mods.railcraft.world.entity.vehicle.MinecartExtension;
import net.minecraft.world.entity.vehicle.AbstractMinecart;

@Mixin(AbstractMinecart.class)
public class AbstractMinecartMixin {

  @Inject(method = "tick", at = @At("HEAD"))
  public void tick(CallbackInfo callbackInfo) {
    MinecartExtension.getOrThrow((AbstractMinecart) (Object) this).tick();
  }
}
