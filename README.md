# Dockerfile java app

## Goal

Create a Dockerfile to run a java app.

## Steps

### Step 1 : Create a simple endpoint

We create a simple endpoint `/data` that return some data.

```java
@SpringBootApplication
public class DockerfileJavaAppApplication {

    private final Flux<Person> allPerson = Flux.just(
            new Person("Anthony", "Paris"),
            new Person("Celia", "Paris"),
            new Person("Thomas", "Marseille")
    );

    @Bean
    RouterFunction<ServerResponse> route() {
        return RouterFunctions.route()
                .GET("/data", serverRequest -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                        .body(allPerson, Person.class))
                .build();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class Person {
        String name;
        String city;
    }

    public static void main(String[] args) {
        SpringApplication.run(DockerfileJavaAppApplication.class, args);
    }
}
```

### Step 2 : Create the Dockerfile

- `FROM openjdk:15` Specify base image
- `ARG JAR_FILE=target/*.jar` Create variable `JAR_FILE` referencing our `jar`file
- `COPY ${JAR_FILE} app.jar` Copy our jar file to the container and name it  `app.jar`
- `EXPOSE 8080` Expose our server port (It's optional, but it's good to see what port our app will be using)
- `ENTRYPOINT ["java", "-jar", "/app.jar"]` Launch our app when we start our container

```dockerfile
FROM openjdk:15
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### Step 3 : Build the image with the Dockerfile

-  `-t` Tag our image

```bash
docker build -t avella/dockerfile-java-app:v1 .
```

We can see our created image by running the following command
```bash
docker image ls
```

### Step 4 : Run the container

- `-d` Detached mode
- `-p` Publish port, forward incoming traffic from our local machine port 8080 to the container port 8080

```bash
docker run -d -p 8080:8080 --name dockerfile-java-app avella/dockerfile-java-app:v1
```

### Step 5 : Test our endpoint

Go to our endpoint `/data` on the port we forwarded `8080` we should see some data.

```json
[{"name":"Anthony","city":"Paris"},{"name":"Celia","city":"Paris"},{"name":"Thomas","city":"Marseille"}]
```