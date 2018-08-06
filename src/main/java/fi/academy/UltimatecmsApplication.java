package fi.academy;

import fi.academy.models.Post;
import fi.academy.repositories.PostRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.sql.Date;

@SpringBootApplication
public class UltimatecmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(UltimatecmsApplication.class, args);
    }


    @Bean
    CommandLineRunner ajaohjelma(PostRepository repository) {
        return (args) -> {



        };

    }
}
