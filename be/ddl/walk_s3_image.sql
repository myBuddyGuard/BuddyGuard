CREATE TABLE WALK_S3_IMAGE (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               image_url VARCHAR(1000) NOT NULL,
                               file_name VARCHAR(255) NOT NULL,
                               file_type VARCHAR(255) NOT NULL,
                               file_size BIGINT NOT NULL,
                               created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
