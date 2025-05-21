package net.bouraoui.fetchingtickets.Repositories;

import net.bouraoui.fetchingtickets.Entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    @Query("Select t from Ticket t where t.techinician_id=:technicianID")
    List<Ticket> findAllByTechinicianID(int technicianID);

    @Query("Select t from Ticket t where t.userId=:userID")
    List<Ticket> findByUserId(int userID);
}
