package dev.elias.restaurante.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    @Bean
    public SecurityContextLogoutHandler securityContextLogoutHandler(
            SecurityContextRepository repository
    ) {
        SecurityContextLogoutHandler handler =
                new SecurityContextLogoutHandler();

        handler.setInvalidateHttpSession(true);
        handler.setClearAuthentication(true);
        handler.setSecurityContextRepository(repository);

        return handler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            SecurityContextRepository repository
    ) throws Exception {

        http

                /*
                 * CSRF temporariamente desativado.
                 *
                 * O sistema utiliza endpoints REST e autenticação
                 * administrativa baseada em sessão.
                 */
                .csrf(csrf ->
                        csrf.disable()
                )

                /*
                 * A autenticação administrativa é armazenada
                 * explicitamente na sessão HTTP.
                 */
                .securityContext(context ->
                        context
                                .securityContextRepository(repository)
                                .requireExplicitSave(true)
                )

                .authorizeHttpRequests(
                        authorization ->
                                authorization

                                        /*
                                         * =====================================================
                                         * HEALTH CHECK
                                         * =====================================================
                                         *
                                         * Utilizado pela hospedagem para verificar
                                         * se o backend está funcionando.
                                         */
                                        .requestMatchers(
                                                HttpMethod.GET,
                                                "/actuator/health"
                                        )
                                        .permitAll()

                                        /*
                                         * =====================================================
                                         * AUTENTICAÇÃO
                                         * =====================================================
                                         */

                                        /*
                                         * Login deve ser público.
                                         */
                                        .requestMatchers(
                                                HttpMethod.POST,
                                                "/api/auth/login"
                                        )
                                        .permitAll()

                                        /*
                                         * Consulta da sessão e logout
                                         * precisam de autenticação.
                                         */
                                        .requestMatchers(
                                                "/api/auth/me",
                                                "/api/auth/logout"
                                        )
                                        .authenticated()

                                        /*
                                         * =====================================================
                                         * API PÚBLICA
                                         * =====================================================
                                         */

                                        /*
                                         * Cardápio público,
                                         * bairros e outros dados públicos.
                                         */
                                        .requestMatchers(
                                                HttpMethod.GET,
                                                "/api/public/**"
                                        )
                                        .permitAll()

                                        /*
                                         * Cliente criando pedido.
                                         */
                                        .requestMatchers(
                                                HttpMethod.POST,
                                                "/api/orders"
                                        )
                                        .permitAll()

                                        /*
                                         * Cliente acompanhando pedido
                                         * pelo número público.
                                         */
                                        .requestMatchers(
                                                HttpMethod.GET,
                                                "/api/orders/number/**"
                                        )
                                        .permitAll()

                                        /*
                                         * =====================================================
                                         * ADMINISTRAÇÃO DO CARDÁPIO
                                         * =====================================================
                                         *
                                         * Apenas ADMIN e MANAGER podem
                                         * alterar cardápios.
                                         */
                                        .requestMatchers(
                                                "/api/admin/daily-menus/**"
                                        )
                                        .hasAnyRole(
                                                "ADMIN",
                                                "MANAGER"
                                        )

                                        /*
                                         * =====================================================
                                         * CONFIGURAÇÕES DE ENTREGA
                                         * =====================================================
                                         *
                                         * Apenas ADMIN e MANAGER.
                                         */
                                        .requestMatchers(
                                                "/api/delivery-zones/**"
                                        )
                                        .hasAnyRole(
                                                "ADMIN",
                                                "MANAGER"
                                        )

                                        /*
                                         * =====================================================
                                         * DEMAIS ROTAS ADMINISTRATIVAS
                                         * =====================================================
                                         *
                                         * Inclui:
                                         *
                                         * pedidos
                                         * balcão
                                         * planos
                                         * relatórios
                                         * outras operações administrativas
                                         */
                                        .requestMatchers(
                                                "/api/admin/**"
                                        )
                                        .hasAnyRole(
                                                "ADMIN",
                                                "MANAGER",
                                                "ATTENDANT"
                                        )

                                        /*
                                         * =====================================================
                                         * REGRA FINAL
                                         * =====================================================
                                         *
                                         * IMPORTANTE:
                                         *
                                         * anyRequest() precisa ser SEMPRE
                                         * a última regra deste bloco.
                                         *
                                         * Nenhum requestMatchers pode
                                         * aparecer depois dela.
                                         */
                                        .anyRequest()
                                        .denyAll()
                )

                /*
                 * APIs sem autenticação retornam HTTP 401.
                 *
                 * Não queremos redirecionamento automático
                 * para formulário HTML do Spring Security.
                 */
                .exceptionHandling(
                        exceptions ->
                                exceptions
                                        .authenticationEntryPoint(
                                                new HttpStatusEntryPoint(
                                                        UNAUTHORIZED
                                                )
                                        )
                )

                /*
                 * Desativa formulário padrão
                 * do Spring Security.
                 */
                .formLogin(
                        form ->
                                form.disable()
                )

                /*
                 * Desativa HTTP Basic.
                 */
                .httpBasic(
                        basic ->
                                basic.disable()
                )

                /*
                 * O projeto utiliza:
                 *
                 * POST /api/auth/logout
                 *
                 * Portanto, o logout automático
                 * do Spring Security fica desabilitado.
                 */
                .logout(
                        logout ->
                                logout.disable()
                );

        return http.build();
    }
}