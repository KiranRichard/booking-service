#INSTALLATION OF THE OPERATING SYSTEM
FROM eclipse-temurin:17-jdk
COPY target/booking-service-0.0.1-SNAPSHOT.jar booking-service.jar
EXPOSE 9092
ENTRYPOINT ["java","-jar","booking-service.jar"]