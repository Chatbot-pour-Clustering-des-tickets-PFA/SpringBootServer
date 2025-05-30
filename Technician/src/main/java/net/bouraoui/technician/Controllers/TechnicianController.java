package net.bouraoui.technician.Controllers;

import lombok.AllArgsConstructor;
import net.bouraoui.technician.Entities.Technician;
import net.bouraoui.technician.Repositories.TechnicianRepository;
import net.bouraoui.technician.kafka.KafkaProducerService;
import net.bouraoui.technician.kafka.KafkaResponseHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/technician")
public class TechnicianController {

    private final TechnicianRepository technicianRepository;
    private final KafkaResponseHandler kafkaResponseHandler;
    private final KafkaProducerService kafkaProducerService;
    private final RestTemplate restTemplate;

    // Replace with your actual ticket service URL or service discovery name
    @Value("${ticket.service.url:http://ticket-service/api/v1/tickets}")
    private String ticketServiceBaseUrl;

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


    @GetMapping("/monthly-stats/{technicianId}")
    public ResponseEntity<?> getMonthlyStats(@PathVariable int technicianId) {
        Map<String, Object> stats = new HashMap<>();

        try {
            int assigned = restTemplate.getForObject(
                    ticketServiceBaseUrl + "/assigned-count/" + technicianId, Integer.class);

            int resolved = restTemplate.getForObject(
                    ticketServiceBaseUrl + "/resolved-count/" + technicianId, Integer.class);

            double avgTime = restTemplate.getForObject(
                    ticketServiceBaseUrl + "/avg-resolution-time/" + technicianId, Double.class);

            stats.put("assignedTicketsLastMonth", assigned);
            stats.put("resolvedTicketsLastMonth", resolved);
            stats.put("averageResolutionTimeInHours", avgTime);

            return ResponseEntity.ok(stats);

        } catch (Exception e) {
            return ResponseEntity.status(502).body("Failed to fetch data from ticket service: " + e.getMessage());
        }
    }

    @GetMapping("/least-loaded")
    public ResponseEntity<?> getLeastLoadedTechId(
            @RequestParam("category") String category) {

        Technician tech = technicianRepository.findTechnicianWithLeastOpenTicketsByCategory(category);
        if (tech == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(tech.getId());
    }
}
