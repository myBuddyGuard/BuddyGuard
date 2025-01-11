CREATE TABLE PETS (
                      id INTEGER PRIMARY KEY AUTO_INCREMENT,   -- 기본 키, 자동 증가
                      name VARCHAR(255) NOT NULL,              -- 반려동물 이름
                      profile_images VARCHAR(255) NOT NULL,    -- 반려동물 사진
                      breed VARCHAR(255) NOT NULL,             -- 종류를 관리
                      birth DATE NOT NULL                      -- 태어난 날 설정
);
