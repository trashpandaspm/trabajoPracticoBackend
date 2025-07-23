# Etapa 1: Construcción con Maven usando una imagen con Java 21
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2: Ejecución con un JRE 21 ligero
FROM eclipse-temurin:21-jre
WORKDIR /app

# Argumento para recibir el puerto desde docker-compose.yml
ARG SERVER_PORT=8080
ENV SERVER_PORT=${SERVER_PORT}

COPY --from=build /app/target/*.jar app.jar

# Expone el puerto que se le pase como variable
EXPOSE ${SERVER_PORT}
ENTRYPOINT ["java", "-jar", "app.jar"]
