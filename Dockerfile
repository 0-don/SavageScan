FROM maven:3.8-amazoncorretto-17

COPY . .
RUN mvn clean install
EXPOSE 8080
CMD mvn spring-boot:run
