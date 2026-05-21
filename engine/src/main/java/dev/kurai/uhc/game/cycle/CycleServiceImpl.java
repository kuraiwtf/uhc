package dev.kurai.uhc.game.cycle;

import dev.kurai.uhc.UltraHardcoreAPI;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

@Getter
@Setter
@RequiredArgsConstructor
public final class CycleServiceImpl implements CycleService {

  private boolean enabled;
  private int totalCycleDuration = 10 * 60;
  private final LinkedList<Cycle> phases = new LinkedList<>();
  private final UltraHardcoreAPI ultraHardcore;

  private int currentIndex = 0;
  private int currentCycleDuration = 0;
  private BukkitTask currentTask = null;

  @Override
  public void start() {
    if (!this.enabled) {
      return;
    }

    this.currentCycleDuration = (this.totalCycleDuration / this.phases.size()) * 20;
    this.runCycle(0);
  }

  private void runCycle(final int index) {
    if (!this.enabled) {
      return;
    }

    final List<Cycle> phaseList = List.copyOf(this.phases);
    final Cycle cycle = phaseList.get(index);
    this.currentIndex = index;

    cycle.onStart();

    this.currentTask =
        Bukkit.getScheduler()
            .runTaskLater(
                this.ultraHardcore.plugin(),
                () -> {
                  cycle.onStop();
                  final int next = (index + 1) % phaseList.size();
                  this.runCycle(next);
                },
                this.currentCycleDuration);
  }

  @Override
  public void skipCycleTo(final Cycle target) {
    if (this.currentTask == null || !this.enabled) {
      return;
    }

    final List<Cycle> phaseList = List.copyOf(this.phases);
    final int targetIndex = phaseList.indexOf(target);
    if (targetIndex == -1) {
      throw new IllegalArgumentException("Cycle not found: " + target.getId());
    }

    final Cycle current = phaseList.get(this.currentIndex);

    this.currentTask.cancel();
    this.currentTask = null;

    current.onSkip();

    this.runCycle(targetIndex);
  }

  @Override
  public void skipCycle() {
    if (this.currentTask == null || !this.enabled) {
      return;
    }

    final List<Cycle> phaseList = List.copyOf(this.phases);
    final Cycle current = phaseList.get(this.currentIndex);

    this.currentTask.cancel();
    this.currentTask = null;

    current.onSkip();

    final int next = (this.currentIndex + 1) % phaseList.size();
    this.runCycle(next);
  }

  @Override
  public Collection<Cycle> getPhases() {
    return Collections.unmodifiableList(this.phases);
  }

  @Override
  public void clearPhases() {
    this.phases.clear();
  }

  @Override
  public void registerCycle(final Cycle cycle) {
    this.phases.add(cycle);
  }

  @Override
  public void registerCycle(final int index, final Cycle cycle) {
    this.phases.add(index, cycle);
  }

  @Override
  public void registerCycleBefore(final Cycle from, final Cycle cycle) {
    final var index = this.phases.indexOf(from);
    if (index == -1) {
      throw new IllegalArgumentException("Cycle not found: " + from.getId());
    }

    this.phases.add(index, cycle);
  }

  @Override
  public void registerCycleAfter(final Cycle from, final Cycle cycle) {
    final var index = this.phases.indexOf(from);
    if (index == -1) {
      throw new IllegalArgumentException("Cycle not found: " + from.getId());
    }

    this.phases.add(index + 1, cycle);
  }

  @Override
  public void unregisterCycle(final String id) {
    this.phases.removeIf(cycle -> cycle.getId().equals(id));
  }

  @Override
  public boolean hasPhase(final String id) {
    return this.phases.stream().anyMatch(cycle -> cycle.getId().equals(id));
  }
}
