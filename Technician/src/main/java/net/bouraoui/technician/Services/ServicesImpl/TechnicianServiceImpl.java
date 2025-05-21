package net.bouraoui.technician.Services.ServicesImpl;

import jakarta.ws.rs.NotFoundException;
import lombok.AllArgsConstructor;
import net.bouraoui.technician.Entities.Technician;
import net.bouraoui.technician.Repositories.TechnicianRepository;
import net.bouraoui.technician.Services.TechnicianService;
import org.springframework.stereotype.Service;


@Service @AllArgsConstructor
public class TechnicianServiceImpl implements TechnicianService {

    private final TechnicianRepository technicianRepository;

    @Override
    public Technician findTechniciaByID(int id) {
        Technician technician = technicianRepository.findById(id).orElseThrow(() -> new NotFoundException("technician does not exist"));

        return technician;
    }

}
