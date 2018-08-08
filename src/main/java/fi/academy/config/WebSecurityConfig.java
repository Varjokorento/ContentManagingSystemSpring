package fi.academy.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    CustomizeAuthenticationSuccessHandler customizeAuthenticationSuccessHandler;

    @Bean
    public UserDetailsService mongoUserDetails() {
        return new CustomUserDetailsService();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        UserDetailsService userDetailsService = mongoUserDetails();
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/","/login").access("hasAnyAuthority('ADMIN', 'USER' )").anyRequest().permitAll()
                .and().formLogin().loginPage("/login").failureUrl("/error").usernameParameter("username").passwordParameter("password").defaultSuccessUrl("/")
                .permitAll().and().logout().permitAll();
        http.exceptionHandling().accessDeniedPage("/login");
    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        System.out.println("hei");
//        http
//                .authorizeRequests()
//                .antMatchers("/").permitAll()
//                .antMatchers("login").permitAll()
//                .antMatchers("/signup").permitAll().anyRequest()
//                .authenticated().and().csrf().disable().formLogin().successHandler(customizeAuthenticationSuccessHandler)
//                .loginPage("login").failureUrl("/error")
//                .usernameParameter("username")
//                .passwordParameter("password")
//                .and().logout()
//                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//                .logoutSuccessUrl("/").and().exceptionHandling();
//
////        http
////                .authorizeRequests()
////                .antMatchers("/").permitAll()
////                .antMatchers("login").permitAll()
////                .antMatchers("/signup").permitAll().anyRequest()
////                .authenticated().and().csrf().disable().formLogin().successHandler(customizeAuthenticationSuccessHandler)
////                .loginPage("login").failureUrl("/error")
////                .usernameParameter("username")
////                .passwordParameter("password")
////                .and().logout()
////                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
////                .logoutSuccessUrl("/").and().exceptionHandling();
//    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.
                ignoring()
                .antMatchers("/resources/**");

    }


}
