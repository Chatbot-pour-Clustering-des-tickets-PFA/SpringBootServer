package net.bouraoui.fetchingtickets.Services;

import net.bouraoui.fetchingtickets.Dtos.DashboardStats;
import net.bouraoui.fetchingtickets.Entities.Ticket;

import java.util.List;
import java.util.Map;

public interface FetchingTicketsService {

    List<Ticket> findAllByUserId(int userId);

    DashboardStats fetchDashboardStats();

    List<Map<String,Object>> fetchDailyPeakHours();

    List<Map<String,Object>> fetchTopTechnicianPerformance(int limit);

    int countAssignedTicketsLastMonth(int technicianId);

    int countResolvedTicketsLastMonth(int technicianId);

    double getAverageResolutionTimeLastMonth(int technicianId);
}
