package br.com.fiap.rbac.config;

import java.io.IOException;

import br.com.fiap.rbac.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/h2-console/**").permitAll() // Permitir acesso ao console H2
                        .requestMatchers("/public/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/user/**").hasRole("USER")
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .successHandler(customAuthenticationSuccessHandler()) // Define o redirecionamento pós-login
                )
                .logout(logout -> logout.permitAll());

        // Desativa a proteção de frameOptions para permitir que o console H2 funcione
        // corretamente,
        // já que ele utiliza frames para exibir o conteúdo.
        
        // código original no vídeo, indicado como depreciado. 
        // http.headers(headers -> headers.frameOptions().disable());
        
        // novo código, atualizado em agosto/2025
        http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));

        // Desabilitar CSRF no console H2, necessário para que o H2 funcione
        // corretamente
        http.csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"));

        return http.build();
    }

    // Configuração de um AuthenticationSuccessHandler para redirecionar usuários
    // com
    // base na role
    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        // Retorna uma nova instância de AuthenticationSuccessHandler anônimo
        return new AuthenticationSuccessHandler() {
            // Método que será executado quando a autenticação for bem-sucedida
            @Override
            public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                    Authentication authentication) throws IOException, ServletException {
                // Verifica se o usuário autenticado possui a role "ROLE_ADMIN"
                if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                    // Redireciona o usuário com a role "ROLE_ADMIN" para a página "/admin"
                    response.sendRedirect("/admin");
                    // Verifica se o usuário possui a role "ROLE_USER"
                } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))) {
                    // Redireciona o usuário com a role "ROLE_USER" para a página "/user"
                    response.sendRedirect("/user");
                    // Caso o usuário não tenha uma role específica definida
                } else {
                    // Redireciona o usuário para a raiz "/" como fallback padrão
                    response.sendRedirect("/");
                }
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PlainTextPasswordEncoder();
    }

    public static class PlainTextPasswordEncoder implements PasswordEncoder {
        @Override
        public String encode(CharSequence rawPassword) {
            return rawPassword.toString(); // Retorna a senha como está, sem criptografia
        }

        @Override
        public boolean matches(CharSequence rawPassword, String encodedPassword) {
            return rawPassword.toString().equals(encodedPassword); // Compara diretamente a senha
        }
    }

    // código original no vídeo, indicado como depreciado.     
    // @Bean
    // public DaoAuthenticationProvider authenticationProvider() {
    //     DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    //     authProvider.setUserDetailsService(userDetailsService); // Define o UserDetailsService personalizado
    //     authProvider.setPasswordEncoder(passwordEncoder()); // Define o codificador de senha sem criptografia
    //     return authProvider;
    // }

    // novo código, atualizado em agosto/2025    
    @Bean
    public DaoAuthenticationProvider authenticationProvider(CustomUserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);        
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

}
