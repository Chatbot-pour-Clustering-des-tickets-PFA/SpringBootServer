package net.bouraoui.technician.Repositories;

import net.bouraoui.technician.Entities.Technician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TechnicianRepository extends JpaRepository<Technician, Integer> {

    @Query(value =
            "SELECT t.* " +
                    "FROM technician t " +
                    "LEFT JOIN ticket tk " +
                    "  ON t.id = tk.techinician_id " +
                    "  AND tk.status = 'OPEN' " +
                    "WHERE t.category = :category " +
                    "GROUP BY t.id " +
                    "ORDER BY COUNT(tk.id) ASC " +
                    "LIMIT 1",
            nativeQuery = true
    )
    Technician findTechnicianWithLeastOpenTicketsByCategory(
            @Param("category") String category
    );


    @Query(value = """
        SELECT * FROM my_user 
        WHERE role = 'Technician'
        """, nativeQuery = true)
    List<TechnicianUser> findAllTechnicians();

    @Query(value = """
        SELECT 
            priority, 
            COUNT(*) AS count 
        FROM ticket 
        WHERE techinician_id = :technicianId 
        GROUP BY priority
    """, nativeQuery = true)
    List<Object[]> countTicketsByPriority(@Param("technicianId") int technicianId);

    @Query(value = """
    SELECT 
        DATE_FORMAT(creation_date, '%Y-%m') AS month,
        COUNT(*) AS count 
    FROM ticket 
    WHERE techinician_id = :technicianId 
      AND creation_date >= DATE_SUB(CURDATE(), INTERVAL 7 MONTH)
    GROUP BY month
    ORDER BY month
""", nativeQuery = true)
    List<Object[]> countAssignedTicketsLast7Months(@Param("technicianId") int technicianId);

    @Query(value = """
        SELECT u.id AS id,
               u.username AS username,
               u.firstname AS firstname,
               u.lastname AS lastname,
               u.email AS email,
               t.category AS category
        FROM my_user u
        JOIN technician t ON t.user_id = u.id
        WHERE u.id = :userId
        """, nativeQuery = true)
    TechnicianProfile getTechnicianProfileByUserId(Long userId);

}
