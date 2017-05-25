FROM tomcat:8.5-alpine

COPY wait-mysql.sh .

COPY ../../sources/business-rest/target/4TRest.war /usr/local/tomcat/webapps

EXPOSE 8000

EXPOSE 8009

CMD ["catalina.sh", "run"]


