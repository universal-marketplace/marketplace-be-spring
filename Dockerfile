FROM openjdk:25-ea-jdk-slim as builder
WORKDIR /app
COPY target/*.jar app.jar

FROM openjdk:25-ea-jdk-slim
WORKDIR /app
COPY --from=builder /app/app.jar ./
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
