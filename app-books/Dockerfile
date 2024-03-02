# docker build -t app-books .

FROM eclipse-temurin:17.0.10_7-jre-alpine
RUN mkdir /app
WORKDIR /app
COPY build/libs/app-books.jar /app/app.jar
CMD ["java", "-jar", "app.jar"]

