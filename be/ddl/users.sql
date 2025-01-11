CREATE TABLE USERS
(
    id            INTEGER PRIMARY KEY AUTO_INCREMENT, -- 기본 키, 자동 증가
    email         VARCHAR(255) NOT NULL,              -- 사용자의 카카오 이메일
    name          VARCHAR(255) NOT NULL,              -- 기본값은 사용자의 카카오 이름 / 변경 가능
    profile_image VARCHAR(255) NOT NULL               -- 사용자의 카카오 프로필 사진
);
