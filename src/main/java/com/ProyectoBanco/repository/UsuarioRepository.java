package com.ProyectoBanco.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ProyectoBanco.domain.Cliente;
import java.util.*;

public interface UsuarioRepository extends JpaRepository<Cliente, Integer> {
    Optional<Cliente> findByEmail(String email);
}
