FROM eclipse-temurin:17-jdk-alpine
COPY ./target/email-provider-0.0.1.jar app.jar
EXPOSE 8284
ENV CONFIG_SERVER_HOST=config-server
ENTRYPOINT [ "java", "-jar", "app.jar" ]