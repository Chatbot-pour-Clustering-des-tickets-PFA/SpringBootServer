package net.bouraoui.fetchingtickets.Repositories;

import net.bouraoui.fetchingtickets.Entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;


public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    @Query("Select t from Ticket t where t.techinician_id=:technicianID")
    List<Ticket> findAllByTechinicianID(int technicianID);

    @Query("Select t from Ticket t where t.userId=:userID")
    List<Ticket> findByUserId(int userID);

    @Query(value = """
    WITH this_month AS (
      SELECT COUNT(*) AS cnt
        FROM ticket
       WHERE creation_date >= date_trunc('month', now())
    ),
    last_month AS (
      SELECT COUNT(*) AS cnt
        FROM ticket
       WHERE creation_date >= date_trunc('month', now() - INTERVAL '1 month')
         AND creation_date <  date_trunc('month', now())
    )
    SELECT
      this_month.cnt           AS total_tickets,
      last_month.cnt           AS last_month_tickets,
      CASE
        WHEN last_month.cnt = 0 THEN NULL
        ELSE ROUND((this_month.cnt - last_month.cnt)*100.0 / last_month.cnt, 1)
      END                       AS pct_change_from_last_month
    FROM this_month, last_month
  """, nativeQuery = true)
    Map<String, Object> fetchTotalAndPctChangeThisMonth();


    @Query(value = """
    WITH this_week AS (
      SELECT COUNT(*) AS cnt
        FROM ticket
       WHERE status = 'open'
         AND creation_date >= date_trunc('week', now())
    ),
    last_week AS (
      SELECT COUNT(*) AS cnt
        FROM ticket
       WHERE status = 'open'
         AND creation_date >= date_trunc('week', now() - INTERVAL '1 week')
         AND creation_date <  date_trunc('week', now())
    )
    SELECT
      this_week.cnt            AS open_tickets,
      last_week.cnt            AS last_week_open,
      CASE
        WHEN last_week.cnt = 0 THEN NULL
        ELSE ROUND((this_week.cnt - last_week.cnt)*100.0 / last_week.cnt, 1)
      END                       AS pct_change_from_last_week
    FROM this_week, last_week
  """, nativeQuery = true)
    Map<String, Object> fetchOpenTicketsAndPctChangeThisWeek();


    @Query(value = """
    WITH this_month AS (
      SELECT COUNT(*) AS cnt
        FROM ticket
       WHERE status = 'resolved'
         AND resolved_date >= date_trunc('month', now())
    ),
    last_month AS (
      SELECT COUNT(*) AS cnt
        FROM ticket
       WHERE status = 'resolved'
         AND resolved_date >= date_trunc('month', now() - INTERVAL '1 month')
         AND resolved_date <  date_trunc('month', now())
    )
    SELECT
      this_month.cnt           AS resolved_tickets,
      last_month.cnt           AS last_month_resolved,
      CASE
        WHEN last_month.cnt = 0 THEN NULL
        ELSE ROUND((this_month.cnt - last_month.cnt)*100.0 / last_month.cnt, 1)
      END                       AS pct_change_from_last_month
    FROM this_month, last_month
  """, nativeQuery = true)
    Map<String, Object> fetchResolvedAndPctChangeThisMonth();


    @Query(value = """
    SELECT
      ROUND(
        AVG(
          EXTRACT(EPOCH FROM (resolved_date - creation_date))
        ) / 3600.0
      , 1) AS avg_resolution_hours
      FROM ticket
     WHERE status = 'resolved'
       AND resolved_date IS NOT NULL
  """, nativeQuery = true)
    Double fetchAvgResolutionHours();


    @Query(value = """
    SELECT
      (date_trunc('day', creation_date))::date AS day,
      COUNT(*)                                 AS tickets_count
    FROM ticket
    WHERE creation_date >= now() - INTERVAL '30 days'
    GROUP BY day
    ORDER BY day
  """, nativeQuery = true)
    List<Map<String, Object>> fetchTicketsPerDayLast30();



    @Query(value = "SELECT category,COUNT(*) AS count FROM ticketGROUP BY category ORDER BY count DESC ",nativeQuery = true)
    List<Map<String, Object>> fetchCountByCategory();

    @Query(value = """ 
SELECT tech.id AS technician_id, tech.name S technician_name, COUNT(*) AS resolved_count
        FROM ticket t JOIN technician tech ON t.techinician_id = tech.id WHERE t.status = 'RESOLVED'AND t.resolved_date IS NOT NULL GROUP BY tech.id, tech.name ORDER BY resolved_count DESC
        LIMIT :limit
        """,nativeQuery = true )
    List<Map<String,Object>> fetchTopTechnicianPerformance(@Param("limit") int limit);

    @Query(value = """
    WITH counts AS (
      SELECT
        (date_trunc('day', creation_date))::date AS day,
        EXTRACT(HOUR FROM creation_date)::int   AS hour,
        COUNT(*)                               AS cnt
      FROM ticket
      GROUP BY day, hour
    ),
    max_per_day AS (
      SELECT day, MAX(cnt) AS max_cnt
      FROM counts
      GROUP BY day
    )
    SELECT
      c.day,
      c.hour       AS peak_hour,
      c.cnt        AS ticket_count
    FROM counts c
    JOIN max_per_day m
      ON c.day = m.day
     AND c.cnt = m.max_cnt
    ORDER BY c.day
  """, nativeQuery = true)
    List<Map<String,Object>> fetchDailyPeakHours();
    @Query(value = """
    SELECT COUNT(*) 
    FROM ticket 
    WHERE techinician_id = :technicianId 
      AND creation_date >= date_trunc('month', now() - interval '1 month') 
      AND creation_date < date_trunc('month', now())
""", nativeQuery = true)
    int countAssignedTicketsLastMonth(@Param("technicianId") int technicianId);


    @Query(value = """
    SELECT COUNT(*) 
    FROM ticket 
    WHERE techinician_id = :technicianId 
      AND status = 'RESOLVED' 
      AND resolved_date >= date_trunc('month', now() - interval '1 month') 
      AND resolved_date < date_trunc('month', now())
""", nativeQuery = true)
    int countResolvedTicketsLastMonth(@Param("technicianId") int technicianId);


    @Query(value = """
    SELECT 
      ROUND(
        AVG(EXTRACT(EPOCH FROM (resolved_date - creation_date)) / 3600.0), 1
      )
    FROM ticket 
    WHERE techinician_id = :technicianId 
      AND status = 'RESOLVED' 
      AND resolved_date IS NOT NULL 
      AND resolved_date >= date_trunc('month', now() - interval '1 month') 
      AND resolved_date < date_trunc('month', now())
""", nativeQuery = true)
    Double getAverageResolutionTimeLastMonth(@Param("technicianId") int technicianId);

}
