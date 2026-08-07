package dev.elias.restaurante.auth.entity;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.Locale;
import java.util.Objects;

@Entity
@Table(
        name = "admin_users",
        indexes = {
                @Index(
                        name = "idx_admin_users_active",
                        columnList = "active"
                ),
                @Index(
                        name = "idx_admin_users_username_active",
                        columnList = "username, active"
                )
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_admin_users_username",
                        columnNames = "username"
                )
        }
)
public class AdminUser {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    @Column(
            nullable = false,
            length = 120
    )
    private String name;

    @Column(
            nullable = false,
            unique = true,
            length = 80
    )
    private String username;

    @Column(
            name = "password_hash",
            nullable = false,
            length = 255
    )
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(
            nullable = false,
            length = 30
    )
    private AdminRole role;

    @Column(nullable = false)
    private Boolean active;

    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private OffsetDateTime createdAt;

    @Column(
            name = "updated_at",
            nullable = false
    )
    private OffsetDateTime updatedAt;

    @Column(name = "last_login_at")
    private OffsetDateTime lastLoginAt;

    protected AdminUser() {
    }

    public AdminUser(
            String name,
            String username,
            String passwordHash,
            AdminRole role
    ) {
        this.name = normalizeRequired(
                name,
                "O nome é obrigatório"
        );

        this.username =
                normalizeUsername(username);

        this.passwordHash =
                normalizeRequired(
                        passwordHash,
                        "A senha criptografada é obrigatória"
                );

        this.role =
                role != null
                        ? role
                        : AdminRole.ADMIN;

        this.active = true;

        OffsetDateTime now =
                OffsetDateTime.now();

        this.createdAt = now;
        this.updatedAt = now;
    }

    public void updateProfile(
            String name,
            AdminRole role
    ) {
        this.name = normalizeRequired(
                name,
                "O nome é obrigatório"
        );

        this.role =
                Objects.requireNonNull(
                        role,
                        "A função é obrigatória"
                );

        touch();
    }

    public void changePassword(
            String passwordHash
    ) {
        this.passwordHash =
                normalizeRequired(
                        passwordHash,
                        "A senha criptografada é obrigatória"
                );

        touch();
    }

    public void activate() {
        this.active = true;
        touch();
    }

    public void deactivate() {
        this.active = false;
        touch();
    }

    public void registerLogin() {
        this.lastLoginAt =
                OffsetDateTime.now();

        touch();
    }

    private void touch() {
        this.updatedAt =
                OffsetDateTime.now();
    }

    private static String normalizeUsername(
            String value
    ) {
        return normalizeRequired(
                value,
                "O nome de usuário é obrigatório"
        )
                .toLowerCase(Locale.ROOT)
                .replaceAll("\\s+", "");
    }

    private static String normalizeRequired(
            String value,
            String message
    ) {
        if (
                value == null
                        || value.isBlank()
        ) {
            throw new IllegalArgumentException(
                    message
            );
        }

        return value.trim();
    }

    @PrePersist
    private void prePersist() {
        OffsetDateTime now =
                OffsetDateTime.now();

        if (createdAt == null) {
            createdAt = now;
        }

        if (updatedAt == null) {
            updatedAt = now;
        }

        if (active == null) {
            active = true;
        }

        if (role == null) {
            role = AdminRole.ADMIN;
        }
    }

    @PreUpdate
    private void preUpdate() {
        updatedAt = OffsetDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public AdminRole getRole() {
        return role;
    }

    public Boolean getActive() {
        return active;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public OffsetDateTime getLastLoginAt() {
        return lastLoginAt;
    }
}