package com.ProyectoBanco.service;

import com.ProyectoBanco.repository.*;
import com.ProyectoBanco.domain.*;
import com.ProyectoBanco.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.hibernate.Hibernate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.Date;

import org.springframework.security.crypto.password.PasswordEncoder;
import java.math.BigDecimal;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    public boolean autenticarUsuario(String email, String contrasena) {
    Optional<Cliente> clienteOptional = usuarioRepository.findByEmail(email);
    if (clienteOptional.isPresent()) {
        Cliente cliente = clienteOptional.get();
        String hash = cliente.getContraseña();
        // Si la contraseña guardada parece texto plano (no es BCrypt)
        if (hash != null && !hash.startsWith("$2a$")) {
            // Comparar directamente
            if (contrasena.equals(hash)) {
                // Migrar a hash seguro
                String nuevoHash = passwordEncoder.encode(contrasena);
                cliente.setContraseña(nuevoHash);
                cliente.setUltimoAcceso(new Date());
                usuarioRepository.save(cliente);
                return true;
            }
        }
        // Comparación normal con hash
        if (hash != null && passwordEncoder.matches(contrasena, hash)) {
            cliente.setUltimoAcceso(new Date());
            usuarioRepository.save(cliente);
            return true;
        }
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

    @Transactional
    public void guardarUsuario(Cliente cliente) {
        // Actualizar el ultimo acceso antes de guardar
        cliente.setUltimoAcceso(new Date());
        usuarioRepository.save(cliente);
    }
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    public boolean cambiarContrasena(String email, String contrasenaActual, String nuevaContrasena) {
        // Buscar el usuario por su email
        Optional<Cliente> usuarioOpt = usuarioRepository.findByEmail(email);
        if (usuarioOpt.isPresent()) {
            Cliente cliente = usuarioOpt.get();
            System.out.println("SE RECIBE EL SIGUIENTE USUARIO: "+ cliente.getNombre() + " contraseña: " + cliente.getContraseña());
            // Verificar si la contraseña actual es correcta
            
            if (!passwordEncoder.matches(contrasenaActual, cliente.getContraseña())) {
                return false; // La contraseña actual no coincide
            }
             // Cambiar la contraseña del usuario
            cliente.setContraseña(passwordEncoder.encode(nuevaContrasena));
            usuarioRepository.save(cliente);
            return true; // Contraseña actualizada correctamente
        }
        System.out.println("EL USUARIO NO EXISTE");
        return false; // No se encontró el usuario
    }
    
    @Autowired
    private CuentaRepository cuentaRepository;
    public Cliente registrarClienteConCuenta(Cliente cliente) {
        cliente.setFechaRegistro(new Date());
        cliente.setUltimoAcceso(new Date());

        // Guardar cliente primero
        Cliente clienteGuardado = usuarioRepository.save(cliente);

        // Crear cuenta asociada
        Cuenta cuenta = new Cuenta();
        cuenta.setCliente(clienteGuardado);
        cuenta.setSaldo(new BigDecimal("0.00")); 
        cuenta.setTipoCuenta("Ahorro");
        cuenta.setFechaApertura(new Date());
        cuenta.setNumeroCuenta(generarNumeroCuentaUnico()); // genera un número único

        cuentaRepository.save(cuenta);

        return clienteGuardado;
    }

    private String generarNumeroCuentaUnico() {
        String numero;
        do {
            numero = String.valueOf((long)(Math.random() * 1_000_000_000L));
        } while (cuentaRepository.existsByNumeroCuenta(numero));
        return numero;
    }
}
