package com.avella.sample.dockerfilejavaapp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;

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
