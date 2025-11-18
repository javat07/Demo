# Use Java 17 runtime
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copy jar file (make sure the filename matches your jar)
COPY C:/Users/trios/Downloads/demo/target/demo-0.0.1-SNAPSHOT.jar


EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
