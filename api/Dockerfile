# ---- Etapa de build ----
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Cachear dependencias antes de copiar el código fuente
COPY gradlew build.gradle settings.gradle ./
COPY gradle ./gradle
RUN chmod +x gradlew && ./gradlew dependencies --no-daemon || true

COPY src ./src
RUN ./gradlew clean bootJar --no-daemon -x test

# ---- Etapa de ejecución ----
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring
USER spring

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
