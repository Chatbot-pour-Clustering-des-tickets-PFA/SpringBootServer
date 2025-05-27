package net.bouraoui.fetchingtickets.Entities;


import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Getter@Setter@Builder@AllArgsConstructor@NoArgsConstructor
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    private String description;

    private Instant creationDate;

    private Instant resolvedDate;

    private Instant modificationDate;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    private int userId;

    private String answer;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private Status status;


    private int techinician_id;

    @Enumerated(EnumType.STRING)
    private ResolverType resolvedBy;
}
