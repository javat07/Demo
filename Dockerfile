FROM eclipse-temurin:17-jdk

# Install Maven
RUN apt-get update && apt-get install -y maven

WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn -ntp dependency:go-offline

# Copy the source code and build
COPY . .
RUN mvn -ntp package -DskipTests

# Expose port (if your Spring Boot app runs on 8080)
EXPOSE 8080

# Run the compiled jar
CMD ["java", "-jar", "target/demo-0.0.1-SNAPSHOT.jar"]
