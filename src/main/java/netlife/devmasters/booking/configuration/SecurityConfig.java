package netlife.devmasters.booking.configuration;


import netlife.devmasters.booking.service.UserService;

import netlife.devmasters.booking.util.JwtAccesoDenegadoHandler;

import netlife.devmasters.booking.util.JwtAutenticacionEntryPoint;
import netlife.devmasters.booking.util.JwtFiltroAutorizacionFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static netlife.devmasters.booking.constant.SecurityConst.URLS_PUBLICS;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
//This means the project needs a authentication to access the endpoints
@EnableWebSecurity
//roles and permissions
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig {
    @Autowired
    private JwtFiltroAutorizacionFilter jwtAuthorizationFilter;

    private JwtAccesoDenegadoHandler jwtAccessDeniedHandler;
    private JwtAutenticacionEntryPoint jwtAuthenticationEntryPoint;

    private UserDetailsService userDetailsService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public SecurityConfig(
            JwtFiltroAutorizacionFilter jwtAuthorizationFilter,
                         JwtAccesoDenegadoHandler jwtAccessDeniedHandler,
                       JwtAutenticacionEntryPoint jwtAuthenticationEntryPoint,
            @Qualifier("userDetailsService") UserDetailsService userDetailsService,
            BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.jwtAuthorizationFilter = jwtAuthorizationFilter;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, UserService userDetailService)
            throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder)
                .and()
                .build();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //Disable CSRF that its not necesary in api rest staeless and enable CORS from other sites. Since we are using JWT, CSRF is not needed.
        http.cors().and().csrf().disable().cors()
                //Set stateless configuration for the session, don't use http session
                .and().sessionManagement().sessionCreationPolicy(STATELESS)
                //filter authorization
                .and().authorizeHttpRequests().requestMatchers(URLS_PUBLICS).permitAll()
                .anyRequest().authenticated()
                .and().exceptionHandling().accessDeniedHandler(jwtAccessDeniedHandler)
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .and()
                //its execute first
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    


}