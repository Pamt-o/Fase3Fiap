package br.com.fiap.rbac.repository;

import java.util.Optional;

import br.com.fiap.rbac.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;



public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
}

