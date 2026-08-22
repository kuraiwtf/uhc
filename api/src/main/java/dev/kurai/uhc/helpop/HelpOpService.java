package dev.kurai.uhc.helpop;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface HelpOpService {

  HelpOpTicket createTicket(final UUID askerId, final String askerName, final String question);

  Optional<HelpOpTicket> getTicket(final int id);

  Collection<HelpOpTicket> getTickets();

  void registerAction(final HelpOpAction action);

  Collection<HelpOpAction> getActions();
}
