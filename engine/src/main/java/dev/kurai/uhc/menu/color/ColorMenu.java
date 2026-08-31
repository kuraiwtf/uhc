package dev.kurai.uhc.menu.color;

import dev.kurai.uhc.menu.template.BorderTemplate;
import dev.kurai.uhc.nickname.NicknameHolder;
import dev.kurai.uhc.util.Color;
import dev.kurai.uhc.util.ItemBuilder;
import java.util.Collection;
import net.j4c0b3y.api.menu.Menu;
import net.j4c0b3y.api.menu.MenuSize;
import net.j4c0b3y.api.menu.button.Button;
import net.j4c0b3y.api.menu.button.ButtonClick;
import net.j4c0b3y.api.menu.layer.impl.BackgroundLayer;
import net.j4c0b3y.api.menu.layer.impl.ForegroundLayer;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class ColorMenu extends Menu {

  private final NicknameHolder nicknameHolder;
  private final Collection<Player> players;

  public ColorMenu(
      final Player player, final NicknameHolder nicknameHolder, final Collection<Player> players) {
    super("Color - " + players.size() + " joueurs", MenuSize.FOUR, player);
    this.nicknameHolder = nicknameHolder;
    this.players = players;
  }

  @Override
  public void setup(final BackgroundLayer backgroundLayer, final ForegroundLayer front) {
    this.apply(new BorderTemplate(DyeColor.ORANGE.getData()));

    front.set(11, new ColorButton(this.nicknameHolder, this.players, Color.WHITE));
    front.set(12, new ColorButton(this.nicknameHolder, this.players, Color.GREEN));
    front.set(13, new ColorButton(this.nicknameHolder, this.players, Color.AQUA));
    front.set(14, new ColorButton(this.nicknameHolder, this.players, Color.LIGHT_PURPLE));
    front.set(15, new ColorButton(this.nicknameHolder, this.players, Color.YELLOW));

    front.set(20, new ColorButton(this.nicknameHolder, this.players, Color.GRAY));
    front.set(21, new ColorButton(this.nicknameHolder, this.players, Color.RED));
    front.set(22, new ColorButton(this.nicknameHolder, this.players, Color.DARK_AQUA));
    front.set(23, new ColorButton(this.nicknameHolder, this.players, Color.DARK_PURPLE));
    front.set(24, new ColorButton(this.nicknameHolder, this.players, Color.GOLD));
  }

  private static final class ColorButton extends Button {

    private final NicknameHolder nicknameHolder;
    private final Collection<Player> players;
    private final Color color;

    private ColorButton(
        final NicknameHolder nicknameHolder, final Collection<Player> players, final Color color) {
      this.nicknameHolder = nicknameHolder;
      this.players = players;
      this.color = color;
    }

    @Override
    public ItemStack getIcon() {
      final ChatColor chatColor = this.color.asBukkitColor();
      return new ItemBuilder(Material.INK_SACK)
          .data(this.color.asDyeColor().getDyeData())
          .name(chatColor + "&l" + this.color.name())
          .asItemStack();
    }

    @Override
    public void onClick(final ButtonClick click) {
      for (final Player player : this.players) {
        this.nicknameHolder.applyColor(player.getUniqueId(), this.color);
      }

      final Menu menu = click.getMenu();
      final Player clicker = menu.getPlayer();
      clicker.playSound(clicker.getLocation(), Sound.ORB_PICKUP, 1, 2);
      menu.close();
    }
  }
}
