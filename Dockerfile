FROM openjdk:jre-alpine

ADD ./target/tea-alert-0.1.0-SNAPSHOT-standalone.jar tea-alert-standalone.jar

CMD ["java", "-jar", "tea-alert-standalone.jar"]