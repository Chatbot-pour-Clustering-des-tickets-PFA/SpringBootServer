package net.bouraoui.technician.kafka;


import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class KafkaTicketResponseConsumer {

    private final KafkaResponseHandler responseHandler;

    public KafkaTicketResponseConsumer(KafkaResponseHandler responseHandler) {
        this.responseHandler = responseHandler;
    }

    @KafkaListener(topics = "ticket-response-topic", groupId = "technician-group")
    public void consume(Map<String, Object> message) {
        String correlationId = (String) message.get("correlationId");
        Object data = message.get("data"); // Optional: adapt structure

        responseHandler.complete(correlationId, data);
    }
}
