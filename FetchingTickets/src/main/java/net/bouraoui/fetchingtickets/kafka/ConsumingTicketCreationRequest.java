package net.bouraoui.fetchingtickets.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.bouraoui.fetchingtickets.Entities.*;
import net.bouraoui.fetchingtickets.Repositories.TicketRepository;
import net.bouraoui.fetchingtickets.Services.FetchingTicketsService;
import org.springframework.kafka.annotation.KafkaListener;

import java.time.Instant;
import java.util.Map;
import com.fasterxml.jackson.core.type.TypeReference;


@AllArgsConstructor
public class ConsumingTicketCreationRequest {

    private final FetchingTicketsService fetchingTicketsService;
    private final TicketRepository ticketRepository;
    private final ObjectMapper            objectMapper;


    @KafkaListener(topics = "ticketCreation", groupId = "first")
    public void consume(String message) {
        try {
            // 1) Désérialisation JSON → Map
            Map<String, Object> payload = objectMapper.readValue(
                    message,
                    new TypeReference<Map<String, Object>>() {}
            );

            // 2) Extraction des champs
            String title       = (String) payload.get("Title");
            String description = (String) payload.get("Description");
            String priStr      = (String) payload.get("Priority");
            String catStr      = (String) payload.get("category");
            Integer userId     = (Integer) payload.get("userId");
            Integer clientId   = payload.get("clientId") != null
                    ? (Integer) payload.get("clientId")
                    : 0;

            // 3) Construction du record
            CreateTicketRequest ticketReq = new CreateTicketRequest(
                    title,
                    description,
                    Instant.now(),
                    null,
                    Instant.now(),
                    Priority.valueOf(priStr.toUpperCase()),
                    userId,
                    null,
                    Category.valueOf(catStr
                            .toUpperCase()
                            .replace(' ', '_')
                            .replace("&", "AND")),
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
        int technicianId = fetchingTicketsService.AssignerTechician(ticket);

        ticket.setTechinician_id(technicianId);
        ticketRepository.save(ticket);

    }
}
