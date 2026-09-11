package dev.kurai.uhc.menu.spectator;

import com.google.common.collect.Lists;
import dev.kurai.uhc.menu.template.BorderTemplate;
import dev.kurai.uhc.menu.template.PaginationTemplate;
import dev.kurai.uhc.profile.Profile;
import dev.kurai.uhc.profile.ProfileService;
import dev.kurai.uhc.profile.component.ProfileMiningComponent;
import dev.kurai.uhc.util.CC;
import dev.kurai.uhc.util.ItemBuilder;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import net.j4c0b3y.api.menu.MenuSize;
import net.j4c0b3y.api.menu.button.Button;
import net.j4c0b3y.api.menu.button.ButtonClick;
import net.j4c0b3y.api.menu.layer.impl.BackgroundLayer;
import net.j4c0b3y.api.menu.layer.impl.ForegroundLayer;
import net.j4c0b3y.api.menu.pagination.PaginatedMenu;
import net.j4c0b3y.api.menu.pagination.PaginationSlot;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;

public final class TopLuckMenu extends PaginatedMenu {

  private final ProfileService profileService;
  private Sorting sorting;

  public TopLuckMenu(
      final Player player, final ProfileService profileService, final Sorting sorting) {
    super("Top Luck", MenuSize.FIVE, player);
    this.profileService = profileService;
    this.sorting = sorting;
  }

  @Contract(pure = true)
  @Override
  public @Unmodifiable List<Button> getEntries() {
    final var buttons = Lists.<Button>newArrayList();

    for (final var profile :
        this.profileService.getPlayingProfiles().stream()
            .sorted(this.sorting.comparator)
            .toList()) {
      buttons.add(new PlayerButton(this.profileService, profile));
    }

    return buttons;
  }

  @Override
  public void setup(final BackgroundLayer backgroundLayer, final ForegroundLayer front) {
    this.apply(new BorderTemplate(DyeColor.YELLOW.getData()));
    this.apply(new PaginationTemplate());

    front.center(new PaginationSlot(this));
    front.set(4, new SortingButton(this));
  }

  public enum Sorting {
    GOLD(
        "Or",
        (o1, o2) ->
            o1.getComponent(ProfileMiningComponent.class).getGoldMined()
                    < o2.getComponent(ProfileMiningComponent.class).getGoldMined()
                ? 1
                : o1.equals(o2) ? 0 : -1),
    DIAMOND(
        "Diamant",
        (o1, o2) ->
            o1.getComponent(ProfileMiningComponent.class).getDiamondMined()
                    < o2.getComponent(ProfileMiningComponent.class).getDiamondMined()
                ? 1
                : o1.equals(o2) ? 0 : -1),
    ;

    private final String display;
    private final Comparator<Profile> comparator;

    Sorting(final String display, final Comparator<Profile> comparator) {
      this.display = display;
      this.comparator = comparator;
    }

    public String getDisplay() {
      return this.display;
    }

    public Comparator<Profile> getComparator() {
      return this.comparator;
    }
  }

  private static final class SortingButton extends Button {

    private final TopLuckMenu menu;

    private SortingButton(final TopLuckMenu menu) {
      this.menu = menu;
    }

    @Override
    public ItemStack getIcon() {
      return new ItemBuilder(Material.HOPPER)
          .name("&e&lFiltre")
          .lore(
              "",
              "&e " + CC.SQUARE + "&f Filtre: &e" + this.menu.sorting.getDisplay(),
              "",
              "&7" + CC.BAR + "&f Permet de modifier",
              "  le&e filtre&f de tri.",
              "")
          .asItemStack();
    }

    @Override
    public void onClick(final ButtonClick click) {
      this.menu.sorting = this.menu.sorting == Sorting.GOLD ? Sorting.DIAMOND : Sorting.GOLD;
      this.menu.update();

      final Player player = click.getMenu().getPlayer();
      player.playSound(player.getLocation(), Sound.WOOD_CLICK, 1, 1);
    }
  }

  private static final class PlayerButton extends Button {

    private final ProfileService profileService;
    private final Profile profile;

    private PlayerButton(final ProfileService profileService, final Profile profile) {
      this.profileService = profileService;
      this.profile = profile;
    }

    @Override
    public ItemStack getIcon() {
      final var profileData = this.profile.getComponent(ProfileMiningComponent.class);
      final var goldDifference = profileData.getGoldMined() - this.getAverageGold();
      final var diamondDifference = profileData.getDiamondMined() - this.getAverageDiamond();

      final var averageOres =
          this.getAverageGold() + this.getAverageDiamond() + this.getAverageStone();

      final var goldPercentage =
          averageOres == 0 ? 0 : (profileData.getGoldMined() * 100) / averageOres;
      final var diamondPercentage =
          averageOres == 0 ? 0 : (profileData.getDiamondMined() * 100) / averageOres;
      final var stonePercentage =
          averageOres == 0 ? 0 : (profileData.getStoneMined() * 100) / averageOres;

      return new ItemBuilder(Material.SKULL_ITEM)
          .data(3)
          .skullOwner(this.profile.getName())
          .name("&6&l" + this.profile.getName())
          .lore(
              "",
              "&e "
                  + CC.SQUARE
                  + " &fOr: &e"
                  + profileData.getGoldMined()
                  + " &8(&f"
                  + (goldDifference > 0 ? "&a+" : goldDifference < 0 ? "&c" : "&7")
                  + goldDifference
                  + "&8) (&e"
                  + goldPercentage
                  + "%&8)",
              "&e "
                  + CC.SQUARE
                  + " &fDiamant: &b"
                  + profileData.getDiamondMined()
                  + " &8(&f"
                  + (diamondDifference > 0 ? "&a+" : diamondDifference < 0 ? "&c" : "&7")
                  + diamondDifference
                  + "&8) (&b"
                  + diamondPercentage
                  + "%&8)",
              "&e "
                  + CC.SQUARE
                  + "&f Pierre: &7"
                  + profileData.getStoneMined()
                  + "&8 (&f"
                  + stonePercentage
                  + "%&8)",
              "",
              "&7" + CC.BAR + "&f Permet de se&d téléporter&f au joueur.",
              "")
          .asItemStack();
    }

    @Override
    public void onClick(final ButtonClick click) {
      final var player = click.getMenu().getPlayer();
      final var target = this.profile.getPlayer();
      if (target == null || !target.isOnline()) {
        return;
      }

      player.teleport(target);
    }

    private int getAverageGold() {
      final var profiles = this.getRelevantProfiles();
      return profiles.stream()
              .mapToInt(value -> value.getComponent(ProfileMiningComponent.class).getGoldMined())
              .sum()
          / profiles.size();
    }

    private int getAverageDiamond() {
      final var profiles = this.getRelevantProfiles();
      return profiles.stream()
              .mapToInt(value -> value.getComponent(ProfileMiningComponent.class).getDiamondMined())
              .sum()
          / profiles.size();
    }

    private int getAverageStone() {
      final var profiles = this.getRelevantProfiles();
      return profiles.stream()
              .mapToInt(value -> value.getComponent(ProfileMiningComponent.class).getStoneMined())
              .sum()
          / profiles.size();
    }

    private Collection<? extends Profile> getRelevantProfiles() {
      return this.profileService.getPlayingProfiles();
    }
  }
}
