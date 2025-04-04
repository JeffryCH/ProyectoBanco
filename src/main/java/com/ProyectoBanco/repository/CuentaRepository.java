package com.ProyectoBanco.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ProyectoBanco.domain.*;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
    List<Cuenta> findByCliente_IdCliente(Long idCliente);
    Optional<Cuenta> findByNumeroCuenta(String numeroCuenta);
    boolean existsByNumeroCuenta(String numeroCuenta);
}
