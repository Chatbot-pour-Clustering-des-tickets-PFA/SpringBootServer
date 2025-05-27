package net.bouraoui.fetchingtickets.Services.Impl;

import lombok.AllArgsConstructor;
import net.bouraoui.fetchingtickets.Dtos.DashboardStats;
import net.bouraoui.fetchingtickets.Entities.Ticket;
import net.bouraoui.fetchingtickets.Repositories.TicketRepository;
import net.bouraoui.fetchingtickets.Services.FetchingTicketsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class FetchingTicketsServiceImpl implements FetchingTicketsService {

    private final TicketRepository ticketRepository;


    @Override
    public List<Ticket> findAllByUserId(int userId) {
        List<Ticket> tickets = ticketRepository.findByUserId(userId);
        return tickets;
    }

    @Override
    public DashboardStats fetchDashboardStats() {
        Map<String,Object> a = ticketRepository.fetchTotalAndPctChangeThisMonth();
        Map<String,Object> b = ticketRepository.fetchOpenTicketsAndPctChangeThisWeek();
        Map<String,Object> c = ticketRepository.fetchResolvedAndPctChangeThisMonth();
        Double avgH = ticketRepository.fetchAvgResolutionHours();
        List<Map<String,Object>> perDay = ticketRepository.fetchTicketsPerDayLast30();
        List<Map<String,Object>> byCat = ticketRepository.fetchCountByCategory();

        return new DashboardStats(
                ((Number)a.get("total_tickets")).intValue(),
                ((Number)a.get("last_month_tickets")).intValue(),
                (Double)a.get("pct_change_from_last_month"),

                ((Number)b.get("open_tickets")).intValue(),
                ((Number)b.get("last_week_open")).intValue(),
                (Double)b.get("pct_change_from_last_week"),

                ((Number)c.get("resolved_tickets")).intValue(),
                ((Number)c.get("last_month_resolved")).intValue(),
                (Double)c.get("pct_change_from_last_month"),

                avgH,
                perDay,
                byCat
        );
    }

    @Override
    public List<Map<String,Object>> fetchDailyPeakHours() {
        return ticketRepository.fetchDailyPeakHours();
    }

    @Override
    public List<Map<String,Object>> fetchTopTechnicianPerformance(int limit) {
        return ticketRepository.fetchTopTechnicianPerformance(limit);
    }

    @Override
    public List<Map<String,Object>> fetchAvgResolutionByPriority() {
        return ticketRepository.fetchAvgResolutionByPriority();
    }



}
