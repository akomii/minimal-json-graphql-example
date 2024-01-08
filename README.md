## minimal-json-graphql-example ![Java 17](https://img.shields.io/badge/Java-17-green) ![Spring Boot 3.2.0](https://img.shields.io/badge/Spring--Boot-3.2.0-green)

This Java application is a GraphQL server built using Spring Boot. It provides functionality to manage authors and books in a book store. The application uses Spring Data Redis for data persistence and integrates GraphQL for querying and mutating data.

### Deployment

#### Prerequisites

1. [Java Development Kit (JDK) 17](https://jdk.java.net/17/) - Download and install the JDK.
2. [Apache Maven](https://maven.apache.org/download.cgi) - Download and install the Maven build tool.
3. [Redis](https://redis.io//) - Ensure that a Redis Database is set up and running.

#### Deployment Steps

1. **Download the project**: Obtain the project folder. This folder should contain the `pom.xml` file and the project's source code.

2. **Build the project**: Run the following command to build and package the project: `mvn clean package`. This command will download the required dependencies, compile the project, and package it into an executable JAR file. The final JAR file will be located in the directory `target`, and its name will be `graphql-1.0-SNAPSHOT.jar`.

3. **Run the application**: Execute the following command: `java -jar target/graphql-1.0-SNAPSHOT.jar`. This will start the Spring Boot application, and it will be accessible locally.

4. **Access the application**: Once the application is up and running, you can access the GraphQL API on `http://localhost:8080/graphql`. Ensure that a Redis Database is running on the default Port 6379. See `src/main/resources/graphql/schema.graphqls` for possible GraphQL queries and mutations.
