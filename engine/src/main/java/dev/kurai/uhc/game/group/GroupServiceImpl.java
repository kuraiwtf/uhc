package dev.kurai.uhc.game.group;

import dev.kurai.uhc.util.CC;
import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

import dev.kurai.uhc.util.Title;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

@Getter
@Setter
public final class GroupServiceImpl implements GroupService {

  private final GroupProvider provider;

  private final Plugin plugin;

  private final Collection<GroupVariable> variables;

  private boolean enabled;

  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE)
  private BukkitTask task;

  public GroupServiceImpl(final Plugin plugin) {
    this.provider = new BuiltinGroupProvider();

    this.plugin = plugin;

    this.variables = new CopyOnWriteArrayList<>();
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

    this.stop();
    final BukkitScheduler scheduler = this.plugin.getServer().getScheduler();
    this.task = scheduler.runTaskTimerAsynchronously(this.plugin, new GroupVariableTask(this), 0, 20);
  }

  @Override
  public void stop() {
    if (this.task != null) {
      this.task.cancel();
      this.task = null;
    }
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
