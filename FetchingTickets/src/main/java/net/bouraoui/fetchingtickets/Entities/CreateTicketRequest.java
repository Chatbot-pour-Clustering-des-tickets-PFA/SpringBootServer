package net.bouraoui.fetchingtickets.Entities;

import java.time.Instant;

public record CreateTicketRequest(
        String title,
        String description,
        Instant creationDate,
        Instant resolvedDate,
        Instant modificationDate,
        Priority priority,
        int userId,
        String answer,
        Category category,
        Status status,
        int technicianId,
        int clientId,
        ResolverType resolvedBy
) {
}
