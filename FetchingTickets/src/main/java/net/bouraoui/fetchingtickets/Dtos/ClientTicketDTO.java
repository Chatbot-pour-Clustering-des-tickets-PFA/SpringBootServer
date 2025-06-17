package net.bouraoui.fetchingtickets.Dtos;

import java.time.Instant;

public interface ClientTicketDTO {
    int getId();
    String getTitle();
    String getDescription();
    String getCategory();
    String getStatus();
    Instant getCreationDate();
    Instant getResolvedDate();
    Instant getModificationDate();
    String getPriority();
    int getUserId();
    String getAnswer();
    int getTechinicianId();
    String getResolvedBy();
    String getTechnicianEmail();
}
