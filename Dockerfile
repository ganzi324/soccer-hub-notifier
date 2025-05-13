# Build stage
FROM eclipse-temurin:21-alpine AS build

WORKDIR /app

COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew build -x test --no-daemon

# Runtime stage
FROM eclipse-temurin:21-alpine

WORKDIR /app

# Copy only the built JAR from the build stage
COPY --from=build /app/build/libs/*.jar ./app.jar

# Use a non-root user for security
RUN addgroup -S travelmate && adduser -S travelmateuser -G travelmate
USER travelmateuser

CMD ["java", "-jar", "app.jar"]
