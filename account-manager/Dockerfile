FROM eclipse-temurin:17-jdk-alpine
COPY ./target/account-manager-0.0.1.jar app.jar
EXPOSE 9090
ENV CONFIG_SERVER_HOST=config-server
ENTRYPOINT [ "java", "-jar", "app.jar" ]