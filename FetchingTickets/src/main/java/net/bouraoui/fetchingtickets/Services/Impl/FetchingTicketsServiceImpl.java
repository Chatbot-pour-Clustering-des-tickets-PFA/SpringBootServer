package net.bouraoui.fetchingtickets.Services.Impl;

import lombok.AllArgsConstructor;
import net.bouraoui.fetchingtickets.Dtos.DashboardStats;
import net.bouraoui.fetchingtickets.Entities.CreateTicketRequest;
import net.bouraoui.fetchingtickets.Entities.Status;
import net.bouraoui.fetchingtickets.Entities.Ticket;
import net.bouraoui.fetchingtickets.Repositories.TicketRepository;
import net.bouraoui.fetchingtickets.Repositories.TopTechnician;
import net.bouraoui.fetchingtickets.Services.FetchingTicketsService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class FetchingTicketsServiceImpl implements FetchingTicketsService {

    private final TicketRepository ticketRepository;
    private final RestTemplate restTemplate;


    @Override
    public List<Ticket> findAllByUserId(int userId) {
        List<Ticket> tickets = ticketRepository.findByUserId(userId);
        return tickets;
    }

    @Override
    public DashboardStats fetchDashboardStats() {
        Map<String, Object> a = ticketRepository.fetchTotalAndPctChangeThisMonth();
        Map<String, Object> b = ticketRepository.fetchOpenTicketsAndPctChangeThisWeek();
        Map<String, Object> c = ticketRepository.fetchResolvedAndPctChangeThisMonth();
        // This one already returns a Double from your query
        Double avgH = ticketRepository.fetchAvgResolutionHours();

        List<Map<String, Object>> perDay = ticketRepository.fetchTicketsPerDayLast30();
        List<Map<String, Object>> byCat  = ticketRepository.fetchCountByCategory();

        return new DashboardStats(
                // total tickets this month
                ((Number) a.get("total_tickets")).intValue(),
                // tickets last month
                ((Number) a.get("last_month_tickets")).intValue(),
                // percent change from last month (may be null)
                a.get("pct_change_from_last_month") == null
                        ? null
                        : ((Number) a.get("pct_change_from_last_month")).doubleValue(),

                // open tickets this week
                ((Number) b.get("open_tickets")).intValue(),
                // open last week
                ((Number) b.get("last_week_open")).intValue(),
                // percent change from last week
                b.get("pct_change_from_last_week") == null
                        ? null
                        : ((Number) b.get("pct_change_from_last_week")).doubleValue(),

                // resolved tickets this month
                ((Number) c.get("resolved_tickets")).intValue(),
                // resolved last month
                ((Number) c.get("last_month_resolved")).intValue(),
                // percent change from last month (resolved)
                c.get("pct_change_from_last_month") == null
                        ? null
                        : ((Number) c.get("pct_change_from_last_month")).doubleValue(),

                // average resolution hours
                avgH,
                // tickets per day last 30
                perDay,
                // count by category
                byCat
        );
    }

    @Override
    public List<Map<String,Object>> fetchDailyPeakHours() {
        return ticketRepository.fetchDailyPeakHours();
    }

    @Override
    public List<TopTechnician> fetchTopTechnicianPerformance(int limit) {
        return ticketRepository.fetchTopTechnicianPerformance(limit);
    }

    @Override
    public List<Map<String,Object>> fetchAvgResolutionByPriority() {
        return ticketRepository.fetchAvgResolutionByPriority();
    }


    @Override
    public int AssignerTechician(Ticket ticket) {

        String url = "http://localhost:8083/api/technicians/least-loaded?category={category}";
        int techId = Math.toIntExact(restTemplate
                .getForObject(url, Long.class, ticket.getCategory().name()));


        return techId;
    }


}
