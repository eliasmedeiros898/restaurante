package dev.elias.restaurante.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(

        @NotBlank(
                message = "Informe o usuário"
        )
        @Size(
                max = 80,
                message = "O usuário deve possuir no máximo 80 caracteres"
        )
        String username,

        @NotBlank(
                message = "Informe a senha"
        )
        @Size(
                max = 200,
                message = "A senha informada é inválida"
        )
        String password
) {
}