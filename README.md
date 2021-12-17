# ams-microgateway

ams-microgateway is a microservice which has two REST APIs one for getting account details and the other for transferring specific amount between two accounts.

## Languages and technologies used

- Java 8
- Spring Boot 
- Maven
- SonarQube (static code analysis tool)
- Swagger (Documentation)
- Docker (Containerization)

## Implementation challenges
There are many approaches to handle data storage. Such as storing accounts in a database or hosted on some provider's storage. 

The challenge we are having here is having state-full microservice having accounts stored in a Map which has a key representing the account number and the value containing the account details. 

The challenge is to maintain the consistency of the accounts info while having multiple requests represented as multiple threads accessing singleton Dao class.

## Solutions proposed

In order maintain consistency across all the concurrent transaction over money transfer API and the API which gets the account info that waits if there's a concurrent money transfer API until it finish used:
 - ReentrantReadWriteLock is used to lock the blocks which access reading and writing operations so if there is a reading operation concurrent with writing operation it shall wait until the writing thread finish. Only one locker used over the map to save the memory while we could create lockers with the same numbers of the accounts we have to minimize the locking scope but it would absolutely be not memory efficient.

 - Synchronized method is used to lock the account object so whenever two threads accessing the same objects at writing time they shall wait in a queue but we won't maintain any reading operations.

## Enhancement ideas
- All the configuration shall be held externally in a file, cache or environment variable not statically such as error codes and messages
- Microservices should be stateless and all data certainly must  be stored or cached outside each service 
- Logging interceptor to be used for requests logging needed after for a search engine such as ELK

## Requirements

- Java 8
- Spring Boot 
- Maven

## Run

The microservice has a docker file so all what you need is to run in order the following commands while you are in the root folder of the project.

```bash
docker build -t ams-microgateway .
docker run -d -p 7090:7090 ams-microgateway
```

## API catalog

The repository contain a postman collection. API catalog via Swagger too.
