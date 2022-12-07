FROM openjdk:8
WORKDIR /home
COPY target/Sophi.jar ./
CMD java -jar Sophi.jar