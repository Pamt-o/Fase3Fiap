package br.com.fiap.rbac.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.com.fiap.rbac.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
}