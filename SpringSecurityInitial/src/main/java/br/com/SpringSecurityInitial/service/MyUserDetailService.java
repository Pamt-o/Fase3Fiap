package br.com.SpringSecurityInitial.service;

import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MyUserDetailService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername (String username) throws UsernameNotFoundException{
        return new User("usuario", "senha", new ArrayList<>());
    }

}
