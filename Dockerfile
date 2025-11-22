# ----- Stage 1: The Build Stage -----
# We use a full Maven & JDK image to build our app
FROM maven:3.9-eclipse-temurin-21 AS build

# Set the working directory
WORKDIR /app

# Copy the pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the rest of the source code and build the "fat jar"
COPY src ./src
RUN mvn clean package -DskipTests

# ----- Stage 2: The Run Stage -----
# We use a slim Java-only image to run the app
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

ARG JAR_FILE=target/MedSync-1.0-SNAPSHOT-jar-with-dependencies.jar

# Copy the built jar from the "build" stage
COPY --from=build /app/${JAR_FILE} ./app.jar

# Set environment variable for JavaFX
ENV DISPLAY=:0

# The command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]