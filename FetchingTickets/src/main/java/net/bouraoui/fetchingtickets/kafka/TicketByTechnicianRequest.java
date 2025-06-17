package net.bouraoui.fetchingtickets.kafka;

import net.bouraoui.fetchingtickets.Entities.Ticket;
import net.bouraoui.fetchingtickets.Repositories.TicketRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TicketByTechnicianRequest {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final TicketRepository ticketRepository;

    public TicketByTechnicianRequest(KafkaTemplate<String, Object> kafkaTemplate,
                                     TicketRepository ticketRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.ticketRepository = ticketRepository;
    }

    @KafkaListener(topics = "ticket-request-topic", groupId = "ticket-service-group", containerFactory = "kafkaListenerContainerFactory")
    public void listen(Map<String, Object> message) {
        System.out.println("Received request: " + message);

        String correlationId = (String) message.get("correlationId");
        Integer technicianId = (Integer) message.get("technicianId");

        List<Ticket> tickets = ticketRepository.findAllByTechinicianID(technicianId);


        List<Map<String, Object>> responseTickets = tickets.stream().map(ticket -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", ticket.getId());
            map.put("technicianId", ticket.getTechinician_id());
            map.put("title", ticket.getTitle());
            map.put("description", ticket.getDescription());
            map.put("priority", ticket.getPriority());
            map.put("type", ticket.getCategory());
            map.put("status", ticket.getStatus());
            String clientEmail = ticketRepository.findClientEmailByClientId(ticket.getUserId());
            map.put("clientEmail", clientEmail);

            map.put("createdAt", ticket.getCreationDate());
            return map;
        }).toList();

        Map<String, Object> response = new HashMap<>();
        response.put("correlationId", correlationId);
        response.put("data", responseTickets);

        kafkaTemplate.send("ticket-response-topic", response);
    }
}



