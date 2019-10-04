FROM mongo:4.2.0
FROM openjdk:11
ADD target/onibusPoa-v-1.0.jar app.jar
ENV JAVA_OPTS=""
EXPOSE 8080
ENTRYPOINT exec java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar