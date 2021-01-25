FROM openjdk:11.0.9.1-jdk
VOLUME /tmp
COPY build/libs/* app.jar
EXPOSE 8010
ENTRYPOINT ["java", "-jar", "/app.jar"]
