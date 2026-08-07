package br.com.fiap.rbac.service;



import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;


import br.com.fiap.rbac.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.fiap.rbac.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Busca o usuário pelo nome de usuário
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        // Converte as roles do usuário para SimpleGrantedAuthority
        var authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());

        // Retorna uma implementação do UserDetails do Spring Security com os dados do usuário
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),      // habilitado
                true,                  // conta não expirada
                true,                  // credenciais não expiradas
                true,                  // conta não bloqueada
                authorities
        );
    }
}

