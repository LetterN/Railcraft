package mods.railcraft.data.advancements;

import java.util.function.Consumer;

import mods.railcraft.Railcraft;
import mods.railcraft.advancements.criterion.MultiBlockFormedTrigger;
import mods.railcraft.advancements.criterion.SpikeMaulUseTrigger;
import mods.railcraft.advancements.criterion.UseTrackKitTrigger;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.TickTrigger;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

public class TrackAdvancements implements Consumer<Consumer<Advancement>> {

  @Override
  public void accept(Consumer<Advancement> consumer) {

    Advancement rcRoot = Advancement.Builder.advancement()
        .display(
            RailcraftItems.REINFORCED_TRACK.get(),
            new TranslationTextComponent("advancements.railcraft.tracks.root.name"),
            new TranslationTextComponent("advancements.railcraft.tracks.root.desc"),
            new ResourceLocation("textures/gui/advancements/backgrounds/stone.png"),
            FrameType.TASK,
            false, false, false)
        .addCriterion("default_unlock", new TickTrigger.Instance(EntityPredicate.AndPredicate.ANY))
        .save(consumer, Railcraft.ID + ":tracks/root");

    Advancement.Builder.advancement()
        .display(
            RailcraftItems.RAW_FIRESTONE.get(),
            new TranslationTextComponent("advancements.railcraft.tracks.firestone.name"),
            new TranslationTextComponent("advancements.railcraft.tracks.firestone.desc"),
            null,
            FrameType.CHALLENGE,
            true, true, false)
        .parent(rcRoot)
        .addCriterion("has_raw_firestone",
            InventoryChangeTrigger.Instance.hasItems(RailcraftItems.RAW_FIRESTONE.get()))
        .save(consumer, Railcraft.ID + ":tracks/firestone");

    // Advancement.Builder.advancement()
    //     .display(
    //         RailcraftItems.MANUAL_ROLLING_MACHINE.get(),
    //         new TranslationTextComponent(
    //             "advancements.railcraft.tracks.blast_furnace.name"),
    //         new TranslationTextComponent(
    //             "advancements.railcraft.tracks.blast_furnace.desc"),
    //         null,
    //         FrameType.TASK,
    //         true, false, false)
    //     .parent(rcRoot)
    //     .addCriterion("has_blast_furnac", MultiBlockFormedTrigger.Instance
    //         .formedMultiBlock(RailcraftBlockEntityTypes.COKE_OVEN.get()))
    //     .save(consumer, Railcraft.ID + ":tracks/blast_furnace");

    Advancement.Builder.advancement()
        .display(
            RailcraftItems.COKE_OVEN_BRICKS.get(),
            new TranslationTextComponent(
                "advancements.railcraft.tracks.coke_oven.name"),
            new TranslationTextComponent(
                "advancements.railcraft.tracks.coke_oven.desc"),
            null,
            FrameType.TASK,
            true, true, false)
        .parent(rcRoot)
        .addCriterion("has_coke_oven", MultiBlockFormedTrigger.Instance
            .formedMultiBlock(RailcraftBlockEntityTypes.COKE_OVEN.get()))
        .save(consumer, Railcraft.ID + ":tracks/coke_oven");

    Advancement rollingTable = Advancement.Builder.advancement()
        .display(
            RailcraftItems.MANUAL_ROLLING_MACHINE.get(),
            new TranslationTextComponent(
                "advancements.railcraft.tracks.manual_rolling_machine.name"),
            new TranslationTextComponent(
                "advancements.railcraft.tracks.manual_rolling_machine.desc"),
            null,
            FrameType.GOAL,
            true, false, false)
        .parent(rcRoot)
        .addCriterion("has_rolling_table",
            InventoryChangeTrigger.Instance.hasItems(RailcraftItems.MANUAL_ROLLING_MACHINE.get()))
        .save(consumer, Railcraft.ID + ":tracks/manual_rolling_machine");

    // Advancement.Builder.advancement()
    //     .display(
    //         RailcraftItems.MANUAL_ROLLING_MACHINE.get(),
    //         new TranslationTextComponent(
    //             "advancements.railcraft.tracks.rock_crusher.name"),
    //         new TranslationTextComponent(
    //             "advancements.railcraft.tracks.rock_crusher.desc"),
    //         null,
    //         FrameType.GOAL,
    //         true, false, false)
    //     .parent(rcRoot)
    //     .addCriterion("has_rolling_table",
    //         InventoryChangeTrigger.Instance.hasItems(RailcraftItems.MANUAL_ROLLING_MACHINE.get()))
    //     .save(consumer, Railcraft.ID + ":tracks/rock_crusher");

    Advancement basicTrack = Advancement.Builder.advancement()
        .display(
            Items.RAIL,
            new TranslationTextComponent("advancements.railcraft.tracks.regular_track.name"),
            new TranslationTextComponent("advancements.railcraft.tracks.regular_track.desc"),
            null,
            FrameType.TASK,
            true, false, false)
        .parent(rollingTable)
        .addCriterion("has_regular",
            InventoryChangeTrigger.Instance.hasItems(Items.RAIL))
        .save(consumer, Railcraft.ID + ":tracks/regular_track");

    Advancement.Builder.advancement()
        .display(
            RailcraftItems.RAW_FIRESTONE.get(),
            new TranslationTextComponent("advancements.railcraft.tracks.track_kit.name"),
            new TranslationTextComponent("advancements.railcraft.tracks.track_kit.desc"),
            null,
            FrameType.GOAL,
            true, true, false)
        .parent(basicTrack)
        .addCriterion("has_raw_firestone", UseTrackKitTrigger.Instance.hasUsedTrackKit())
        .save(consumer, Railcraft.ID + ":tracks/track_kit");

    Advancement.Builder.advancement()
        .display(
            RailcraftItems.IRON_SPIKE_MAUL.get(),
            new TranslationTextComponent("advancements.railcraft.tracks.junctions.name"),
            new TranslationTextComponent("advancements.railcraft.tracks.junctions.desc"),
            null,
            FrameType.GOAL,
            true, false, false)
        .parent(basicTrack)
        .addCriterion("has_used_spikemaul", SpikeMaulUseTrigger.Instance.hasUsedSpikeMaul())
        .save(consumer, Railcraft.ID + ":tracks/junctions");

    Advancement.Builder.advancement()
        .display(
            RailcraftItems.HIGH_SPEED_TRACK.get(),
            new TranslationTextComponent("advancements.railcraft.tracks.high_speed_track.name"),
            new TranslationTextComponent("advancements.railcraft.tracks.high_speed_track.desc"),
            null,
            FrameType.CHALLENGE,
            true, true, false)
        .parent(rollingTable)
        .addCriterion("has_hs_track",
            InventoryChangeTrigger.Instance.hasItems(RailcraftItems.HIGH_SPEED_TRACK.get()))
        .save(consumer, Railcraft.ID + ":tracks/high_speed_track");

    Advancement.Builder.advancement()
        .display(
            RailcraftItems.STRAP_IRON_TRACK.get(),
            new TranslationTextComponent(
                "advancements.railcraft.tracks.wooden_track.name"),
            new TranslationTextComponent(
                "advancements.railcraft.tracks.wooden_track.desc"),
            null,
            FrameType.TASK,
            true, false, false)
        .parent(rollingTable)
        .addCriterion("has_strap_iron",
            InventoryChangeTrigger.Instance.hasItems(RailcraftItems.STRAP_IRON_TRACK.get()))
        .save(consumer, Railcraft.ID + ":tracks/wooden_track");

  }

}
