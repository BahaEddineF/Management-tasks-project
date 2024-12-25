# Use a base image for Spring Boot application
FROM openjdk:21

# Set working directory inside the container
WORKDIR /app

# Copy the jar file to the container
COPY target/security-0.0.1-SNAPSHOT.jar /app/app.jar

# Expose the port your Spring Boot app will run on
EXPOSE 8080

# Command to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]
