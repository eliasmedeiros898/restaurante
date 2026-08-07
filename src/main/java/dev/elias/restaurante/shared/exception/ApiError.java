package dev.elias.restaurante.shared.exception;

import java.time.OffsetDateTime;

public record ApiError(
        OffsetDateTime timestamp,
        Integer status,
        String error,
        String message,
        String path
) {
}