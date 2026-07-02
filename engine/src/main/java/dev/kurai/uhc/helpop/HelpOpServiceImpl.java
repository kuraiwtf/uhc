package dev.kurai.uhc.helpop;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class HelpOpServiceImpl implements HelpOpService {

  private final Map<Integer, HelpOpTicket> tickets;
  private final AtomicInteger idGenerator;
  private final List<HelpOpAction> actions;

  public HelpOpServiceImpl() {
    this.tickets = Maps.newHashMap();
    this.idGenerator = new AtomicInteger(1);
    this.actions = Lists.newArrayList();

    this.registerAction(
        new HelpOpAction(
            "Téléporter",
            NamedTextColor.GREEN,
            "Se téléporter au joueur.",
            false,
            id -> "/helpopanswer tp " + id));
    this.registerAction(
        new HelpOpAction(
            "Inventaire",
            NamedTextColor.YELLOW,
            "Voir son inventaire.",
            false,
            id -> "/helpopanswer inventaire " + id));
    this.registerAction(
        new HelpOpAction(
            "Répondre",
            NamedTextColor.AQUA,
            "Lui répondre.",
            true,
            id -> "/helpopanswer repondre " + id + " "));
    this.registerAction(
        new HelpOpAction(
            "Pseudo",
            NamedTextColor.LIGHT_PURPLE,
            "Voir son pseudo.",
            false,
            id -> "/helpopanswer qui " + id));
  }

  @Override
  public HelpOpTicket createTicket(
      final UUID askerId, final String askerName, final String question) {
    final var ticket =
        new HelpOpTicket(
            this.idGenerator.getAndIncrement(), askerId, askerName, question, Instant.now());
    this.tickets.put(ticket.id(), ticket);
    return ticket;
  }

  @Override
  public Optional<HelpOpTicket> getTicket(final int id) {
    return Optional.ofNullable(this.tickets.get(id));
  }

  @Override
  public Collection<HelpOpTicket> getTickets() {
    return this.tickets.values();
  }

  @Override
  public void registerAction(final HelpOpAction action) {
    this.actions.add(action);
  }

  @Override
  public Collection<HelpOpAction> getActions() {
    return this.actions;
  }
}
