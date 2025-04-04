package com.ProyectoBanco.controller;

import com.ProyectoBanco.service.*;
import com.ProyectoBanco.domain.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Date;
import java.time.ZoneId;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Controller
@RequestMapping("/iniciosesion")
public class InicioSesionController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/login")
    public String mostrarLogin(HttpSession session) {
        // Verificar si ya hay un usuario en sesión
        if (session.getAttribute("clienteLogueado") != null) {
            // Si ya hay un usuario logueado, redirigir al portal
            return "redirect:/banca/portal";
        }
        return "iniciosesion/login";
    }

    @GetMapping("/registro")
    public String registrarse(){
        return "iniciosesion/formularioRegistroUsuario";
    }
            
    @Autowired
    private PasswordEncoder passwordEncoder;
    @PostMapping("/registrarse")
    public String registrarse(@RequestParam String nombre,
        @RequestParam String apellido,
        @RequestParam String correo,
        @RequestParam String telefono,
        @RequestParam String contrasena,
        @RequestParam String confirmarContrasena,
        @RequestParam String tipoCuenta,
        @RequestParam String fechaNacimiento,
        @RequestParam String identificacion,
        Model model) {
        
        if (!contrasena.equals(confirmarContrasena)) {
            model.addAttribute("error", "Las contraseñas no coinciden.");
            return "iniciosesion/formularioRegistroUsuario";
        }
        //Crear usuario
        String usuarioGenerado = (nombre + apellido.charAt(0)).toLowerCase();
        //Fomato para la fecha de nacimiento
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(fechaNacimiento, formatter);
        Date fecha = java.sql.Date.valueOf(localDate);
        //Añadir atrinitos
        Cliente cliente = new Cliente();
        cliente.setNombre(nombre);
        cliente.setApellido(apellido);
        cliente.setEmail(correo);
        cliente.setTelefono(telefono);
        cliente.setContraseña(passwordEncoder.encode(contrasena)); // Idealmente se debe hashear
        cliente.setUsuario(usuarioGenerado); 
        cliente.setFechaNacimiento(fecha);
        cliente.setIdentificacion(identificacion);

        usuarioService.registrarClienteConCuenta(cliente);
        return "iniciosesion/registroExitoso";
    }

    
    @GetMapping("/loginError")
    public String mostrarLoginError() {
        return "iniciosesion/loginError";
    }

    @PostMapping("/autenticar")
    public String login(@RequestParam String email, @RequestParam String contrasena, Model model, HttpSession session) {
        Cliente usuario = usuarioService.obtenerPorEmail(email);

        if (usuario != null && usuarioService.autenticarUsuario(email, contrasena)) {
            // Actualizar último acceso
            usuario.setUltimoAcceso(new Date());
            usuarioService.guardarUsuario(usuario);

            // Guardar el usuario en sesión con el mismo nombre que usamos en la vista
            session.setAttribute("clienteLogueado", usuario);

            // Redirigir a la página principal, por ejemplo, el dashboard del usuario.
            return "redirect:/banca/portal";
        } else {
            // Enviar mensaje de error al formulario de login
            model.addAttribute("error", "Correo o contraseña incorrectos.");
            return "redirect:/iniciosesion/loginError";
        }
    }

    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        // Invalidar la sesión
        session.invalidate();
        return "redirect:/iniciosesion/login";
    }
}
