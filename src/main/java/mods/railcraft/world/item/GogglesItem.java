package mods.railcraft.world.item;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

/**
 * @author CovertJaguar <https://www.railcraft.info>
 */
public class GogglesItem extends ArmorItem {

  public GogglesItem(Properties properties) {
    super(RailcraftArmorMaterial.GOGGLES, EquipmentSlotType.HEAD, properties);
  }

  @Override
  public ActionResult<ItemStack> use(World level, PlayerEntity player, Hand hand) {
    ItemStack itemStack = player.getItemInHand(hand);
    if (!level.isClientSide()) {
      incrementAura(itemStack);
      Aura aura = getAura(itemStack);
      player.displayClientMessage(getDescriptionText(aura.getDisplayName()), true);
    }
    return ActionResult.sidedSuccess(itemStack, level.isClientSide());
  }

  @Override
  public void appendHoverText(ItemStack itemStack, @Nullable World level,
      List<ITextComponent> lines, ITooltipFlag adv) {
    lines.add(getDescriptionText(getAura(itemStack).getDisplayName()));
    lines.add(new TranslationTextComponent("goggles.description"));
  }

  public static ITextComponent getDescriptionText(ITextComponent displayName) {
    return new TranslationTextComponent("goggles.aura",
        displayName.copy().withStyle(TextFormatting.DARK_PURPLE));
  }

  public static Aura getAura(ItemStack itemStack) {
    return Optional.ofNullable(itemStack.getTag())
        .filter(tag -> tag.contains("aura", Constants.NBT.TAG_STRING))
        .map(tag -> tag.getString("aura"))
        .flatMap(Aura::getByName)
        .orElse(Aura.NONE);
  }

  public static void incrementAura(ItemStack itemStack) {
    Aura aura = getAura(itemStack).getNext();
    if (aura == Aura.TRACKING) {
      aura.getNext();
    }
    itemStack.getOrCreateTag().putString("aura", aura.getSerializedName());
  }

  public enum Aura implements IStringSerializable {

    NONE("none"),
    TRACKING("tracking"),
    TUNING("tuning"),
    SHUNTING("shunting"),
    SIGNALLING("signalling"),
    SURVEYING("surveying"),
    WORLDSPIKE("worldspike");

    private static final Map<String, Aura> byName =
        Arrays.stream(values()).collect(Collectors.toMap(Aura::getName, Function.identity()));

    private String name;
    private final ITextComponent displayName;

    private Aura(String name) {
      this.name = name;
      this.displayName = new TranslationTextComponent("goggles.aura." + name);
    }

    public ITextComponent getDisplayName() {
      return this.displayName;
    }

    public String getName() {
      return this.name;
    }

    @Override
    public String getSerializedName() {
      return this.name;
    }

    public Aura getNext() {
      return values()[this.ordinal() + 1 % values().length];
    }

    public static Optional<Aura> getByName(String name) {
      return Optional.ofNullable(byName.get(name));
    }
  }
}
