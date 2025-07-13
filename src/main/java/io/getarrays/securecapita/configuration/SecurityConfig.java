package io.getarrays.securecapita.configuration;

import io.getarrays.securecapita.filter.CustomAuthorizationFilter;
import io.getarrays.securecapita.handler.CustomAccessDeniedHandler;
import io.getarrays.securecapita.handler.CustomAuthenticationEntryPoint;
import io.getarrays.securecapita.roles.prerunner.ROLE_AUTH;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

/**
 * @author Junior RT
 * @version 1.0
 * @license Get Arrays, LLC (https://getarrays.io)
 * @since 11/19/2022
 */
//code to return roles?
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor





public class SecurityConfig {
    private final BCryptPasswordEncoder encoder;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final UserDetailsService userDetailsService;
    private final CustomAuthorizationFilter customAuthorizationFilter;

    private static final String[] PUBLIC_URLS = {"/api/v1/jasper/pdf/asset","/user/verify/password/**","/user/reset/password",
            "/user/login/**", "/user/verify/code/**", "/user/register/**", "/user/resetpassword/**", "/user/verify/account/**",
            "/user/refresh/token/**","/api/v1/user/station/get", "/api/v1/user/station/getCount", "/StockItemRequest", "/user/image/**", "user/list/**", "io/getarrays/securecapita/assert/**", "newvehicle/**", "inspection/**", "inventory/**", "/purchaseRequisition/**", "/store/**", "/inspection/addtoassert/**", "product/**", "category/**", "stock/totalQuantity/**", "users/{userId}/**"};


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable().cors();
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        http.authorizeHttpRequests().requestMatchers(PUBLIC_URLS).permitAll();
        http.authorizeHttpRequests().requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll();
        http.authorizeHttpRequests().requestMatchers("/user/register/**","/api/v1/admin/roles/**").hasAnyAuthority(ROLE_AUTH.ASSIGN_ROLE.name());

        http.authorizeHttpRequests().requestMatchers("/user/delete/**","/user/users/**","/user/list/**","/user/update/**","/user/assignStationToUser/**","/api/v1/user/station/**").hasAnyAuthority(ROLE_AUTH.UPDATE_USER.name());
//        http.authorizeHttpRequests().requestMatchers(DELETE, "/purchaseRequisition/delete/**").hasAnyAuthority("DELETE:purchaseRequisition");
        http.authorizeHttpRequests().requestMatchers("/api/v1/products/create/**","/api/v1/products/delete/**").hasAnyAuthority(ROLE_AUTH.CREATE_PRODUCT.name());
        http.authorizeHttpRequests().requestMatchers("/api/v1/MailingList/create/**","/api/v1/mailinglist/delete/**").hasAnyAuthority(ROLE_AUTH.CREATE_MAILLIST.name());
        http.authorizeHttpRequests().requestMatchers("/station/check/**").hasAnyAuthority(ROLE_AUTH.CHECK_ASSET.name());
        http.authorizeHttpRequests().requestMatchers("/assert/confirm/**").hasAnyAuthority(ROLE_AUTH.CONFIRM_ASSET.name());
        http.authorizeHttpRequests().requestMatchers("/assert/movable/**").authenticated();

        http.authorizeHttpRequests().requestMatchers("/laptop/**").authenticated();
        http.authorizeHttpRequests().requestMatchers("/antivirus/**").permitAll();
        http.authorizeHttpRequests().requestMatchers("/assert/allaserts/**").authenticated();
http.authorizeHttpRequests().requestMatchers("/ITAsserthttp/**");
        http.authorizeHttpRequests().requestMatchers("/antivirus/**").permitAll();


        http.exceptionHandling().accessDeniedHandler(customAccessDeniedHandler).authenticationEntryPoint(customAuthenticationEntryPoint);
        http.authorizeHttpRequests().anyRequest().authenticated();
        http.addFilterBefore(customAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(encoder);
        return new ProviderManager(authProvider);
    }








    /* Documentations
    https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/web/authentication/UsernamePasswordAuthenticationFilter.html

    https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/authentication/AuthenticationManager.html

    https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/authentication/AuthenticationProvider.html

    https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/authentication/dao/DaoAuthenticationProvider.html

    https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/authentication/dao/AbstractUserDetailsAuthenticationProvider.html
    */
}
















