package dev.elias.restaurante.auth.exception;

public class InvalidCredentialsException
        extends RuntimeException {

    public InvalidCredentialsException() {
        super("Usuário ou senha inválidos");
    }
}