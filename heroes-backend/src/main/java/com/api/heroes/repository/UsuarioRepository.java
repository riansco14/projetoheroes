package com.api.heroes.repository;

import com.api.heroes.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    @Query("SELECT h FROM Usuario h WHERE h.login = :loginParam")
    Optional<Usuario> findByLogin(String loginParam);
}