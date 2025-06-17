package net.bouraoui.technician.kafka;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class KafkaTicketResponseConsumer {

    private final KafkaResponseHandler responseHandler;
    private final ObjectMapper objectMapper = new ObjectMapper();


    public KafkaTicketResponseConsumer(KafkaResponseHandler responseHandler) {
        this.responseHandler = responseHandler;
    }

    @KafkaListener(topics = "ticket-response-topic", groupId = "technician-group")
    public void consume(String message) {
        try {
            Map<String, Object> payload = objectMapper.readValue(message, new TypeReference<>() {});
            String correlationId = (String) payload.get("correlationId");
            Object data = payload.get("data");

            responseHandler.complete(correlationId, data);
        } catch (Exception e) {
            e.printStackTrace(); // Optional: replace with a logger in production
        }
    }
}
