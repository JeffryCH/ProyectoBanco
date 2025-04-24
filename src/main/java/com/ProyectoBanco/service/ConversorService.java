package com.ProyectoBanco.service;

import com.ProyectoBanco.domain.Conversor;
import com.ProyectoBanco.repository.ConversorRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author kcalm
 */
@Service
public class ConversorService {

    @Autowired
    private ConversorRepository conversorRepository;

 
    public BigDecimal calcularMontoConvertido(BigDecimal monto, Conversor.Moneda monedaOrigen, 
            Conversor.Moneda monedaDestino) {
        // Obtén la tasa de conversión de tu repositorio
        Optional<Conversor> conversor = conversorRepository
                .findByMonedaOrigenAndMonedaDestino(monedaOrigen, monedaDestino);

        if (conversor.isEmpty()) {
            throw new IllegalArgumentException("No se encontró la tasa de cambio para las monedas especificadas.");
        }

        // Realiza el cálculo de la conversión
        return monto.multiply(conversor.get().getTasaConversion());
    }

    public List<Conversor> obtenerTasasDeConversion() {
        return conversorRepository.findAll(); // Obtiene todas las tasas desde la base de datos
    }

}
