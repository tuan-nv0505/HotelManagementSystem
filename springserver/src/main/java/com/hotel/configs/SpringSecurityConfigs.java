package com.hotel.configs;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
@EnableTransactionManagement
@ComponentScan(
        basePackages = {
                "com.hotel"
        }
)
@Order(2)
public class SpringSecurityConfigs {
    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public HandlerMappingIntrospector mvcHandlerMappingIntrospector() {
        return new HandlerMappingIntrospector();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.csrf(c -> c.disable())
//                .authorizeHttpRequests((requests) -> requests
//                        .requestMatchers("/admin").hasRole("ADMIN")
//                        .requestMatchers("/api/**").permitAll()
//                        .requestMatchers("/websocket/**").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .formLogin(form -> form.loginPage("/admin/login")
//                        .loginProcessingUrl("/admin/login")
//                        .defaultSuccessUrl("/admin/", true)
//                        .failureUrl("/admin/login?error=true")
//                        .permitAll()
//                )
//                .logout((logout) -> logout.logoutSuccessUrl("/admin/login").permitAll());

        http.securityMatcher("/admin/**", "/", "/login")
                .csrf(c -> c.disable())
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .requestMatchers("/api/**").permitAll()
                        .requestMatchers("/websocket/**").permitAll()
                        .anyRequest().authenticated()
                ).formLogin(form -> form.loginPage("/admin/login")
                        .loginProcessingUrl("/admin/login")
                        .defaultSuccessUrl("/admin/", true)
                        .failureUrl("/admin/login?error=true")
                        .permitAll()
                ).logout((logout) -> logout.logoutSuccessUrl("/admin/login").permitAll());

        return http.build();
    }

    @Bean
    public Cloudinary cloudinary() {
        Cloudinary cloudinary
                = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dt1pa28g2",
                "api_key", "824465552867193",
                "api_secret", "A9MAKfzfQok2sZCjtIuhsDBTzis",
                "secure", true));
        return cloudinary;
    }
}
