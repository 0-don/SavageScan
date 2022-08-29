FROM maven:3.8-amazoncorretto-17

COPY . .
RUN mvn clean install
CMD mvn spring-boot:run
CMD ["java", \
"-Dcom.sun.management.jmxremote", \
"-Dcom.sun.management.jmxremote.port=9010", \
"-Dcom.sun.management.jmxremote.local.only=false", \
"-Dcom.sun.management.jmxremote.authenticate=false", \
"-Dcom.sun.management.jmxremote.ssl=false"]
EXPOSE 9010