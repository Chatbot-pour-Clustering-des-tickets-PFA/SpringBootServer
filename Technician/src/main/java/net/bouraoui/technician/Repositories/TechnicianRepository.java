package net.bouraoui.technician.Repositories;

import net.bouraoui.technician.Entities.Technician;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TechnicianRepository extends JpaRepository<Technician, Integer> {
}
