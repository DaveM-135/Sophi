FROM tomcat:9.0.8-jre8-alpine
VOLUME /tmp
COPY target/Sophi-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/Sophi-0.0.1-SNAPSHOT.war
EXPOSE 8080
ENTRYPOINT [ "sh", "-c", "java -Dspring.profiles.active=docker -Djava.security.egd=file:/dev/./urandom -jar /usr/local/tomcat/webapps/Sophi-0.0.1-SNAPSHOT.war" ]