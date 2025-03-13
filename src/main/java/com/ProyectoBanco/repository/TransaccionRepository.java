package com.ProyectoBanco.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ProyectoBanco.domain.*;
import java.util.*;
import org.springframework.stereotype.Repository;

@Repository
public interface TransaccionRepository extends JpaRepository<Transaccion, Long> {
    List<Transaccion> findByCuenta_Cliente_IdCliente(Long idCliente);
}

