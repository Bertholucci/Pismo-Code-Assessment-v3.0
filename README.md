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