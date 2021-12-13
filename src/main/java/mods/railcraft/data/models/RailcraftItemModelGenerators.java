package mods.railcraft.data.models;

import java.util.function.BiConsumer;
import java.util.function.Supplier;
import com.google.gson.JsonElement;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.world.item.Item;
import net.minecraft.resources.ResourceLocation;

public class RailcraftItemModelGenerators {

  private final BiConsumer<ResourceLocation, Supplier<JsonElement>> output;

  public RailcraftItemModelGenerators(BiConsumer<ResourceLocation, Supplier<JsonElement>> output) {
    this.output = output;
  }

  private void generateFlatItem(Item item, ModelTemplate model) {
    model.create(ModelLocationUtils.getModelLocation(item), TextureMapping.layer0(item),
        this.output);
  }

  public void run() {
    this.generateFlatItem(RailcraftItems.IRON_TUNNEL_BORE_HEAD.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.BRONZE_TUNNEL_BORE_HEAD.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.STEEL_TUNNEL_BORE_HEAD.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.DIAMOND_TUNNEL_BORE_HEAD.get(),
        ModelTemplates.FLAT_ITEM);

    this.generateFlatItem(RailcraftItems.TANK_MINECART.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.TRACK_LAYER.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.TRACK_REMOVER.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.TUNNEL_BORE.get(), ModelTemplates.FLAT_ITEM);

    this.generateFlatItem(RailcraftItems.SIGNAL_BLOCK_SURVEYOR.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.SIGNAL_TUNER.get(), ModelTemplates.FLAT_ITEM);

    this.generateFlatItem(RailcraftItems.WHISTLE_TUNER.get(), ModelTemplates.FLAT_ITEM);

    this.generateFlatItem(RailcraftItems.GOGGLES.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.OVERALLS.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.STEEL_SHEARS.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.STEEL_SWORD.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
    this.generateFlatItem(RailcraftItems.STEEL_SHOVEL.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
    this.generateFlatItem(RailcraftItems.STEEL_PICKAXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
    this.generateFlatItem(RailcraftItems.STEEL_AXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
    this.generateFlatItem(RailcraftItems.STEEL_HOE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
    this.generateFlatItem(RailcraftItems.STEEL_BOOTS.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.STEEL_CHESTPLATE.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.STEEL_LEGGINGS.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.STEEL_HELMET.get(), ModelTemplates.FLAT_ITEM);

    this.generateFlatItem(RailcraftItems.CREOSOTE_BUCKET.get(), ModelTemplates.FLAT_ITEM);

    this.generateFlatItem(RailcraftItems.TICKET.get(), ModelTemplates.FLAT_ITEM);

    this.generateFlatItem(RailcraftItems.CONTROLLER_CIRCUIT.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.RECEIVER_CIRCUIT.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.SIGNAL_CIRCUIT.get(), ModelTemplates.FLAT_ITEM);

    this.generateFlatItem(RailcraftItems.WOODEN_RAIL.get(),
        ModelTemplates.FLAT_HANDHELD_ROD_ITEM);
    this.generateFlatItem(RailcraftItems.STANDARD_RAIL.get(),
        ModelTemplates.FLAT_HANDHELD_ROD_ITEM);
    this.generateFlatItem(RailcraftItems.ADVANCED_RAIL.get(),
        ModelTemplates.FLAT_HANDHELD_ROD_ITEM);
    this.generateFlatItem(RailcraftItems.ELECTRIC_RAIL.get(),
        ModelTemplates.FLAT_HANDHELD_ROD_ITEM);
    this.generateFlatItem(RailcraftItems.HIGH_SPEED_RAIL.get(),
        ModelTemplates.FLAT_HANDHELD_ROD_ITEM);
    this.generateFlatItem(RailcraftItems.REINFORCED_RAIL.get(),
        ModelTemplates.FLAT_HANDHELD_ROD_ITEM);

    this.generateFlatItem(RailcraftItems.WOODEN_RAILBED.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.WOODEN_TIE.get(), ModelTemplates.FLAT_ITEM);

    this.generateFlatItem(RailcraftItems.STONE_RAILBED.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.STONE_TIE.get(), ModelTemplates.FLAT_ITEM);

    this.generateFlatItem(RailcraftItems.BRASS_INGOT.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.BRASS_NUGGET.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.BRONZE_INGOT.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.BRONZE_NUGGET.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.COPPER_INGOT.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.COPPER_NUGGET.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.STEEL_INGOT.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.STEEL_NUGGET.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.TIN_INGOT.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.TIN_NUGGET.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.ZINC_INGOT.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.ZINC_NUGGET.get(), ModelTemplates.FLAT_ITEM);

    this.generateFlatItem(RailcraftItems.COAL_COKE.get(), ModelTemplates.FLAT_ITEM);

    this.generateFlatItem(RailcraftItems.REBAR.get(), ModelTemplates.FLAT_HANDHELD_ROD_ITEM);

    this.generateFlatItem(RailcraftItems.DIAMOND_CROWBAR.get(),
        ModelTemplates.FLAT_HANDHELD_ITEM);
    this.generateFlatItem(RailcraftItems.IRON_CROWBAR.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
    this.generateFlatItem(RailcraftItems.STEEL_CROWBAR.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
    this.generateFlatItem(RailcraftItems.SEASONS_CROWBAR.get(),
        ModelTemplates.FLAT_HANDHELD_ITEM);

    this.generateFlatItem(RailcraftItems.IRON_SPIKE_MAUL.get(),
        ModelTemplates.FLAT_HANDHELD_ITEM);
    this.generateFlatItem(RailcraftItems.STEEL_SPIKE_MAUL.get(),
        ModelTemplates.FLAT_HANDHELD_ITEM);
    this.generateFlatItem(RailcraftItems.DIAMOND_SPIKE_MAUL.get(),
        ModelTemplates.FLAT_HANDHELD_ITEM);

    this.generateFlatItem(RailcraftItems.TRANSITION_TRACK_KIT.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.LOCKING_TRACK_KIT.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.BUFFER_STOP_TRACK_KIT.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.ACTIVATOR_TRACK_KIT.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.BOOSTER_TRACK_KIT.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.CONTROL_TRACK_KIT.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.GATED_TRACK_KIT.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.DETECTOR_TRACK_KIT.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.COUPLER_TRACK_KIT.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.EMBARKING_TRACK_KIT.get(), ModelTemplates.FLAT_ITEM);
    this.generateFlatItem(RailcraftItems.DISEMBARKING_TRACK_KIT.get(), ModelTemplates.FLAT_ITEM);
  }
}
