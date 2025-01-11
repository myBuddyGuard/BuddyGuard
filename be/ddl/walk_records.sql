CREATE TABLE WALK_RECORD (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             start_date DATE NOT NULL,
                             end_date DATE NOT NULL,
                             start_time VARCHAR(255) NOT NULL,
                             end_time VARCHAR(255) NOT NULL,
                             total_time VARCHAR(255) NOT NULL,
                             note VARCHAR(255) NOT NULL,
                             center_position_id BIGINT,
                             map_level INT NOT NULL,
                             path_image_id BIGINT,
                             distance DOUBLE(10,3) NOT NULL
);
