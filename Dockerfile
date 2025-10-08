FROM openjdk:21-jdk-slim
WORKDIR /app
COPY . .
RUN apt-get update && apt-get install -y maven
RUN mvn clean package -DskipTests
EXPOSE 3000
CMD ["java", "-jar", "target/finservice-0.0.1-SNAPSHOT.jar"]
