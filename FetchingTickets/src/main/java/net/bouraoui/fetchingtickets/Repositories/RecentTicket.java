package net.bouraoui.fetchingtickets.Repositories;

public interface RecentTicket {
    Integer getTicketId();        // maps to ticket_id
    String  getTitle();           // title
    String  getCategory();        // category
    String  getPriority();        // priority ← new
    String  getStatus();          // status   ← new

    String  getTechnicianUsername(); // technician_username
    String  getTechnicianEmail();    // technician_email

    String  getClientUsername();     // client_username
    String  getClientEmail();        // client_email
    Integer getClientId();           // client_id
}
