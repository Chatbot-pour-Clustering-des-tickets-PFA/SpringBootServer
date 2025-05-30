package bouraouiechaib.space.demo.model;


public record AuthResponse(
        String  token,
        Long    userId,
        Integer clientId,
        Integer technicianId,
        Role    role
) {}
