package net.bouraoui.fetchingtickets.Services.Impl;

import lombok.AllArgsConstructor;
import net.bouraoui.fetchingtickets.Entities.Ticket;
import net.bouraoui.fetchingtickets.Repositories.TicketRepository;
import net.bouraoui.fetchingtickets.Services.FetchingTicketsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FetchingTicketsServiceImpl implements FetchingTicketsService {

    private final TicketRepository ticketRepository;


    @Override
    public List<Ticket> findAllByUserId(int userId) {
        List<Ticket> tickets = ticketRepository.findByUserId(userId);
        return tickets;
    }
}
