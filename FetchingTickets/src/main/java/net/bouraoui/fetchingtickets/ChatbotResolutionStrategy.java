package net.bouraoui.fetchingtickets;

import net.bouraoui.fetchingtickets.Entities.ResolverType;
import net.bouraoui.fetchingtickets.Entities.Status;
import net.bouraoui.fetchingtickets.Entities.Ticket;

public class ChatbotResolutionStrategy implements TicketResolutionStrategy{


    @Override
    public void resolveTicket(Ticket ticket) {
        ticket.setStatus(Status.RESOLVED);
        ticket.setResolvedBy(ResolverType.CHATBOT);
    }
}
