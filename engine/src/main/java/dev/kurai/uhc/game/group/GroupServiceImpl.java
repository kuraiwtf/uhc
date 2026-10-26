package dev.kurai.uhc.game.group;

import com.google.common.collect.Lists;
import dev.kurai.uhc.util.CC;
import java.util.Collection;

import dev.kurai.uhc.util.Title;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

@Getter
@Setter
public final class GroupServiceImpl implements GroupService {

  private final GroupProvider provider;

  private final Plugin plugin;

  private final Collection<GroupVariable> variables;

  private boolean enabled;

  public GroupServiceImpl(final Plugin plugin) {
    this.provider = new BuiltinGroupProvider();

    this.plugin = plugin;

    this.variables = Lists.newArrayList();
  }

  @Override
  public void addGroupVariable(final GroupVariable variable) {
    this.variables.add(variable);
  }

  @Override
  public void removeGroupVariable(final GroupVariable variable) {
    this.variables.remove(variable);
  }

  @Override
  public void start() {
    if (!this.enabled || this.variables.isEmpty()) {
      return;
    }

    final BukkitScheduler scheduler = this.plugin.getServer().getScheduler();
    scheduler.runTaskTimerAsynchronously(this.plugin, new GroupVariableTask(this), 0, 20);
  }

  @Override
  public void alertGroups() {
    for (final Player player : Bukkit.getOnlinePlayers()) {
        Title.sendTitle(
          player,
          0,
          3 * 20,
          0,
          "§4" + CC.DANGER + " §cGroupes de " + this.provider.groups() + " §4" + CC.DANGER,
          "");
    }
  }
}
