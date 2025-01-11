CREATE TABLE VACCINATION_RECORDS
(
    id               INTEGER PRIMARY KEY AUTO_INCREMENT, -- 기본 키, 자동 증가
    pet_id           INTEGER      NOT NULL,              -- 반려동물 ID (외래 키)
    vaccination_date DATE         NOT NULL,              -- 예방접종 날짜
    vaccination_name VARCHAR(255) NOT NULL,              -- 백신 이름
    description      VARCHAR(255) NOT NULL               -- 접종에 대한 설명
);
