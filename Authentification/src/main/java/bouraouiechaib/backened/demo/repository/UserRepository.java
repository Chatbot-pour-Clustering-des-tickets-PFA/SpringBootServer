package bouraouiechaib.backened.demo.repository;

import bouraouiechaib.backened.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // Vous pouvez ajouter des méthodes de recherche personnalisées ici si nécessaire
}



