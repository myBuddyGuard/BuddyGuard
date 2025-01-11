CREATE TABLE WEIGHT_RECORDS (
                                id INTEGER PRIMARY KEY AUTO_INCREMENT,  -- 기본 키, 자동 증가
                                pet_id INTEGER NOT NULL,                -- 반려동물 ID (외래 키)
                                created_at DATE NOT NULL,               -- 기록 생성 날짜
                                weight DOUBLE NOT NULL                 -- 체중
);
