FROM jetty:9.4.50-jre8-alpine
COPY /target/Sophi.war /var/lib/jetty/webapps