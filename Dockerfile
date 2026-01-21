# Java 21 base image (MATCHES pom.xml)
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copy Maven wrapper and pom first (layer caching)
COPY .mvn .mvn
COPY mvnw .
COPY pom.xml .

RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline

# Copy source
COPY src src

# Build jar
RUN ./mvnw clean package -DskipTests

EXPOSE 8080

CMD ["java", "-jar", "target/FinanceTracker-0.0.1-SNAPSHOT.jar"]
