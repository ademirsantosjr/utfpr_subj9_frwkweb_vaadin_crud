package org.vaadin.example;

import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.vaadin.example.model.Person;
import org.vaadin.example.model.PersonRepository;

/**
 * The entry point of the Spring Boot application.
 */
@SpringBootApplication
public class Application extends SpringBootServletInitializer {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner demo(PersonRepository personRepository) {
        return (args) -> {
            log.info("======== CREATE Janaina =========");
            Person person1 = new Person(
                "Janaina Bach",
                LocalDate.parse("1975-01-20"),
                "038.676.560-02",
                "bach@gmail.com",
                "Engenheira"
            );
            personRepository.save(person1);

            log.info("======== CREATE Pedro =========");
            Person person2 = new Person(
                "Pedro Macedo",
                LocalDate.parse("2001-10-15"),
                "700.726.260-03",
                "macedo@yahoo.com",
                "Professor"
            );
            personRepository.save(person2);
        };
    }

}
