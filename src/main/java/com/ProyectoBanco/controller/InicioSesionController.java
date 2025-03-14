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
    public String mostrarRegistro() {
        return "iniciosesion/formularioRegistroUsuario";
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
