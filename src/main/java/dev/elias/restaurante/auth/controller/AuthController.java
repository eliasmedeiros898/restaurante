package dev.elias.restaurante.auth.controller;

import dev.elias.restaurante.auth.dto.AdminSessionResponse;
import dev.elias.restaurante.auth.dto.LoginRequest;
import dev.elias.restaurante.auth.security.AdminPrincipal;
import dev.elias.restaurante.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    private final SecurityContextRepository
            securityContextRepository;

    private final SecurityContextLogoutHandler
            logoutHandler;

    public AuthController(
            AuthService authService,
            SecurityContextRepository securityContextRepository,
            SecurityContextLogoutHandler logoutHandler
    ) {
        this.authService = authService;
        this.securityContextRepository =
                securityContextRepository;
        this.logoutHandler = logoutHandler;
    }

    @PostMapping("/login")
    public AdminSessionResponse login(
            @Valid
            @RequestBody
            LoginRequest request,
            HttpServletRequest servletRequest,
            HttpServletResponse servletResponse
    ) {
        AdminPrincipal principal =
                authService.authenticate(
                        request.username(),
                        request.password()
                );

        List<SimpleGrantedAuthority> authorities =
                List.of(
                        new SimpleGrantedAuthority(
                                "ROLE_"
                                        + principal
                                        .role()
                                        .name()
                        )
                );

        Authentication authentication =
                UsernamePasswordAuthenticationToken
                        .authenticated(
                                principal,
                                null,
                                authorities
                        );

        SecurityContext securityContext =
                SecurityContextHolder
                        .createEmptyContext();

        securityContext.setAuthentication(
                authentication
        );

        SecurityContextHolder.setContext(
                securityContext
        );

        /*
         * Cria a sessão antes de trocar o ID.
         * A troca reduz o risco de session fixation.
         */
        HttpSession session =
                servletRequest.getSession(true);

        servletRequest.changeSessionId();

        securityContextRepository.saveContext(
                securityContext,
                servletRequest,
                servletResponse
        );

        return AdminSessionResponse.from(
                principal
        );
    }

    @GetMapping("/me")
    public AdminSessionResponse me(
            Authentication authentication
    ) {
        AdminPrincipal principal =
                extractPrincipal(authentication);

        return AdminSessionResponse.from(
                principal
        );
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(
            Authentication authentication,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        logoutHandler.logout(
                request,
                response,
                authentication
        );
    }

    private AdminPrincipal extractPrincipal(
            Authentication authentication
    ) {
        if (
                authentication == null
                        || !authentication.isAuthenticated()
                        || !(authentication.getPrincipal()
                        instanceof AdminPrincipal principal)
        ) {
            throw new IllegalStateException(
                    "Sessão administrativa inválida"
            );
        }

        return principal;
    }
}