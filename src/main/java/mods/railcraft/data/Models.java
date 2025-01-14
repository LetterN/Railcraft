package mods.railcraft.data;

import java.util.Optional;
import mods.railcraft.Railcraft;
import net.minecraft.data.ModelsUtil;
import net.minecraft.data.StockTextureAliases;
import net.minecraft.util.ResourceLocation;

public class Models {

  public static final ModelsUtil POST_COLUMN =
      create("template_post_full_column", "_full_column", StockTextureAliases.TEXTURE);
  public static final ModelsUtil POST_TOP_COLUMN =
      create("template_post_top_column", "_top_column", StockTextureAliases.TEXTURE);
  public static final ModelsUtil POST_DOUBLE_CONNECTION =
      create("template_post_double_connection", "_double_connection", StockTextureAliases.TEXTURE);
  public static final ModelsUtil POST_PLATFORM =
      create("template_post_platform", "_platform", StockTextureAliases.TEXTURE);
  public static final ModelsUtil POST_SMALL_COLUMN =
      create("template_post_small_column", "_small_column", StockTextureAliases.TEXTURE);
  public static final ModelsUtil POST_SINGLE_CONNECTION =
      create("template_post_single_connection", "_single_connection", StockTextureAliases.TEXTURE);
  public static final ModelsUtil POST_INVENTORY =
      create("post_inventory", "_inventory", StockTextureAliases.TEXTURE);

  public static final ModelsUtil ELEVATOR_TRACK =
      create("template_elevator_track", StockTextureAliases.TEXTURE);

  public static final ModelsUtil FACE_OVERLAY =
      create("face_overlay", StockTextureAliases.TEXTURE);

  public static final ResourceLocation BUFFER_STOP =
      new ResourceLocation(Railcraft.ID, "block/buffer_stop");

  private static ModelsUtil create(String name, StockTextureAliases... textureAliases) {
    return new ModelsUtil(Optional.of(new ResourceLocation(Railcraft.ID, "block/" + name)),
        Optional.empty(), textureAliases);
  }

  private static ModelsUtil create(String name, String suffix,
      StockTextureAliases... textureAliases) {
    return new ModelsUtil(Optional.of(new ResourceLocation(Railcraft.ID, "block/" + name)),
        Optional.of(suffix), textureAliases);
  }
}
