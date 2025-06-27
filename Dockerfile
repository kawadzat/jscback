## Use a multi-stage build for caching dependencies
FROM maven:3.8.8-eclipse-temurin-21-alpine AS build
WORKDIR /app
# copy the pom.xml file to the container
COPY . .
#show logs when installing dependencies
RUN mvn -T 4 clean package -DskipTests

FROM eclipse-temurin:21-alpine
WORKDIR /app
COPY --from=build /app /app
EXPOSE 9090
ENV SERVER_PORT=9090
ENV SPRING_PROFILES_ACTIVE=dev
# Command to run the application
CMD ["java", "--enable-preview", "-jar", "target/jscbackend.jar"]
