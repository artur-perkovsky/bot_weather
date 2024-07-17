     FROM eclipse-temurin:17-jdk AS base
     FROM debian:bullseye-slim

     ENV JAVA_HOME=/opt/java/openjdk
     COPY --from=base $JAVA_HOME $JAVA_HOME
     ENV PATH="${JAVA_HOME}/bin:${PATH}"

     RUN apt update -y \
       && apt upgrade -y \
       && apt install -y --no-install-recommends curl zip unzip \
       && apt autoremove -y \
       && apt clean cache \
       && rm -rf /var/cache/apt/ \
       && useradd --create-home user

     WORKDIR /home/user
     COPY projets\pay-4-content-bot\pay-4-content-bot\target\*.jar app.jar
     USER user

     EXPOSE 10000
     ENTRYPOINT ["java","-jar","app.jar"]