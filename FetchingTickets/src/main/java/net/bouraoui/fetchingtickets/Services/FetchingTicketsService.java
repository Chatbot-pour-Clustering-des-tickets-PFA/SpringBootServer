package net.bouraoui.fetchingtickets.Services;

import net.bouraoui.fetchingtickets.Dtos.DashboardStats;
import net.bouraoui.fetchingtickets.Entities.CreateTicketRequest;
import net.bouraoui.fetchingtickets.Entities.Ticket;
import net.bouraoui.fetchingtickets.Repositories.TopTechnician;

import java.util.List;
import java.util.Map;

public interface FetchingTicketsService {

    List<Ticket> findAllByUserId(int userId);

    DashboardStats fetchDashboardStats();

    List<Map<String,Object>> fetchDailyPeakHours();
    List<TopTechnician> fetchTopTechnicianPerformance(int limit);
    List<Map<String,Object>> fetchAvgResolutionByPriority();
    int AssignerTechician(Ticket ticket);
}
