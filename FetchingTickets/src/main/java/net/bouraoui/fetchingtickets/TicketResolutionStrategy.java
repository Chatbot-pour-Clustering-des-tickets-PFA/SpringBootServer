package net.bouraoui.fetchingtickets;

import net.bouraoui.fetchingtickets.Entities.Ticket;

public interface TicketResolutionStrategy {
    void resolveTicket(Ticket ticket);
}
