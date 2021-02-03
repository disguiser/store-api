FROM openjdk:11.0.8-slim-buster
ADD api.jar api.jar
ENTRYPOINT ["java", "-jar", "api.jar", "--spring.profiles.active=prod"]
