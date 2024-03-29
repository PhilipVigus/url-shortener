
#
# Build stage
#
FROM maven:3.8.5-openjdk-18 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

#
# Package stage
#
FROM maven:3.8.5-openjdk-18
COPY --from=build /home/app/target/url-shortener-0.0.1-SNAPSHOT.jar /usr/local/lib/url-shortener-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/url-shortener-0.0.1-SNAPSHOT.jar"]
