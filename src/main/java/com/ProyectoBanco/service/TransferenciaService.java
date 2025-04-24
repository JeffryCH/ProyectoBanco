package com.ProyectoBanco.service;

import com.ProyectoBanco.repository.*;
import com.ProyectoBanco.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;


@Service
public class TransferenciaService {
    @Autowired
    private TransferenciaRepository transferenciaRepository;

    public List<Transferencia> obtenerTransferenciasPorUsuario(Long idCliente) {
        return transferenciaRepository.findByCuentaOrigen_Cliente_IdCliente(idCliente);
    }
    
    // Método para guardar la transferencia en la base de datos
    public void guardarTransferencia(Transferencia transferencia) {
        transferenciaRepository.save(transferencia);
    }
    
    
}
