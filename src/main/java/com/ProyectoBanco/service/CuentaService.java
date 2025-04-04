
package com.ProyectoBanco.service;

import com.ProyectoBanco.repository.*;
import com.ProyectoBanco.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class CuentaService {
    @Autowired
    private CuentaRepository cuentaRepository;

    public List<Cuenta> obtenerCuentasPorCliente(Long idCliente) {
        return cuentaRepository.findByCliente_IdCliente(idCliente);
    }
    
    // Método para obtener una cuenta por su ID
    public Optional<Cuenta> obtenerCuentaPorId(Long idCuenta) {
        return cuentaRepository.findById(idCuenta);
    }

    // Método para obtener una cuenta por su número
    public Optional<Cuenta> obtenerCuentaPorNumero(String numeroCuenta) {
        return cuentaRepository.findByNumeroCuenta(numeroCuenta);
    }

    // Método para actualizar una cuenta
    public void actualizarCuenta(Cuenta cuenta) {
        cuentaRepository.save(cuenta);
    }
}
