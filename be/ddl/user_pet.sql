CREATE TABLE USER_PET
(
    id      INTEGER PRIMARY KEY AUTO_INCREMENT, -- 기본 키, 자동 증가
    user_id INTEGER      NOT NULL,              -- 사용자 아이디
    pet_id  INTEGER      NOT NULL,              -- 반려동물 아이디
    role    VARCHAR(255) NOT NULL              -- 사용자 역할 (admin, guest)
);
