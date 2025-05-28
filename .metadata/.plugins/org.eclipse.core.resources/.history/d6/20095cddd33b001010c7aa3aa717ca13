FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY target/proyecto-0.0.1-SNAPSHOT.jar app_proyecto.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app_proyecto.jar"]
