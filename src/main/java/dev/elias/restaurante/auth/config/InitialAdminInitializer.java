package dev.elias.restaurante.auth.config;

import dev.elias.restaurante.auth.repository.AdminUserRepository;
import dev.elias.restaurante.auth.service.AdminUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InitialAdminInitializer
        implements CommandLineRunner {

    private final AdminUserRepository repository;
    private final AdminUserService service;

    @Value("${app.initial-admin.name:}")
    private String name;

    @Value("${app.initial-admin.username:}")
    private String username;

    @Value("${app.initial-admin.password:}")
    private String password;

    public InitialAdminInitializer(
            AdminUserRepository repository,
            AdminUserService service
    ) {
        this.repository = repository;
        this.service = service;
    }

    @Override
    public void run(String... args) {
        if (
                username == null
                        || username.isBlank()
                        || password == null
                        || password.isBlank()
        ) {
            return;
        }

        if (
                repository
                        .existsByUsernameIgnoreCase(
                                username
                        )
        ) {
            return;
        }

        service.createInitialAdmin(
                name == null || name.isBlank()
                        ? "Administrador"
                        : name,
                username,
                password
        );

        System.out.println(
                "Usuário administrador inicial criado: "
                        + username
        );
    }
}