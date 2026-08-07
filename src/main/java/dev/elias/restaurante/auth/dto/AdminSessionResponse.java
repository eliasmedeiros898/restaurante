package dev.elias.restaurante.auth.dto;

import dev.elias.restaurante.auth.entity.AdminRole;
import dev.elias.restaurante.auth.security.AdminPrincipal;

public record AdminSessionResponse(
        Long id,
        String name,
        String username,
        AdminRole role
) {

    public static AdminSessionResponse from(
            AdminPrincipal principal
    ) {
        return new AdminSessionResponse(
                principal.id(),
                principal.name(),
                principal.username(),
                principal.role()
        );
    }
}