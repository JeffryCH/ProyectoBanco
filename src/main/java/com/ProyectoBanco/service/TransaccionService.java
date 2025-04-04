package com.ProyectoBanco.service;

import com.ProyectoBanco.repository.*;
import com.ProyectoBanco.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;


@Service
public class TransaccionService {
    @Autowired
    private TransaccionRepository transaccionRepository;

    public List<Transaccion> obtenerTransaccionesPorUsuario(Long idCliente) {
        return transaccionRepository.findByCuenta_Cliente_IdCliente(idCliente);
    }
    
}
