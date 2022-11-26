FROM gradle:7.5.1-jdk11-alpine as build-jar

WORKDIR /tmp

COPY . /tmp

RUN gradle bootJar


FROM openjdk:11.0.16-jdk

WORKDIR /var/www

COPY  --from=build-jar /tmp/build/libs/*.jar spring-boot.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "spring-boot.jar"]