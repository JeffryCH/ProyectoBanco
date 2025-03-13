package com.ProyectoBanco.service;

import com.ProyectoBanco.repository.*;
import com.ProyectoBanco.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.hibernate.Hibernate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    
    public boolean autenticarUsuario(String email, String contrasena) {
        Optional<Cliente> clienteOptional = usuarioRepository.findByEmail(email);
        if (clienteOptional.isPresent()) {
            Cliente cliente = clienteOptional.get();
            // Comparación directa de contraseñas (no recomendado en producción)
            return contrasena.equals(cliente.getContraseña());
        }
        return false;
    }
    @Transactional
    public Cliente obtenerPorEmail(String email) {
        Optional<Cliente> clienteOptional = usuarioRepository.findByEmail(email); // Obtienes el Optional<Cliente>
        if (clienteOptional.isPresent()) { // Verificas si el valor está presente
            Cliente usuario = clienteOptional.get(); // Obtienes el Cliente
            Hibernate.initialize(usuario.getCuentas()); // Inicializas las colecciones
            Hibernate.initialize(usuario.getProductos()); // Asegúrate de cargar productos si es necesario
            return usuario;
        }
        return null; // Devuelves null si no se encuentra el cliente
        }
        
        //return usuarioRepository.findByEmail(email).orElse(null);
    }
