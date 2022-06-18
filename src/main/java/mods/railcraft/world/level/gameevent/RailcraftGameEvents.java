package mods.railcraft.world.level.gameevent;

import mods.railcraft.Railcraft;
import net.minecraft.core.Registry;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class RailcraftGameEvents {

  public static final DeferredRegister<GameEvent> deferredRegister =
      DeferredRegister.create(Registry.GAME_EVENT_REGISTRY, Railcraft.ID);

  public static final RegistryObject<GameEvent> NEIGHBOR_NOTIFY = register("neighbor_notify");

  private static RegistryObject<GameEvent> register(String name) {
    return register(name, GameEvent.DEFAULT_NOTIFICATION_RADIUS);
  }

  private static RegistryObject<GameEvent> register(String name, int notificationRadius) {
    return deferredRegister.register(name, () -> new GameEvent(name, notificationRadius));
  }
}
