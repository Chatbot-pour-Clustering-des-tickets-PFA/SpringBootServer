package bouraouiechaib.space.demo.Requests;

import bouraouiechaib.space.demo.model.Role;

public record RegisterRequest(
        String username,
        String firstName,
                              String LastName,
                              String email,
                              Role role,
                              String password,
                              String categories) {
}
