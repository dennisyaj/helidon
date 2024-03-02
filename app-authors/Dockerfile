# docker build -t app-authors .

FROM eclipse-temurin:17.0.10_7-jre-alpine
RUN mkdir /app
WORKDIR /app
COPY build/libs/app-authors.jar /app/app.jar
CMD ["java", "-jar", "app.jar"]

