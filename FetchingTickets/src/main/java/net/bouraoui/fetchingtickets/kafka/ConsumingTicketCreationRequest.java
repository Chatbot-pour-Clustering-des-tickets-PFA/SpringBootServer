package net.bouraoui.fetchingtickets.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.bouraoui.fetchingtickets.Entities.*;
import net.bouraoui.fetchingtickets.Repositories.TicketRepository;
import net.bouraoui.fetchingtickets.Services.FetchingTicketsService;
import org.springframework.kafka.annotation.KafkaListener;

import java.awt.*;
import java.time.Instant;
import java.util.Map;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ConsumingTicketCreationRequest {

    private final FetchingTicketsService fetchingTicketsService;
    private final TicketRepository ticketRepository;
    private final ObjectMapper            objectMapper;


    @KafkaListener(topics = "ticketCreation", groupId = "ticket-creation-group")
    public void consume(Map<String, Object> payload) {
        try {
            System.out.println("we received something");

            String title       = (String) payload.get("Title");
            String description = (String) payload.get("Description");
            String priStr      = (String) payload.get("Priority");
            System.out.println("category: " + payload.get("Category"));
            Integer catId = (Integer) payload.get("Category");


            Category[] categories = Category.values();
            Category categoryEnum = categories[catId];

            System.out.println("category: " + categoryEnum);

            System.out.println("userId: " + payload.get("userId"));

            Integer userId   = (Integer) payload.get("userId");
            System.out.println("user Id: "+ userId);
            Integer clientId = payload.get("clientId") != null ? (Integer) payload.get("clientId") : 0;

            String AnswerByDL  = (String) payload.get("AnswerByDL");
            String AnswerByRAG = (String) payload.get("AnswerByRAG");

            CreateTicketRequest ticketReq = new CreateTicketRequest(
                    title,
                    description,
                    Instant.now(),
                    null,
                    Instant.now(),
                    Priority.valueOf(priStr.toUpperCase()),
                    userId,
                    null,
                    AnswerByDL,
                    AnswerByRAG,
                    categoryEnum,
                    Status.OPEN,
                    0,
                    clientId,
                    null
            );

            createTicket(ticketReq);

        } catch (Exception e) {
            System.err.println("Error processing Kafka message: " + e.getMessage());
        }
    }

    public void createTicket(CreateTicketRequest ticketReq){
        Ticket ticket = new Ticket();
        ticket.setStatus(Status.OPEN);
        ticket.setPriority(ticketReq.priority());
        ticket.setDescription(ticketReq.description());
        ticket.setTitle(ticketReq.title());
        ticket.setUserId(ticketReq.userId());
        ticket.setCategory(ticketReq.category());
        ticket.setAnswerBYDL(ticketReq.AnswerByDL());
        ticket.setResolvedBy(ticketReq.resolvedBy());
        ticket.setCreationDate(Instant.now());
        int technicianId = fetchingTicketsService.AssignerTechician(ticket);

        System.out.println("technician id : "+technicianId);

        ticket.setTechinician_id(technicianId);
        ticketRepository.save(ticket);

    }
}
