package net.bouraoui.clientservice.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpResponse;

@RestController
@RequestMapping("/api/v1/client")
public class ClientController {

    @RequestMapping("/tickets")
    public ResponseEntity<?> getTickets() {
        //fetch tickets first
        // for each ticket fetch techinician
        return ResponseEntity.ok().build();
    }
}
