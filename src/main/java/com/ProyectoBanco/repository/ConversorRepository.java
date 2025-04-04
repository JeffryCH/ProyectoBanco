package com.ProyectoBanco.repository;

import com.ProyectoBanco.domain.Conversor;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author kcalm
 */
public interface ConversorRepository extends JpaRepository<Conversor, Long> {


    Optional<Conversor> findByMonedaOrigenAndMonedaDestino(Conversor.Moneda monedaOrigen, 
            Conversor.Moneda monedaDestino);
}


