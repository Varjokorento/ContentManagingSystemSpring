package fi.academy;

import fi.academy.models.Comment;
import fi.academy.models.Post;
import fi.academy.models.Role;
import fi.academy.repositories.PostRepository;
import fi.academy.repositories.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.RedirectViewControllerRegistration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class UltimatecmsApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(UltimatecmsApplication.class, args);


    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(UltimatecmsApplication.class);
    }



    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }



//    @Bean
//    CommandLineRunner init(RoleRepository roleRepository) {
//
////        roleRepository.deleteAll();
////        return (args) -> {
////           Role adminRole = roleRepository.findByRole("ADMIN");
////           if(adminRole == null) {
////               Role newAdminRole = new Role();
////               newAdminRole.setRole("ADMIN");
////               roleRepository.save(newAdminRole);
////           }
////           Role useRole = roleRepository.findByRole("USER");
////           if(useRole == null) {
////               Role newUserRole = new Role();
////               newUserRole.setRole("User");
////               roleRepository.save(newUserRole);
////           }
////        };
//
//    }
}
