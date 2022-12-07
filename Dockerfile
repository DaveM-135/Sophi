FROM openjdk:8
WORKDIR /home
ADD Sophi-0.0.1-SNAPSHOT.jar ./
EXPOSE 8080
CMD java -jar Sophi-0.0.1-SNAPSHOT.jar