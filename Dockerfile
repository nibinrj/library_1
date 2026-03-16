        # Use a base image with Java installed (e.g., OpenJDK)
        FROM openjdk:17-jdk-alpine

        # Set the working directory inside the container
        WORKDIR /app

        # Copy the executable JAR file into the container
        COPY target/libray_1-0.0.1-SNAPSHOT.jar /app/app.jar

        # Expose the port your Spring Boot application listens on (e.g., 8080)
        EXPOSE 8080

        # Define the command to run the application when the container starts
        CMD ["java", "-jar", "app.jar"]