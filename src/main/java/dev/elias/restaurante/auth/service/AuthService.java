package dev.elias.restaurante.auth.service;

import dev.elias.restaurante.auth.entity.AdminUser;
import dev.elias.restaurante.auth.exception.InvalidCredentialsException;
import dev.elias.restaurante.auth.repository.AdminUserRepository;
import dev.elias.restaurante.auth.security.AdminPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
public class AuthService {

    private final AdminUserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            AdminUserRepository repository,
            PasswordEncoder passwordEncoder
    ) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public AdminPrincipal authenticate(
            String username,
            String rawPassword
    ) {
        String normalizedUsername =
                normalizeUsername(username);

        AdminUser user = repository
                .findByUsernameIgnoreCaseAndActiveTrue(
                        normalizedUsername
                )
                .orElseThrow(
                        InvalidCredentialsException::new
                );

        boolean passwordMatches =
                passwordEncoder.matches(
                        rawPassword,
                        user.getPasswordHash()
                );

        if (!passwordMatches) {
            throw new InvalidCredentialsException();
        }

        user.registerLogin();

        return AdminPrincipal.from(user);
    }

    private String normalizeUsername(
            String username
    ) {
        if (
                username == null
                        || username.isBlank()
        ) {
            throw new InvalidCredentialsException();
        }

        return username
                .trim()
                .toLowerCase(Locale.ROOT);
    }
}