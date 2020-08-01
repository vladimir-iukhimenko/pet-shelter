package ru.pet.shelter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication
@Slf4j
public class ShelterApplication {

    public static void main(String[] args) {
        log.info("Application starts...");

        SpringApplication.run(ShelterApplication.class, args);
    }

}
