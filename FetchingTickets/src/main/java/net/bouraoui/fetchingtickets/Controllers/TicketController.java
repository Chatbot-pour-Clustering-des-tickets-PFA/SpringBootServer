package net.bouraoui.fetchingtickets.Controllers;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.bouraoui.fetchingtickets.Dtos.DashboardStats;
import net.bouraoui.fetchingtickets.Entities.Ticket;
import net.bouraoui.fetchingtickets.Repositories.TicketRepository;
import net.bouraoui.fetchingtickets.Repositories.TopTechnician;
import net.bouraoui.fetchingtickets.Services.FetchingTicketsService;
import net.bouraoui.fetchingtickets.TicketResolverContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/tickets/")
@AllArgsConstructor
public class TicketController {
    private final TicketRepository ticketRepository;
    private final TicketResolverContext ticketResolverContext;
    private final FetchingTicketsService fetchingTicketsService;


    @PostMapping("createTicket")
    public ResponseEntity<String> createTicket(@RequestBody Ticket ticket) {
        //assign priority
        //assign cluster
        //assign technician
        ticketRepository.save(ticket);

        return ResponseEntity.status(HttpStatus.CREATED).body("Ticket created successfully.");
    }

    @PutMapping("updateTicket/{id}")
    public ResponseEntity<String> updateTicket(@RequestBody Ticket ticket, @PathVariable int id) {
        Optional<Ticket> optionalTicket = ticketRepository.findById(id);
        if (optionalTicket.isPresent()) {
            Ticket foundTicket = optionalTicket.get();
            if (ticket.getResolvedBy() != null) {
                ticketResolverContext.resolveTicket(ticket, ticket.getResolvedBy());
            }
            ticketRepository.save(ticket);
            return ResponseEntity.ok("Ticket updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ticket not found with ID: " + id);
        }
    }

    @DeleteMapping("deleteTicket/{id}")
    public ResponseEntity<String> deleteTicket(@PathVariable int id) {
        Optional<Ticket> optionalTicket = ticketRepository.findById(id);
        if (optionalTicket.isPresent()) {
            ticketRepository.delete(optionalTicket.get());
            return ResponseEntity.ok("Ticket deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ticket not found with ID: " + id);
        }
    }


    @GetMapping("getTickets")
    public ResponseEntity<List<Ticket>> getTickets() {
        List<Ticket> tickets = ticketRepository.findAll();
        return ResponseEntity.ok(tickets);
    }

    /*@GetMapping("getTicketByTecnician/{technicianId}")
    public ResponseEntity<?> getTicketByTecnician(@PathVariable("technicianId") int id) {
        boolean exists = technicianRepository.existsById(id);
        if (!exists) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Technician with ID " + id + " not found.");
        }

        List<Ticket> tickets = ticketRepository.findAllByTechinicianID(id);

        if (tickets.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // 204 No Content
        }

        return ResponseEntity.ok(tickets);
    }*/

    @GetMapping("/stats")
    public ResponseEntity<DashboardStats> getDashboardStats() {
        DashboardStats stats = fetchingTicketsService.fetchDashboardStats();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/analytics/daily-peak-hours")
    public ResponseEntity<List<Map<String,Object>>> getDailyPeakHours() {
        return ResponseEntity.ok(fetchingTicketsService.fetchDailyPeakHours());
    }

    @GetMapping("/analytics/top-technicians")
    public ResponseEntity<List<TopTechnician>> getTopTechnicians(
            @RequestParam(name="limit", defaultValue="5") int limit
    ) {
        return ResponseEntity.ok(fetchingTicketsService.fetchTopTechnicianPerformance(limit));
    }

    @GetMapping("/analytics/avg-resolution-by-priority")
    public ResponseEntity<List<Map<String,Object>>> getAvgResolutionByPriority() {
        return ResponseEntity.ok(fetchingTicketsService.fetchAvgResolutionByPriority());
    }

}
