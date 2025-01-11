CREATE TABLE USER_TOKENS (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             user_id BIGINT NOT NULL,
                             fcm_token VARCHAR(255) NOT NULL
);