FROM amazoncorretto:21

WORKDIR /app
COPY target/user-management-0.0.1.jar /app/user-management.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/user-management.jar"]
