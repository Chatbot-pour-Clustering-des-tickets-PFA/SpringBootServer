package net.bouraoui.technician.Entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Technician {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private int userId;
}
