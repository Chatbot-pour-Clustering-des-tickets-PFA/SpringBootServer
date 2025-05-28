package net.bouraoui.fetchingtickets.Services.Impl;

import net.bouraoui.fetchingtickets.Dtos.DashboardStats;
import net.bouraoui.fetchingtickets.Entities.Ticket;
import net.bouraoui.fetchingtickets.Repositories.TicketRepository;
import net.bouraoui.fetchingtickets.Services.FetchingTicketsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class FetchingTicketsServiceImpl implements FetchingTicketsService {

    private final TicketRepository ticketRepository;

    @Autowired
    public FetchingTicketsServiceImpl(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public List<Ticket> findAllByUserId(int userId) {
        return ticketRepository.findByUserId(userId);
    }

    @Override
    public DashboardStats fetchDashboardStats() {
        // Build and return DashboardStats using the queries from repository
        return new DashboardStats(
                ticketRepository.fetchTotalAndPctChangeThisMonth(),
                ticketRepository.fetchOpenTicketsAndPctChangeThisWeek(),
                ticketRepository.fetchResolvedAndPctChangeThisMonth(),
                ticketRepository.fetchAvgResolutionHours()
        );
    }

    @Override
    public List<Map<String, Object>> fetchDailyPeakHours() {
        return ticketRepository.fetchDailyPeakHours();
    }

    @Override
    public List<Map<String, Object>> fetchTopTechnicianPerformance(int limit) {
        return ticketRepository.fetchTopTechnicianPerformance(limit);
    }

    @Override
    public int countAssignedTicketsLastMonth(int technicianId) {
        return ticketRepository.countAssignedTicketsLastMonth(technicianId);
    }

    @Override
    public int countResolvedTicketsLastMonth(int technicianId) {
        return ticketRepository.countResolvedTicketsLastMonth(technicianId);
    }

    @Override
    public double getAverageResolutionTimeLastMonth(int technicianId) {
        Double avg = ticketRepository.getAverageResolutionTimeLastMonth(technicianId);
        return avg != null ? avg : 0.0;
    }
}
