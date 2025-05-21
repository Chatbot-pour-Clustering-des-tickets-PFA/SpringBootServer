package net.bouraoui.technician.Controllers;

import lombok.AllArgsConstructor;
import net.bouraoui.technician.kafka.KafkaProducerService;
import net.bouraoui.technician.kafka.KafkaResponseHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/technician")
public class TechnicianController {

    private final KafkaResponseHandler kafkaResponseHandler;
    private final KafkaProducerService kafkaProducerService;

    @GetMapping("/getTickets/{technician_id}")
    public ResponseEntity<?> getTickets(@PathVariable("technician_id") int technician_id) {
        String correlationId = UUID.randomUUID().toString();

        Map<String, Object> message = new HashMap<>();
        message.put("action", "getTicketsByTechnicianId");
        message.put("technicianId", technician_id);
        message.put("correlationId", correlationId);

        kafkaResponseHandler.register(correlationId);
        kafkaProducerService.send("ticket-request-topic", message);

        try {
            Object response = kafkaResponseHandler.waitForResponse(correlationId, 5000);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(504).body("No response from ticket service in time.");
        }

    }
}
