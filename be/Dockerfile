# 베이스 이미지로 Amazon Corretto 17 사용
FROM amazoncorretto:17

# JAR 파일을 컨테이너 내부로 복사
# ARG JAR_FILE=build/libs/*.jar
COPY build/libs/myBuddyGuard-0.0.1-SNAPSHOT.jar /app.jar

# 컨테이너 내부에서 사용할 포트를 공개
EXPOSE 8080

# jar 파일 실행
ENTRYPOINT ["java","-jar","/app.jar"]
