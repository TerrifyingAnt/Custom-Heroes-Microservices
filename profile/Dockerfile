# Start with a base image containing Java runtime
FROM amazoncorretto:17-alpine-jdk

# Create a directory
WORKDIR /app

# Copy all the files from the current directory to the image
COPY /profile/ .

# Grant execute permissions to the gradlew script
RUN chmod +x ./gradlew

# Run the Gradle wrapper to build the project
RUN ./gradlew clean build -x test

# Run the jar file
CMD ["java", "-jar", "./build/libs/profile-0.0.1-SNAPSHOT.jar"]