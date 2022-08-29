FROM maven:3.8-amazoncorretto-17

COPY . .
RUN mvn clean install
CMD mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=9010 -Dcom.sun.management.jmxremote.rmi.port=9010 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -Djava.rmi.server.hostname=localhost"'

EXPOSE 9010