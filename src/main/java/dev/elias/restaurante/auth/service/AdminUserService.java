package dev.elias.restaurante.auth.service;

import dev.elias.restaurante.auth.entity.AdminRole;
import dev.elias.restaurante.auth.entity.AdminUser;
import dev.elias.restaurante.auth.repository.AdminUserRepository;
import dev.elias.restaurante.shared.exception.BusinessRuleException;
import dev.elias.restaurante.shared.exception.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminUserService {

    private final AdminUserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public AdminUserService(
            AdminUserRepository repository,
            PasswordEncoder passwordEncoder
    ) {
        this.repository = repository;
        this.passwordEncoder =
                passwordEncoder;
    }

    @Transactional
    public AdminUser createInitialAdmin(
            String name,
            String username,
            String rawPassword
    ) {
        validatePassword(rawPassword);

        if (
                repository
                        .existsByUsernameIgnoreCase(
                                username
                        )
        ) {
            throw new BusinessRuleException(
                    "Já existe um administrador com esse usuário"
            );
        }

        String encodedPassword =
                passwordEncoder.encode(
                        rawPassword
                );

        AdminUser adminUser =
                new AdminUser(
                        name,
                        username,
                        encodedPassword,
                        AdminRole.ADMIN
                );

        return repository.save(adminUser);
    }

    @Transactional(readOnly = true)
    public AdminUser findActiveByUsername(
            String username
    ) {
        return repository
                .findByUsernameIgnoreCaseAndActiveTrue(
                        username
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Usuário administrativo não encontrado"
                        )
                );
    }

    @Transactional
    public void changePassword(
            Long userId,
            String newRawPassword
    ) {
        validatePassword(newRawPassword);

        AdminUser user = repository
                .findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Usuário administrativo não encontrado"
                        )
                );

        user.changePassword(
                passwordEncoder.encode(
                        newRawPassword
                )
        );
    }

    private void validatePassword(
            String password
    ) {
        if (
                password == null
                        || password.length() < 8
        ) {
            throw new BusinessRuleException(
                    "A senha deve possuir pelo menos 8 caracteres"
            );
        }

        if (
                password.chars()
                        .noneMatch(Character::isLetter)
        ) {
            throw new BusinessRuleException(
                    "A senha deve possuir pelo menos uma letra"
            );
        }

        if (
                password.chars()
                        .noneMatch(Character::isDigit)
        ) {
            throw new BusinessRuleException(
                    "A senha deve possuir pelo menos um número"
            );
        }
    }
}