package net.bouraoui.technician.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter@Setter@AllArgsConstructor@NoArgsConstructor
@Builder
public class Technician {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String email;

    private int userId;
}
