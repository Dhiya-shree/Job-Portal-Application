package com.mvc.jobportal.config;

import com.mvc.jobportal.services.user.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    private final CustomUserDetailsService userDetailsService;
    private final AuthenticationSuccess authenticationSuccess;


    public SecurityConfig(CustomUserDetailsService userDetailsService, AuthenticationSuccess authenticationSuccess) {
        this.userDetailsService = userDetailsService;
        this.authenticationSuccess = authenticationSuccess;
    }

    private final String[] publicUrl={"/","/global-search",
    "/register","/webjars/**","/register/**","/resources/**"
    ,"/assets/**","/css/**","/summernote/**","/js/**",
    "/*.css","/*.js","/*.js.map"
    ,"/fonts**","/favicon.ico","/resources/**","/error"};


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationManager(){
        DaoAuthenticationProvider authenticationProvider=new DaoAuthenticationProvider(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;

    }


    @Bean
    protected SecurityFilterChain securityFilterChain(HttpSecurity http){
        http.authenticationProvider(authenticationManager());
        http.authorizeHttpRequests(auth->{
            auth.requestMatchers(publicUrl).permitAll();
            auth.anyRequest().authenticated();
        });

        http.formLogin(form->
                form.loginPage("/login").permitAll()
                        .successHandler(authenticationSuccess));
        http.logout(logout->
        {
            logout.logoutUrl("/logout");
            logout.logoutSuccessUrl("/");
        });
        http.cors(Customizer.withDefaults());
        http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
}
