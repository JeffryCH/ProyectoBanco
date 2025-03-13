
package com.ProyectoBanco.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ProyectoBanco.domain.*;
import java.util.*;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
}
