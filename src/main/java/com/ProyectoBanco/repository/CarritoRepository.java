package com.ProyectoBanco.repository;

import com.ProyectoBanco.domain.CarritoItem;
import com.ProyectoBanco.domain.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CarritoRepository extends JpaRepository<CarritoItem, Long> {
    List<CarritoItem> findByCliente(Cliente cliente);
    int countByCliente(Cliente cliente);
}
