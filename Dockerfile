FROM openjdk:17-slim-buster
ADD target/store-api*.jar api.jar
ENTRYPOINT ["java", "-jar", "api.jar", "--spring.profiles.active=prod"]