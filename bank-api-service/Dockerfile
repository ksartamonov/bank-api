FROM openjdk:18
WORKDIR /bank-api-service-docker
ARG JAR_FILE=target/bank-api-service-1.0-SNAPSHOT-exec.jar
COPY ${JAR_FILE} .
EXPOSE 8082
ENTRYPOINT java -jar "bank-api-service-1.0-SNAPSHOT-exec.jar"