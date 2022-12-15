FROM openjdk:8
WORKDIR /home
COPY Sophi.jar ./
CMD java -jar Sophi.jar