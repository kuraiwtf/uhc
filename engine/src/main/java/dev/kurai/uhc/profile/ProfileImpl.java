package dev.kurai.uhc.profile;

import static dev.kurai.uhc.util.CC.prefix;
import static net.kyori.adventure.text.Component.text;

import com.google.common.collect.Maps;
import dev.kurai.uhc.UltraHardcoreAPI;
import dev.kurai.uhc.actionbar.Actionbar;
import dev.kurai.uhc.ecs.component.Component;
import dev.kurai.uhc.ecs.component.defaults.NameComponent;
import dev.kurai.uhc.ecs.entity.Entity;
import dev.kurai.uhc.module.power.AbstractPower;
import dev.kurai.uhc.module.power.defaults.item.AbstractItemPower;
import dev.kurai.uhc.profile.component.*;
import dev.kurai.uhc.profile.state.ProfileState;
import dev.kurai.uhc.profile.state.WaitingProfileState;
import java.util.*;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ProfileImpl implements Profile {

  private final Map<Class<? extends Component>, Component> components;

  private final UltraHardcoreAPI ultraHardcore;
  private final Map<String, AbstractPower> powers;

  public ProfileImpl(final Player player, final UltraHardcoreAPI ultraHardcore) {
    this(player.getUniqueId(), player.getName(), ultraHardcore);
  }

  public ProfileImpl(final UUID id, final String name, final UltraHardcoreAPI ultraHardcore) {
    this.components = Maps.newHashMap();
    this.addComponents(
        new NameComponent(name),
        new ClaimComponent(),
        new ProfileIdentifierComponent(id),
        new ProfileMiningComponent(),
        new ProfileStateComponent(new WaitingProfileState()));

    this.ultraHardcore = ultraHardcore;
    this.powers = Maps.newHashMap();
  }

  @Override
  public Audience audience() {
    return this.ultraHardcore.getBukkitAudiences().player(this.getId());
  }

  @Override
  public UUID getId() {
    return this.getComponent(ProfileIdentifierComponent.class).getIdentifier();
  }

  @Override
  public String getName() {
    return this.getComponent(NameComponent.class).getName();
  }

  public void setName(final String name) {
    this.getComponent(NameComponent.class).setName(name);
  }

  @Override
  public Actionbar getActionbar() {
    return this.ultraHardcore.getActionbarService().getActionbar(this.getId());
  }

  @Override
  public void addItem(final ItemStack item) {
    this.findPlayer()
        .ifPresent(
            player -> {
              final var left = player.getInventory().addItem(item);
              if (left.isEmpty()) {
                return;
              }

              this.getComponent(ClaimComponent.class).getItems().addAll(left.values());
              this.sendMessage(
                  prefix()
                      .append(text("Un objet a été ajouté à votre "))
                      .append(text("/claim", NamedTextColor.GREEN))
                      .append(text('.'))
                      .build());
            });
  }

  @Override
  public ProfileState getState() {
    return this.getComponent(ProfileStateComponent.class).getState();
  }

  @Override
  public void setState(final ProfileState state) {
    if (this.getState() != null) {
      this.getState().onExit(this);
    }

    this.getComponent(ProfileStateComponent.class).setState(state);
    state.onEntry(this);
  }

  @Override
  public Collection<Component> getComponents() {
    return this.components.values();
  }

  @Override
  public <E extends Entity<UUID>> E addComponent(final Component component) {
    this.components.put(component.getClass(), component);
    return (E) this;
  }

  @Override
  public boolean removeComponent(final Class<? extends Component> componentClass) {
    return this.components.remove(componentClass) != null;
  }

  @Override
  public boolean hasComponent(final Class<? extends Component> componentClass) {
    return this.components.containsKey(componentClass);
  }

  @Override
  public <T extends Component> T getComponent(final Class<T> componentClass) {
    return Optional.ofNullable(this.components.get(componentClass))
        .map(componentClass::cast)
        .orElse(null);
  }

  @Override
  public Collection<AbstractPower> getPowers() {
    return List.copyOf(this.powers.values());
  }

  @Override
  public <T extends AbstractPower> T getPower(final Class<T> clazz) {
    return this.powers.values().stream()
        .filter(power -> clazz.isAssignableFrom(power.getClass()))
        .map(clazz::cast)
        .findFirst()
        .orElse(null);
  }

  @Override
  public void registerPower(final AbstractPower power) {
    this.powers.put(power.getId(), power);

    if (power instanceof final Listener listener) {
      this.ultraHardcore.getEventService().registerListener(listener);
    }

    if (power instanceof final AbstractItemPower itemPower) {
      this.findPlayer()
          .ifPresentOrElse(
              player -> this.addItem(itemPower.provideIcon(player)),
              () ->
                  this.getComponent(OfflineActionComponent.class)
                      .getActions()
                      .add(player -> this.addItem(itemPower.provideIcon(player))));
    }
  }

  @Override
  public void unregisterPower(final String id) {
    this.powers.remove(id);
  }
}
