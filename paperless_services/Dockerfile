# Use an official Maven image to build the project
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package

# Use an OpenJDK image to run the Spring Boot app
FROM openjdk:17-jdk-alpine
# needed for tess4j - start
RUN apk update
RUN apk add --no-cache tesseract-ocr
RUN mkdir -p /app/tessdata
ADD https://github.com/tesseract-ocr/tessdata_best/raw/main/eng.traineddata /app/tessdata/eng.traineddata
ADD https://github.com/tesseract-ocr/tessdata_best/raw/main/osd.traineddata /app/tessdata/osd.traineddata
# needed for tess4j - end
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]