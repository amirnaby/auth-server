package com.niam.authserver.config;

import com.niam.authserver.filter.JwtAuthFilter;
import com.niam.authserver.filter.LoginPostProcessingFilter;
import com.niam.authserver.service.JwtHandler;
import com.niam.authserver.web.exception.FilterChainExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final UserDetailsService userDetailsService;
    private final AuthenticationEntryPoint authEntryPoint;
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final LogoutSuccessHandler logoutSuccessHandler;
    private final SimpleUrlAuthenticationFailureHandler authenticationFailureHandler;
    private final JwtHandler jwtHandler;
    @Autowired
    private FilterChainExceptionHandler filterChainExceptionHandler;

    public SecurityConfig(UserDetailsService userDetailsService, LogoutSuccessHandler logoutSuccessHandler,
                          @Qualifier("customAuthenticationEntryPoint") AuthenticationEntryPoint authEntryPoint,
                          @Qualifier("handlerExceptionResolver") HandlerExceptionResolver handlerExceptionResolver,
                          SimpleUrlAuthenticationFailureHandler authenticationFailureHandler, JwtHandler jwtHandler) {
        this.userDetailsService = userDetailsService;
        this.authEntryPoint = authEntryPoint;
        this.handlerExceptionResolver = handlerExceptionResolver;
        this.logoutSuccessHandler = logoutSuccessHandler;
        this.authenticationFailureHandler = authenticationFailureHandler;
        this.jwtHandler = jwtHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(cors -> cors.configure(http))
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(filterChainExceptionHandler, LogoutFilter.class)
                .addFilterBefore(jwtAuthFilter(), AuthorizationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui", "/login*", "/resources/**", "/api-docs", "/captcha", "/refresh-token")
                        .permitAll()
                        .requestMatchers("/changePassword*", "/user/updatePassword*")
                        .hasRole("CHANGE_PASSWORD")
                        .requestMatchers("/invalidSession*")
                        .hasAnyRole("ANONYMOUS")
                        .requestMatchers("/users*", "/resetPassword*")
                        .hasRole("ADMIN")
                        .anyRequest()
                        .authenticated())
                .userDetailsService(userDetailsService)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authEntryPoint));
        return http.build();
    }

    private JwtAuthFilter jwtAuthFilter() {
        return new JwtAuthFilter(userDetailsService, jwtHandler, handlerExceptionResolver);
    }

    @Bean
    public FilterRegistrationBean<LoginPostProcessingFilter> loginPostProcessingFilterRegistration() {
        FilterRegistrationBean<LoginPostProcessingFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(loginPostProcessingFilter());
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public LoginPostProcessingFilter loginPostProcessingFilter() {
        return new LoginPostProcessingFilter(handlerExceptionResolver);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }
}