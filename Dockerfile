# Use the Azul Zulu OpenJDK 21.0.2 image as the base
FROM azul/zulu-openjdk-debian:21.0.2

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven project file
COPY pom.xml .

# Download and cache the dependencies
RUN apt-get update && apt-get install -y maven
RUN mvn dependency:go-offline -B

# Copy the entire project
COPY . .

# Build the application
RUN mvn package -DskipTests

# Expose the port your application runs on (replace 8080 if your app runs on a different port)
EXPOSE 8080

# Set the entry point to run the JAR file
ENTRYPOINT ["java", "-jar", "target/todo_app-0.0.1-SNAPSHOT.jar"]

