CREATE TABLE admin_users (
                             id BIGSERIAL PRIMARY KEY,

                             name VARCHAR(120) NOT NULL,

                             username VARCHAR(80) NOT NULL,

                             password_hash VARCHAR(255) NOT NULL,

                             role VARCHAR(30) NOT NULL DEFAULT 'ADMIN',

                             active BOOLEAN NOT NULL DEFAULT TRUE,

                             created_at TIMESTAMP WITH TIME ZONE NOT NULL,

                             updated_at TIMESTAMP WITH TIME ZONE NOT NULL,

                             last_login_at TIMESTAMP WITH TIME ZONE,

                             CONSTRAINT uk_admin_users_username
                                 UNIQUE (username),

                             CONSTRAINT ck_admin_users_role
                                 CHECK (
                                     role IN (
                                              'ADMIN',
                                              'MANAGER',
                                              'ATTENDANT'
                                         )
                                     )
);

CREATE INDEX idx_admin_users_active
    ON admin_users (active);

CREATE INDEX idx_admin_users_username_active
    ON admin_users (username, active);