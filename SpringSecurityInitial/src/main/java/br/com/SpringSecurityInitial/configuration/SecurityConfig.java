package br.com.SpringSecurityInitial.configuration;

import br.com.SpringSecurityInitial.service.MyUserDetailService;
import br.com.SpringSecurityInitial.support.encoder.PlainTextPasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final MyUserDetailService myUserDetailService;

    public SecurityConfig(MyUserDetailService myUserDetailService) {
        this.myUserDetailService = myUserDetailService; //injeta o serviço de usários
    }

    //configurando o gerenciador de autenticação
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration){
        return authenticationConfiguration.getAuthenticationManager(); //configuração padrão
    }

    @Bean
    public PlainTextPasswordEncoder passwordEncoder(){
        return new PlainTextPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);

        provider.setPasswordEncoder(passwordEncoder);

        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        //filtra os paths e aplica as oliticas de segurança
        http.authorizeHttpRequests(authorizeHttpRequests -> {
            authorizeHttpRequests.requestMatchers("/public").permitAll();
            authorizeHttpRequests.requestMatchers("/logout").permitAll();
            authorizeHttpRequests.anyRequest().authenticated();
        });
        http.formLogin(Customizer.withDefaults());
        return http.build();
    }
}
