# base image
FROM openjdk:11-slim as build

# label
LABEL maintainer="Robby Quintiens <robby.quintiens@student.pxl.be>"

WORKDIR application

# app jar-file
ARG JAR_FILE=target/*.jar

# copy app jar to container
COPY ${JAR_FILE} application.jar

RUN java -Djarmode=layertools -jar application.jar extract


# Wait Tool
ENV WAIT_VERSION 2.7.2
ADD https://github.com/ufoscout/docker-compose-wait/releases/download/$WAIT_VERSION/wait /wait
RUN chmod +x /wait

# STAGE 2
FROM openjdk:11-slim
WORKDIR application
COPY --from=build application/dependencies/ ./
COPY --from=build application/spring-boot-loader/ ./
COPY --from=build application/snapshot-dependencies/ ./
COPY --from=build application/application/ ./
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]