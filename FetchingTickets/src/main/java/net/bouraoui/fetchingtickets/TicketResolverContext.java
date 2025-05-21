package net.bouraoui.fetchingtickets;

import net.bouraoui.fetchingtickets.Entities.ResolverType;
import net.bouraoui.fetchingtickets.Entities.Ticket;
import org.springframework.stereotype.Component;

@Component
public class TicketResolverContext {
    private final TechnicianResolutionStrategy technicianStrategy;
    private final ChatbotResolutionStrategy chatbotStrategy;

    public TicketResolverContext() {
        this.technicianStrategy = new TechnicianResolutionStrategy();
        this.chatbotStrategy = new ChatbotResolutionStrategy();
    }

    public void resolveTicket(Ticket ticket, ResolverType resolverType) {
        switch (resolverType) {
            case TECHNICIAN:
                technicianStrategy.resolveTicket(ticket);
                break;
            case CHATBOT:
                chatbotStrategy.resolveTicket(ticket);
                break;
            default:
                throw new IllegalArgumentException("Unsupported resolver type: " + resolverType);
        }
    }
}
