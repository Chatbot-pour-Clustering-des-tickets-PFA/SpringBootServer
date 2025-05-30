package net.bouraoui.technician.Repositories;

import net.bouraoui.technician.Entities.Technician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TechnicianRepository extends JpaRepository<Technician, Integer> {

    @Query(value =
            "SELECT t.* " +
                    "FROM technician t " +
                    "LEFT JOIN ticket tk " +
                    "  ON t.id = tk.technician_id " +
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
}
