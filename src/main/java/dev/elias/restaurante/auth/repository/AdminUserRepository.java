package dev.elias.restaurante.auth.repository;

import dev.elias.restaurante.auth.entity.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminUserRepository
        extends JpaRepository<AdminUser, Long> {

    Optional<AdminUser>
    findByUsernameIgnoreCase(
            String username
    );

    Optional<AdminUser>
    findByUsernameIgnoreCaseAndActiveTrue(
            String username
    );

    boolean existsByUsernameIgnoreCase(
            String username
    );
}