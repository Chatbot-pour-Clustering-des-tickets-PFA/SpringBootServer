package net.bouraoui.fetchingtickets.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TicketByTechnicianRequest {


        private final KafkaTemplate<String, Object> kafkaTemplate;

        public TicketByTechnicianRequest(KafkaTemplate<String, Object> kafkaTemplate) {
            this.kafkaTemplate = kafkaTemplate;
        }

        @KafkaListener(topics = "ticket-request-topic", groupId = "ticket-service-group", containerFactory = "kafkaListenerContainerFactory")
        public void listen(Map<String, Object> message) {
            System.out.println("Received request: " + message);

            String correlationId = (String) message.get("correlationId");
            Integer technicianId = (Integer) message.get("technicianId");

            // 🧠 Fake data for now – simulate database call
            List<Map<String, Object>> tickets = List.of(
                    Map.of("id", 1, "technicianId", technicianId, "title", "Fix server crash"),
                    Map.of("id", 2, "technicianId", technicianId, "title", "Replace RAM")
            );

            // ✅ Build response with the same correlationId
            Map<String, Object> response = new HashMap<>();
            response.put("correlationId", correlationId);
            response.put("data", tickets);

            kafkaTemplate.send("ticket-response-topic", response);
        }
    }


