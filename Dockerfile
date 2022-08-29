FROM maven:3.8-amazoncorretto-17

COPY . .
RUN mvn clean install
CMD mvn spring-boot:run

EXPOSE 9010