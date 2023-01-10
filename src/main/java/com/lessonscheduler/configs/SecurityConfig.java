package com.lessonscheduler.configs;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.lessonscheduler.security.JwtAuthEntryPoint;
import com.lessonscheduler.security.JwtAuthenticationFilter;

import com.lessonscheduler.Constants;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

   @Autowired
   private JwtAuthEntryPoint jwtAuthEntryPoint;


    @Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        /*
         CORS must be processed before Spring Security comes into action since preflight 
         requests will not contain cookies and Spring Security will reject the request as 
         it will determine that the user is not authenticated.
         */
		http
            .cors().configurationSource(corsConfigurationSource())
            .and()
			.csrf().disable()
            .exceptionHandling()
            .authenticationEntryPoint(jwtAuthEntryPoint)
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeHttpRequests()
            .requestMatchers("/login", "/signup", "/home", "/v3/**", "/swagger-ui/**").permitAll()
            .requestMatchers("/admin").hasAnyRole("ADMIN")
            .anyRequest().authenticated()
            .and()
            .httpBasic();

            http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
            

		return http.build();
	}   

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(Constants.CORS_URLS));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("Content-Type", "Authorization")); // Cors blocks the request without it.
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
