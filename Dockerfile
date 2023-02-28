###################################################
# BANK-API-SERVICE

# BUILD SECTION
FROM maven:3.8.7-openjdk-18 as build
COPY bank-api-service/src /home/app/src
COPY bank-api-service/pom.xml /home/app/
RUN mvn -f /home/app/pom.xml clean install

# PACKAGE SECTION
FROM openjdk:18-alpine
COPY --from=build /home/app/target/*.jar /app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "/app.jar"]
###################################################


