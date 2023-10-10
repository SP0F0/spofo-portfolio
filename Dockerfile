FROM openjdk:17-alpine

COPY ./build/libs/portfolioEntity-0.0.1-SNAPSHOT.jar /usr/src/myapp/
CMD java -jar /usr/src/myapp/portfolioEntity-0.0.1-SNAPSHOT.jar
