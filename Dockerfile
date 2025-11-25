# Etapa 1: build
FROM maven:3.8.6-jdk-17 AS build
WORKDIR /workspace
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: empacotar e rodar
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /workspace/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]