CREATE TABLE HOSPITAL_RECORDS (
                                  id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                  pet_id BIGINT NOT NULL,
                                  date DATETIME NOT NULL,
                                  title VARCHAR(255) NOT NULL,
                                  description TEXT NOT NULL
);
