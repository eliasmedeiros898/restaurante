package dev.elias.restaurante.auth.security;

import dev.elias.restaurante.auth.entity.AdminRole;
import dev.elias.restaurante.auth.entity.AdminUser;

import java.io.Serializable;

public record AdminPrincipal(
        Long id,
        String name,
        String username,
        AdminRole role
) implements Serializable {

    public static AdminPrincipal from(
            AdminUser user
    ) {
        return new AdminPrincipal(
                user.getId(),
                user.getName(),
                user.getUsername(),
                user.getRole()
        );
    }
}