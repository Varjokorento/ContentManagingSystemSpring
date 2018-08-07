package fi.academy;

import fi.academy.models.Comment;
import fi.academy.models.Post;
import fi.academy.repositories.PostRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.RedirectViewControllerRegistration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class UltimatecmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(UltimatecmsApplication.class, args);
    }



    @Bean
    CommandLineRunner ajaohjelma(PostRepository repository) {
        return (args) -> {

            List<Comment> kommentit = new ArrayList<>();

            Comment comment = new Comment("Nickname", "Ihan kiva", "yeh");

            kommentit.add(comment);

            Post postcomment = new Post("Otsikko", "Testi testi", new java.util.Date(), kommentit);

            repository.save(postcomment);

        };

    }
}
