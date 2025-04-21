package com.backened.demo.repository;

import com.backened.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // Vous pouvez ajouter des méthodes de recherche personnalisées ici si nécessaire
}



