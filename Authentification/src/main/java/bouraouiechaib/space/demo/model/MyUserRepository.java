package bouraouiechaib.space.demo.model;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MyUserRepository extends JpaRepository<MyUser, Long> {

    Optional<MyUser> findByUsername(String username);



    @Query(value="Select u from MyUser u where u.email=:email")
    Optional<MyUser> findByEmail(String email);

    @Query(
            value = "SELECT id FROM client WHERE user_id = :userId",
            nativeQuery = true
    )
    Optional<Integer> findClientIdByUserIdNative(@Param("userId") Long userId);

    /**
     * Similarly for technician:
     */
    @Query(value="SELECT t.id FROM technician t WHERE user_id = :userId",nativeQuery = true)
    Optional<Integer> findTechnicianIdByUserId(@Param("userId") Long userId);
}