package net.bouraoui.technician.Entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter@Setter@AllArgsConstructor@NoArgsConstructor
@Builder
public class Technician {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String email;

    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private Category category;
}
