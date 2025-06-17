package net.bouraoui.technician.Services.ServicesImpl;

import lombok.AllArgsConstructor;
import net.bouraoui.technician.Entities.Technician;
import net.bouraoui.technician.Repositories.TechnicianRepository;
import net.bouraoui.technician.Services.TechnicianService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service @AllArgsConstructor
public class TechnicianServiceImpl implements TechnicianService {

    private final TechnicianRepository technicianRepository;

    @Override
    public Technician findTechniciaByID(int id) {
        return technicianRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Technician does not exist"));
    }


}
