package mods.railcraft.world.item;

import java.util.Map;
import com.google.common.collect.MapMaker;
import mods.railcraft.RailcraftConfig;
import mods.railcraft.advancements.criterion.RailcraftCriteriaTriggers;
import mods.railcraft.api.carts.ILinkableCart;
import mods.railcraft.api.item.Crowbar;
import mods.railcraft.season.Season;
import mods.railcraft.world.entity.vehicle.CartTools;
import mods.railcraft.world.entity.vehicle.IDirectionalCart;
import mods.railcraft.world.entity.vehicle.LinkageManagerImpl;
import mods.railcraft.world.entity.vehicle.SeasonalCart;
import mods.railcraft.world.entity.vehicle.TrackRemover;
import mods.railcraft.world.entity.vehicle.Train;
import mods.railcraft.world.entity.vehicle.TunnelBore;
import mods.railcraft.world.item.enchantment.RailcraftEnchantments;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.chat.TranslatableComponent;

/**
 * @author CovertJaguar (https://www.railcraft.info)
 */
public class CrowbarHandler {

  public static final float SMACK_VELOCITY = 0.07f;

  private static final Map<Player, AbstractMinecart> linkMap =
      new MapMaker()
          .weakKeys()
          .weakValues()
          .makeMap();

  public InteractionResult handleInteract(AbstractMinecart cart, Player player,
      InteractionHand hand) {
    ItemStack stack = player.getItemInHand(hand);
    if (stack.isEmpty() || !(stack.getItem() instanceof Crowbar)) {
      return InteractionResult.PASS;
    }

    if (player.level.isClientSide()) {
      return InteractionResult.SUCCESS;
    }

    Crowbar crowbar = (Crowbar) stack.getItem();

    if ((stack.getItem() instanceof SeasonsCrowbarItem) && (cart instanceof SeasonalCart)
        && RailcraftConfig.common.enableSeasons.get()) {
      Season season = SeasonsCrowbarItem.getSeason(stack);
      ((SeasonalCart) cart).setSeason(season);
      RailcraftCriteriaTriggers.SEASON_SET.trigger((ServerPlayer) player, cart, season);
      return InteractionResult.CONSUME;
    }

    if (crowbar.canLink(player, hand, stack, cart)) {
      this.linkCart(player, hand, stack, cart, crowbar);
      return InteractionResult.CONSUME;
    } else if (crowbar.canBoost(player, hand, stack, cart)) {
      this.boostCart(player, hand, stack, cart, crowbar);
      return InteractionResult.CONSUME;
    }

    return InteractionResult.PASS;
  }

  private void linkCart(Player player, InteractionHand hand, ItemStack stack,
      AbstractMinecart cart, Crowbar crowbar) {
    boolean used = false;
    boolean linkable = cart instanceof ILinkableCart;

    if (!linkable || ((ILinkableCart) cart).isLinkable()) {
      AbstractMinecart last = linkMap.remove(player);
      if (last != null && last.isAlive()) {
        LinkageManagerImpl lm = LinkageManagerImpl.INSTANCE;
        if (lm.areLinked(cart, last, false)) {
          lm.breakLink(cart, last);
          used = true;
          player.displayClientMessage(new TranslatableComponent("crowbar.link_broken"), true);
        } else {
          used = lm.createLink(last, cart);
          if (used) {
            if (!player.level.isClientSide()) {
              RailcraftCriteriaTriggers.CART_LINK.trigger((ServerPlayer) player, last, cart);
            }
            player.displayClientMessage(new TranslatableComponent("crowbar.link_created"), true);
          }
        }
        if (!used) {
          player.displayClientMessage(new TranslatableComponent("crowbar.link_failed"), true);
        }
      } else {
        linkMap.put(player, cart);
        player.displayClientMessage(new TranslatableComponent("crowbar.link_started"), true);
      }
    }
    if (used) {
      crowbar.onLink(player, hand, stack, cart);
    }
  }

  private void boostCart(Player player, InteractionHand hand, ItemStack stack,
      AbstractMinecart cart, Crowbar crowbar) {
    player.causeFoodExhaustion(.25F);

    if (player.getVehicle() != null) {
      // NOOP
    } else if (cart instanceof TunnelBore) {
      // NOOP
    } else if (cart instanceof IDirectionalCart) {
      ((IDirectionalCart) cart).reverse();
    } else if (cart instanceof TrackRemover) {
      TrackRemover trackRemover = (TrackRemover) cart;
      trackRemover.setMode(trackRemover.getMode().getNext());
    } else {
      int lvl = EnchantmentHelper.getItemEnchantmentLevel(RailcraftEnchantments.SMACK.get(), stack);
      if (lvl == 0) {
        CartTools.smackCart(cart, player, SMACK_VELOCITY);
      }

      Train.get(cart).ifPresent(train -> {
        float smackVelocity = SMACK_VELOCITY * (float) Math.pow(1.7, lvl);
        smackVelocity /= (float) Math.pow(train.size(), 1D / (1 + lvl));
        for (AbstractMinecart each : train) {
          CartTools.smackCart(cart, each, player, smackVelocity);
        }
      });
    }
    crowbar.onBoost(player, hand, stack, cart);
  }
}
