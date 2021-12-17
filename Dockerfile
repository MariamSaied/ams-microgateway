FROM store/oracle/serverjre:1.8.0_241-b07
EXPOSE 7090
ADD target/*.jar /home/java/app.jar
ENTRYPOINT ["java","-jar","/home/java/app.jar"]
