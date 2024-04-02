## minimal-json-graphql-example ![Java 17](https://img.shields.io/badge/Java-17-green) ![Spring Boot 3.2.0](https://img.shields.io/badge/Spring--Boot-3.2.0-green)

This Spring Boot project serves as a demonstration platform for exploring GraphQL as a tool for querying and mutating data while experimenting with various persistence strategies. It implements a simple bookstore domain model with Author and Book entities.

### Modules

- **server**: Defines the core GraphQL API (schema, resolvers), the bookstore domain model (Author, Book), and establishes interfaces and testing tools for persistence.
- **redis**: Implements the `server` persistence interfaces using [Redis](https://redis.io//) for fast in-memory data storage.
- **filesystem**: Implements the `server` persistence interfaces with a custom filesystem-based approach with JSON serialization.
- **elasticsearch**: Implements the `server` persistence interfaces using [Elasticsearch](https://www.elastic.co/de/elasticsearch) for robust search and indexing capabilities.

### Deployment

#### Prerequisites

1. [Java Development Kit (JDK) 17](https://jdk.java.net/17/) - Download and install the JDK.
2. [Apache Maven](https://maven.apache.org/download.cgi) - Download and install the Maven build tool.
3. For backend choice:
   - [redis](https://redis.io//): A running Redis instance.
   - [elasticsearch](https://www.elastic.co/de/elasticsearch): A running Elasticsearch instance.
   - filesystem: No additional setup needed.

#### Deployment Steps

1. **Download the project**: Obtain the project folder. This folder should contain the `pom.xml` file and the project's source code.

2. **Choose your Backend**: Inside the project, navigate to your desired backend (either `redis`, `filesystem` or `elasticsearch`). Make sure, that the according requirement from the prerequisites is fulfilled.

3. **Build the project**: Run the following command to build and package the project: `mvn clean package`. This command will download the required dependencies, compile the project, and package it into an executable JAR file. The final JAR file will be located in the directory `target`, and its name will be `...-1.0-SNAPSHOT.jar`.

4. **Run the application**: Execute the following command: `java -jar ...-1.0-SNAPSHOT.jar`. This will start the Spring Boot application, and it will be accessible locally.

5. **Access the application**: Once the application is up and running, you can access the GraphQL API on `http://localhost:8080/graphql`. See `src/main/resources/graphql/schema.graphqls` for possible GraphQL queries and mutations.
