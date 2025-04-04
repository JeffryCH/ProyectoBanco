package com.ProyectoBanco.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ProyectoBanco.domain.*;
import java.util.*;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferenciaRepository extends JpaRepository<Transferencia, Long> {
    List<Transferencia> findByCuentaOrigen_Cliente_IdCliente(Long idCliente);
}

