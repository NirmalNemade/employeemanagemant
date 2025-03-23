// // package com.example.demo.security;

// // import jakarta.servlet.ServletException;
// // import jakarta.servlet.http.HttpServletResponse;
// // import lombok.RequiredArgsConstructor;
// // import org.springframework.beans.factory.annotation.Autowired;
// // import org.springframework.beans.factory.annotation.Qualifier;
// // import org.springframework.context.annotation.Bean;
// // import org.springframework.context.annotation.Configuration;
// // import org.springframework.security.access.AccessDeniedException;
// // import org.springframework.security.authentication.AuthenticationManager;
// // import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
// // import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// // import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// // import org.springframework.security.config.http.SessionCreationPolicy;
// // import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// // import org.springframework.security.crypto.password.PasswordEncoder;
// // import org.springframework.security.web.SecurityFilterChain;
// // import org.springframework.security.web.access.AccessDeniedHandler;
// // import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// // import org.springframework.web.servlet.HandlerExceptionResolver;

// // import java.io.IOException;

// // @Configuration
// // @EnableWebSecurity
// // @RequiredArgsConstructor
// // public class WebSecurityConfig {

// //     private final JWTAuthFilter jwtAuthFilter;

// //     @Autowired
// //     @Qualifier("handlerExceptionResolver")
// //     private HandlerExceptionResolver handlerExceptionResolver;

// //     @Bean
// //     public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

// //         httpSecurity
// //                 .csrf(csrfConfig -> csrfConfig.disable())
// //                 .sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
// //                 .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
// //                 .authorizeHttpRequests(auth -> auth
// //                 .requestMatchers("/employees/**").hasAnyRole("CREATOR", "ADMIN")
// //                         .anyRequest().permitAll()
// //                 )
// //                 .exceptionHandling(exHandlingConfig -> exHandlingConfig.accessDeniedHandler(accessDeniedHandler()));

// //         return httpSecurity.build();
// //     }

// //     @Bean
// //     public PasswordEncoder passwordEncoder() {
// //         return new BCryptPasswordEncoder();
// //     }

// //     @Bean
// //     AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
// //         return configuration.getAuthenticationManager();
// //     }

// //     @Bean
// //     public AccessDeniedHandler accessDeniedHandler() {
// //         return (request, response, accessDeniedException) -> {
// //             handlerExceptionResolver.resolveException(request, response, null, accessDeniedException);
// //         };
// //     }

// // }

// package com.example.demo.security;

// import jakarta.servlet.ServletException;
// import jakarta.servlet.http.HttpServletResponse;
// import lombok.RequiredArgsConstructor;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Qualifier;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.access.AccessDeniedException;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.access.AccessDeniedHandler;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// import org.springframework.web.cors.CorsConfiguration;
// import org.springframework.web.cors.CorsConfigurationSource;
// import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
// import org.springframework.web.servlet.HandlerExceptionResolver;

// import java.io.IOException;
// import java.util.List;

// @Configuration
// @EnableWebSecurity
// @RequiredArgsConstructor
// public class WebSecurityConfig {

//     private final JWTAuthFilter jwtAuthFilter;

//     @Autowired
//     @Qualifier("handlerExceptionResolver")
//     private HandlerExceptionResolver handlerExceptionResolver;

//     @Bean
//     public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//         httpSecurity
//                 .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Add CORS configuration
//                 .csrf(csrfConfig -> csrfConfig.disable())
//                 .sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                 .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
//                 .authorizeHttpRequests(auth -> auth
//                         .requestMatchers("/api/auth/**").permitAll() // Allow public access to auth endpoints
//                         .requestMatchers("/**").permitAll() 
//                         .requestMatchers("/api/employees/**").hasAnyRole("CREATOR", "ADMIN") // Secure employee endpoints
//                         .anyRequest().authenticated() // Require authentication for other requests
//                 )
//                 .exceptionHandling(exHandlingConfig -> exHandlingConfig.accessDeniedHandler(accessDeniedHandler()));

//         return httpSecurity.build();
//     }

//     @Bean
//     public PasswordEncoder passwordEncoder() {
//         return new BCryptPasswordEncoder();
//     }

//     @Bean
//     public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
//         return configuration.getAuthenticationManager();
//     }

//     @Bean
//     public AccessDeniedHandler accessDeniedHandler() {
//         return (request, response, accessDeniedException) -> {
//             handlerExceptionResolver.resolveException(request, response, null, accessDeniedException);
//         };
//     }

//     @Bean
//     public CorsConfigurationSource corsConfigurationSource() {
//         CorsConfiguration configuration = new CorsConfiguration();
//         configuration.setAllowedOrigins(List.of("http://localhost:5173")); // Vue.js dev server
//         configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//         configuration.setAllowedHeaders(List.of("*"));
//         configuration.setAllowCredentials(true); // Allow cookies or Authorization header with JWT

//         UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//         source.registerCorsConfiguration("/**", configuration); // Apply to all endpoints
//         return source;
//     }
// }
package com.example.demo.security;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JWTAuthFilter jwtAuthFilter;

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver handlerExceptionResolver;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrfConfig -> csrfConfig.disable())
                .sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll() // Ensure login is public
                        .requestMatchers("/api/employees/**").hasAnyRole("CREATOR", "ADMIN")
                        .requestMatchers("/**").permitAll() 
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exHandlingConfig -> exHandlingConfig.accessDeniedHandler(accessDeniedHandler()));

        return httpSecurity.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            handlerExceptionResolver.resolveException(request, response, null, accessDeniedException);
        };
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:5173")); // Match your Vue.js port
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}