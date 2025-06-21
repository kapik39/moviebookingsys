package vn.jpringboot.cinemaBooking.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import vn.jpringboot.cinemaBooking.security.AuthEntryPointJwt;
import vn.jpringboot.cinemaBooking.security.AuthTokenFilter;
import vn.jpringboot.cinemaBooking.security.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private static final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthEntryPointJwt unauthorizedHandler;
    private final AuthTokenFilter authTokenFilter;

    public SecurityConfig(UserDetailsServiceImpl userDetailsService,
            AuthEntryPointJwt unauthorizedHandler,
            AuthTokenFilter authTokenFilter) {
        this.userDetailsService = userDetailsService;
        this.unauthorizedHandler = unauthorizedHandler;
        this.authTokenFilter = authTokenFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        logger.info("Configuring DaoAuthenticationProvider...");
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        logger.info("DaoAuthenticationProvider configured successfully.");
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        logger.info("AuthenticationManager configured successfully.");
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        logger.info("Configuring SecurityFilterChain...");
        http.csrf(csrf -> csrf.disable())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth.requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/swagger-ui/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll()
                        .requestMatchers("/swagger-resources/**").permitAll()
                        .requestMatchers("/swagger-resources").permitAll()
                        .requestMatchers("/webjars/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/movie/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/movie/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/movie/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/movie/**").authenticated()
                        .requestMatchers("/user/**").authenticated()
                        .requestMatchers("/theater/**").authenticated()
                        .requestMatchers("/screen/**").authenticated()
                        .requestMatchers("/booking/**").authenticated()
                        .requestMatchers("/showtime/**").authenticated()
                        .anyRequest().authenticated());

        http.authenticationProvider(authenticationProvider());
        logger.info("Adding AuthTokenFilter before UsernamePasswordAuthenticationFilter");
        http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);

        logger.info("SecurityFilterChain configured.");
        return http.build();
    }
}
