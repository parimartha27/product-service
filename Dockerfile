FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY build/libs/product-service-0.0.1-SNAPSHOT.jar product-service.jar

EXPOSE 5000

ENTRYPOINT ["java","-jar","product-service.jar"]


