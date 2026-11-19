package dev.kurai.uhc.helpop;

import org.jspecify.annotations.NullMarked;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@NullMarked
public interface HelpOpService {

    HelpOpTicket createTicket(final UUID askerId, final String askerName, final String question);

    Optional<HelpOpTicket> getTicket(final int id);

    Collection<HelpOpTicket> getTickets();

    void registerAction(final HelpOpAction action);

    Collection<HelpOpAction> getActions();
}
