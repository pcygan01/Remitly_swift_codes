FROM maven:3.8.7-eclipse-temurin-17 AS build

WORKDIR /app

# Copy maven files first for better caching
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Copy the Excel file
COPY src/main/resources/Interns_2025_SWIFT_CODES.xlsx ./src/main/resources/Interns_2025_SWIFT_CODES.xlsx

# Build the application
RUN mvn package -DskipTests

# Runtime stage
FROM openjdk:17-slim

WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Set JAVA_OPTS environment variable with default value
ENV JAVA_OPTS=""

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]

# Expose port
EXPOSE 8080 