# Pismo-Code-Assessment-v3.0

This application was developed with the goal to fulfill the requirements specified in the Pismo Code Assessment v.3.0 - Guidelines (Part I).

## Technologies used

* Java 25
* Spring Boot 4.0
* PostgreSQL database
* Flyway
* TestContainers
* JUnit
* Mockito
* Docker Compose

## How to run locally

As this application uses Docker to create the resources of the PostgreSQL database and the Flyway migrations, we must first execute the Docker Compose file, before starting the application itself.
There is also a Dockerfile inside the /service root folder, that also uses Java and Maven resources to run the application. 
So, first, we must understand the structure of the folders. To follow this guide, you must in the Pismo-Code-Assessment-v3.0/service folder.

### 1. Go to /local folder

```sh
cd ./service/local/
```

### 2. Run the docker compose file

```sh
docker-compose up -d
```

### 3. Run the dockerfile with the Java + Spring application

```sh
docker-compose up -d
```

### 4. Build the docker image

```sh
cd ../
docker build -t pismo-java-application .
```

### 5. Run the docker image in a container

```sh
docker run -p 8080:8080  -e DATABASE_STRING_CONNECTION="jdbc:postgresql://postgresql:5432/pismo" --network local-network --name pismo-java-container pismo-java-application
```

## Future mapped improvements

There are some features that could also be developed, but it would take some more time, which are:

* Authentication for the users with the respective account number (preferably with OAuth 2.0)
* Create a bulk strategy operation for transactions, using a resource like RabbitMQ or Kafka
* Cache operations for "operation type" entity, which does not contain many data and can easily be optimized by caching the select operation by ID and implementing caching evict strategies for creation, deletion and update operations.
* Also, there can be certain changes to the entities, with the goal to improve audit and LGPD specifications, such the identificiation of the timezone for the user, and the inclusion of "is_active" fields in the entities, so that no hard deletes are performed in the database.