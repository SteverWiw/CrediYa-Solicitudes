# Build
FROM gradle:8.11-jdk17-alpine AS builder
WORKDIR /app

COPY . .

# Compilar el servicio app-service
RUN gradle :app-service:bootJar --no-daemon -x validateStructure

# Runtime
FROM gcr.io/distroless/java17-debian12:nonroot
WORKDIR /app

# Copiamos el jar generado en app-service
COPY --from=builder /app/applications/app-service/build/libs/*.jar app.jar

# Puerto
EXPOSE 8081

# Nombre del servicio
ENV SPRING_APPLICATION_NAME=loan-service

# Ejecutamos
ENTRYPOINT ["java", "-jar", "app.jar"]
