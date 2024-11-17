package com.example.shopmebe.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.RememberMeAuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {

    @Autowired
    private DataSource dataSource;

    @Bean
    public UserDetailsService userDetailsService() {
        return new ShopmeUserDetailsService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(authenticationProvider(), rememberMeAuthenticationProvider());
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> requests
                .requestMatchers("/images/**").permitAll()
                .requestMatchers("/static/js/**").permitAll()
                .requestMatchers("/webjars/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/v1/countries/**").permitAll()
                .requestMatchers("/users/**", "/settings/**", "/v1/states/**", "/v1/countries/**").hasAuthority("Admin")
                .requestMatchers("categories/**, brands/**").hasAnyAuthority("Admin", "Editor")

                .requestMatchers("/products/new", "/products/delete/**")
                .hasAnyAuthority("Admin", "Editor")

                .requestMatchers("/products/edit/**", "/products/save", "/products/check_unique")
                .hasAnyAuthority("Admin", "Editor", "Salesperson", "Shipper")

                .requestMatchers("/products", "/products/", "/products/detail/**", "/products/detail/**")
                .hasAnyAuthority("Admin", "Editor", "Salesperson", "Shipper")

                .requestMatchers("products/**").hasAnyAuthority("Admin", "Editor", "Salesperson", "Shipper")
                .requestMatchers("/login").permitAll()
                .anyRequest().authenticated()
        ).formLogin((form) -> form
                .loginPage("/login")
                .usernameParameter("email")
                .permitAll()
        ).rememberMe((remember) -> remember
                .tokenRepository(persistentTokenRepository())
                .key("springRocks")
        ).logout((logout) -> logout
                .logoutUrl("/logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll());

        http.authenticationManager(authenticationManager());

        return http.build();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepo = new JdbcTokenRepositoryImpl();
        tokenRepo.setDataSource(dataSource);
        return tokenRepo;
    }

    @Bean
    PersistentTokenBasedRememberMeServices rememberMeServices() {
        return new PersistentTokenBasedRememberMeServices("springRocks", userDetailsService(), persistentTokenRepository());
    }


    @Bean
    RememberMeAuthenticationProvider rememberMeAuthenticationProvider() {
        return new RememberMeAuthenticationProvider("springRocks");
    }
}
