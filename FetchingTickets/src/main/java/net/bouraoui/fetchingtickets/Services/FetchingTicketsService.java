package net.bouraoui.fetchingtickets.Services;

import net.bouraoui.fetchingtickets.Entities.Ticket;

import java.util.List;

public interface FetchingTicketsService {

    List<Ticket> findAllByUserId(int userId);
}
