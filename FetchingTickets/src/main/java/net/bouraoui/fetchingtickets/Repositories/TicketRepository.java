package net.bouraoui.fetchingtickets.Repositories;

import net.bouraoui.fetchingtickets.Entities.Ticket;
import net.bouraoui.fetchingtickets.Stats.TicketsOverTime;
import net.bouraoui.fetchingtickets.Repositories.PriorityCount;
import net.bouraoui.fetchingtickets.Repositories.RecentTicket;
import net.bouraoui.fetchingtickets.Repositories.TopTechnician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    @Query("SELECT t FROM Ticket t WHERE t.techinician_id = :technicianID")
    List<Ticket> findAllByTechinicianID(@Param("technicianID") int technicianID);

    @Query("SELECT t FROM Ticket t WHERE t.userId = :userID")
    List<Ticket> findByUserId(@Param("userID") int userID);

    @Query(value = """
      WITH this_month AS (
        SELECT COUNT(*) AS cnt
          FROM ticket
         WHERE creation_date >= DATE_FORMAT(NOW(), '%Y-%m-01')
      ),
      last_month AS (
        SELECT COUNT(*) AS cnt
          FROM ticket
         WHERE creation_date >= DATE_SUB(DATE_FORMAT(NOW(), '%Y-%m-01'), INTERVAL 1 MONTH)
           AND creation_date <  DATE_FORMAT(NOW(), '%Y-%m-01')
      )
      SELECT
        this_month.cnt           AS total_tickets,
        last_month.cnt           AS last_month_tickets,
        CASE
          WHEN last_month.cnt = 0 THEN NULL
          ELSE ROUND((this_month.cnt - last_month.cnt) * 100.0 / last_month.cnt, 1)
        END                       AS pct_change_from_last_month
      FROM this_month, last_month
    """, nativeQuery = true)
    Map<String, Object> fetchTotalAndPctChangeThisMonth();

    @Query(value = """
      WITH this_week AS (
        SELECT COUNT(*) AS cnt
          FROM ticket
         WHERE status = 'open'
           AND creation_date >= DATE_SUB(CURDATE(), INTERVAL WEEKDAY(NOW()) DAY)
      ),
      last_week AS (
        SELECT COUNT(*) AS cnt
          FROM ticket
         WHERE status = 'open'
           AND creation_date >= DATE_SUB(DATE_SUB(CURDATE(), INTERVAL WEEKDAY(NOW()) DAY), INTERVAL 7 DAY)
           AND creation_date <  DATE_SUB(CURDATE(), INTERVAL WEEKDAY(NOW()) DAY)
      )
      SELECT
        this_week.cnt            AS open_tickets,
        last_week.cnt            AS last_week_open,
        CASE
          WHEN last_week.cnt = 0 THEN NULL
          ELSE ROUND((this_week.cnt - last_week.cnt) * 100.0 / last_week.cnt, 1)
        END                       AS pct_change_from_last_week
      FROM this_week, last_week
    """, nativeQuery = true)
    Map<String, Object> fetchOpenTicketsAndPctChangeThisWeek();

    @Query(value = """
      WITH this_month AS (
        SELECT COUNT(*) AS cnt
          FROM ticket
         WHERE status = 'resolved'
           AND resolved_date >= DATE_FORMAT(NOW(), '%Y-%m-01')
      ),
      last_month AS (
        SELECT COUNT(*) AS cnt
          FROM ticket
         WHERE status = 'resolved'
           AND resolved_date >= DATE_SUB(DATE_FORMAT(NOW(), '%Y-%m-01'), INTERVAL 1 MONTH)
           AND resolved_date <  DATE_FORMAT(NOW(), '%Y-%m-01')
      )
      SELECT
        this_month.cnt           AS resolved_tickets,
        last_month.cnt           AS last_month_resolved,
        CASE
          WHEN last_month.cnt = 0 THEN NULL
          ELSE ROUND((this_month.cnt - last_month.cnt) * 100.0 / last_month.cnt, 1)
        END                       AS pct_change_from_last_month
      FROM this_month, last_month
    """, nativeQuery = true)
    Map<String, Object> fetchResolvedAndPctChangeThisMonth();

    @Query(value = """
      SELECT
        ROUND(
          AVG(
            TIMESTAMPDIFF(SECOND, creation_date, resolved_date)
          ) / 3600.0
        , 1) AS avg_resolution_hours
      FROM ticket
     WHERE status = 'resolved'
       AND resolved_date IS NOT NULL
    """, nativeQuery = true)
    Double fetchAvgResolutionHours();

    @Query(value = """
      SELECT
        DATE(creation_date)        AS day,
        COUNT(*)                   AS tickets_count
      FROM ticket
      WHERE creation_date >= NOW() - INTERVAL 30 DAY
      GROUP BY day
      ORDER BY day
    """, nativeQuery = true)
    List<Map<String, Object>> fetchTicketsPerDayLast30();

    @Query(value = "SELECT category, COUNT(*) AS count FROM ticket GROUP BY category ORDER BY count DESC", nativeQuery = true)
    List<Map<String, Object>> fetchCountByCategory();

    @Query(value = """
  SELECT
    tech.id                      AS technician_id,
    tech_user.username           AS technician_username,
    tech_user.email              AS technician_email,
    COUNT(*)                     AS resolved_count
  FROM ticket t
  JOIN technician tech 
    ON t.techinician_id = tech.id
  JOIN my_user tech_user 
    ON tech.user_id = tech_user.id
  WHERE t.status = 'RESOLVED'
    AND t.resolved_date IS NOT NULL
  GROUP BY
    tech.id,
    tech_user.username,
    tech_user.email
  ORDER BY resolved_count DESC
  LIMIT :limit
""", nativeQuery = true)
    List<TopTechnician> fetchTopTechnicianPerformance(@Param("limit") int limit);


    @Query(value = """
      WITH counts AS (
        SELECT
          DATE(creation_date) AS day,
          HOUR(creation_date) AS hour,
          COUNT(*)           AS cnt
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
        c.hour      AS peak_hour,
        c.cnt       AS ticket_count
      FROM counts c
      JOIN max_per_day m
        ON c.day = m.day
       AND c.cnt = m.max_cnt
      ORDER BY c.day
    """, nativeQuery = true)
    List<Map<String, Object>> fetchDailyPeakHours();

    @Query(value = """
      SELECT
        t.priority                                  AS priority,
        ROUND(
          AVG(
            TIMESTAMPDIFF(SECOND, t.creation_date, t.resolved_date)
          ) / 3600.0
        , 1)                                         AS avg_resolution_hours
      FROM ticket t
     WHERE t.status = 'resolved'
       AND t.resolved_date IS NOT NULL
     GROUP BY t.priority
     ORDER BY t.priority
    """, nativeQuery = true)
    List<Map<String, Object>> fetchAvgResolutionByPriority();

    @Query(value = """
      SELECT
        DATE(t.creation_date)      AS day,
        COUNT(*)                  AS ticket_count
      FROM ticket t
      GROUP BY DATE(t.creation_date)
      ORDER BY DATE(t.creation_date)
    """, nativeQuery = true)
    List<TicketsOverTime> fetchTicketsOverTime();

    @Query(value = """
  SELECT
    t.id                       AS ticket_id,
    t.title                    AS title,
    t.category                 AS category,
    t.priority                 AS priority,
    t.status                   AS status,
    tech_user.username         AS technician_username,
    tech_user.email            AS technician_email,
    tech_user.firstname        AS technician_firstname,
    tech_user.lastname         AS technician_lastname,
    client_user.username       AS client_username,
    client_user.email          AS client_email,
    client_user.firstname      AS client_firstname,
    client_user.lastname       AS client_lastname,
    c.id                       AS client_id
  FROM ticket t
  JOIN technician tech      ON t.techinician_id = tech.id
  JOIN my_user tech_user    ON tech.user_id = tech_user.id
  JOIN client c             ON t.user_id = c.id
  JOIN my_user client_user  ON c.user_id = client_user.id
  ORDER BY t.creation_date DESC
  LIMIT :limit
""", nativeQuery = true)
    List<RecentTicket> fetchRecentTickets(@Param("limit") int limit);




    @Query(value = """
      SELECT
        t.priority    AS priority,
        COUNT(*)      AS count
      FROM ticket t
      GROUP BY t.priority
      ORDER BY count DESC
    """, nativeQuery = true)
    List<PriorityCount> fetchCountByPriority();
}