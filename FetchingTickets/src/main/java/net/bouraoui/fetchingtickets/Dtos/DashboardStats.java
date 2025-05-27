package net.bouraoui.fetchingtickets.Dtos;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter@Setter
public class DashboardStats {

    private final int totalTickets;
    private final int lastMonthTickets;
    private final Double pctChangeThisMonth;

    private final int openTickets;
    private final int lastWeekOpen;
    private final Double pctChangeThisWeek;

    private final int resolvedTickets;
    private final int lastMonthResolved;
    private final Double pctChangeResolvedThisMonth;

    private final Double avgResolutionHours;

    private final List<Map<String,Object>> ticketsPerDay;

    private final List<Map<String,Object>> countByCategory;

    public DashboardStats(
            int totalTickets,
            int lastMonthTickets,
            Double pctChangeThisMonth,
            int openTickets,
            int lastWeekOpen,
            Double pctChangeThisWeek,
            int resolvedTickets,
            int lastMonthResolved,
            Double pctChangeResolvedThisMonth,
            Double avgResolutionHours,
            List<Map<String,Object>> ticketsPerDay,
            List<Map<String,Object>> countByCategory
    ) {
        this.totalTickets                 = totalTickets;
        this.lastMonthTickets             = lastMonthTickets;
        this.pctChangeThisMonth           = pctChangeThisMonth;
        this.openTickets                  = openTickets;
        this.lastWeekOpen                 = lastWeekOpen;
        this.pctChangeThisWeek            = pctChangeThisWeek;
        this.resolvedTickets              = resolvedTickets;
        this.lastMonthResolved            = lastMonthResolved;
        this.pctChangeResolvedThisMonth   = pctChangeResolvedThisMonth;
        this.avgResolutionHours           = avgResolutionHours;
        this.ticketsPerDay                = ticketsPerDay;
        this.countByCategory              = countByCategory;
    }

}
