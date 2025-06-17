package bouraouiechaib.space.demo.model;


import bouraouiechaib.space.demo.Repositories.ClientList;
import bouraouiechaib.space.demo.Repositories.TechnicianProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
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


    @Query(value = "Select u from MyUser u where u.role='Client' ")
    List<ClientList> findClientInfo();

    @Query(value = """
        SELECT u.id AS id,
               u.username AS username,
               u.firstname AS firstname,
               u.lastname AS lastname,
               u.email AS email,
               t.category AS category
        FROM my_user u
        JOIN technician t ON t.user_id = u.id
        WHERE u.id = :userId
        """, nativeQuery = true)
    TechnicianProfile getTechnicianProfileByUserId(Long userId);
}