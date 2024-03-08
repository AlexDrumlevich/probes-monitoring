FROM openjdk:17-alpine
COPY ./target/avg-reducer-0.0.1.jar app.jar
ENV CONFIG_SERVER_HOST=config-server
ENTRYPOINT [ "java", "-jar", "app.jar" ]
