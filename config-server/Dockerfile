FROM eclipse-temurin:17-jdk-alpine
COPY ./target/config-server-0.0.1.jar app.jar
EXPOSE 8888
ENTRYPOINT [ "java", "-jar", "app.jar" ]