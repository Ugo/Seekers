# Seekers
Simple tool to compute average of prices. The project is composed of a front end, a back end (called DataConsumer) and a price generator (called DataProducer).


## Tools used for the project
- Java 1.8
- maven 3.3.9
- mongoDB 3.2.10

There are other dependencies used in the pom files (spring boot, jboss resteasy) but they don't need any installation, they will be downloaded at the first compile with maven.

## Build the project
### Front end
There is nothing particular to do here. I tested on the server lighttpd. There are only two files for the front end (index.html and callAverage.js). So it is quite straightforward.
The only remark here is that I configured the lighttpd server on port 9090, it has to be the same port with the value defined in the annotation CrossOrigin in the ServicesController class of the DataProducer otherwise it may not work. 

### Data Consumer
The data consumer part has been built on top of spring boot, so the project already contains an embedded server. There is then no need to deploy it in a server. There are 3 ways to start it.
- In eclipse, run Application class as Java application
- Start it using maven with the following command: `./mvnw spring-boot:run`
- Build the jar with the command `./mvnw clean package` and then run the jar with the command `java -jar target/DataConsumer-0.1-SNAPSHOT.jar` 

### Data Producer
Finally the data producer is a simple java program that can be run either directly from eclipse either from the jar once built. The project can be built using the command `mvn clean package` and then run with the command `java -jar target/DataProducer.jar`. The default interval between two prices is 1000ms (1sec), this value can be modified at runtime, just add the new value in the call. For example to reduce the interval to 500ms you should call `java -jar target/DataProducer.jar 500`.
