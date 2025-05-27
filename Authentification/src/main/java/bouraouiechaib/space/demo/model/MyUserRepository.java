package bouraouiechaib.space.demo.model;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MyUserRepository extends JpaRepository<MyUser, Long> {

    Optional<MyUser> findByUsername(String username);

    @Query(value="Select u from MyUser u where u.email=:email")
    Optional<MyUser> findByEmail(String email);
}