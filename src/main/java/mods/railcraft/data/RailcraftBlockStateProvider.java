package mods.railcraft.data;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.block.Block;
import net.minecraft.data.BlockModelWriter;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.data.IFinishedBlockState;
import net.minecraft.data.ModelsResourceUtil;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class RailcraftBlockStateProvider implements IDataProvider {

  private static final Logger logger = LogManager.getLogger();
  private static final Gson gson =
      new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
  private final DataGenerator generator;

  public RailcraftBlockStateProvider(DataGenerator generator) {
    this.generator = generator;
  }

  public void run(DirectoryCache directoryCache) {
    Map<Block, IFinishedBlockState> blockStates = Maps.newHashMap();
    Consumer<IFinishedBlockState> blockStateConsumer = (blockState) -> {
      Block block = blockState.getBlock();
      IFinishedBlockState existingBlockState = blockStates.put(block, blockState);
      if (existingBlockState != null) {
        throw new IllegalStateException("Duplicate blockstate definition for " + block);
      }
    };
    Map<ResourceLocation, Supplier<JsonElement>> models = Maps.newHashMap();
    BiConsumer<ResourceLocation, Supplier<JsonElement>> modelConsumer = (model, json) -> {
      Supplier<JsonElement> existingJson = models.put(model, json);
      if (existingJson != null) {
        throw new IllegalStateException("Duplicate model definition for " + model);
      }
    };

    Set<Item> skippedAutoModels = new HashSet<>();
    new RailcraftBlockModelProvider(blockStateConsumer, modelConsumer, skippedAutoModels::add)
        .run();
    new RailcraftItemModelProvider(modelConsumer).run();


    RailcraftBlocks.BLOCKS.getEntries().forEach((block) -> {
      Item item = Item.BY_BLOCK.get(block.get());
      if (item != null && !skippedAutoModels.contains(item)) {
        ResourceLocation itemModel = ModelsResourceUtil.getModelLocation(item);
        if (!models.containsKey(itemModel)) {
          models.put(itemModel,
              new BlockModelWriter(ModelsResourceUtil.getModelLocation(block.get())));
        }
      }
    });

    Path outputFolder = this.generator.getOutputFolder();

    this.saveCollection(directoryCache, outputFolder, blockStates,
        RailcraftBlockStateProvider::createBlockStatePath);
    this.saveCollection(directoryCache, outputFolder, models,
        RailcraftBlockStateProvider::createModelPath);
  }

  private <T> void saveCollection(DirectoryCache p_240081_1_, Path p_240081_2_,
      Map<T, ? extends Supplier<JsonElement>> p_240081_3_, BiFunction<Path, T, Path> p_240081_4_) {
    p_240081_3_.forEach((p_240088_3_, p_240088_4_) -> {
      Path path = p_240081_4_.apply(p_240081_2_, p_240088_3_);

      try {
        IDataProvider.save(gson, p_240081_1_, p_240088_4_.get(), path);
      } catch (Exception exception) {
        logger.error("Couldn't save {}", path, exception);
      }

    });
  }

  private static Path createBlockStatePath(Path parentDir, Block block) {
    ResourceLocation blockName = ForgeRegistries.BLOCKS.getKey(block);
    return parentDir.resolve("assets/" + blockName.getNamespace() + "/blockstates/"
        + blockName.getPath() + ".json");
  }

  private static Path createModelPath(Path parentDir, ResourceLocation modelLocation) {
    return parentDir.resolve(
        "assets/" + modelLocation.getNamespace() + "/models/" + modelLocation.getPath() + ".json");
  }

  @Override
  public String getName() {
    return "Railcraft Block State Definitions";
  }
}
