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

@Controller
@RequestMapping("/iniciosesion")
public class InicioSesionController {

    @Autowired
    private UsuarioService usuarioService;
    
    @GetMapping("/login")
    public String mostrarLogin() {
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
        Cliente usuario = usuarioService.obtenerPorEmail(email);;

        if (usuario != null && usuarioService.autenticarUsuario(email, contrasena)) {
            //Guardar el usuario
            session.setAttribute("usuario", usuario);
            // Redirigir a la página principal, por ejemplo, el dashboard del usuario.
            return "redirect:/banca/portal";
        } else {
            // Enviar mensaje de error al formulario de login
            model.addAttribute("error", "Correo o contraseña incorrectos.");
            return "redirect:/iniciosesion/loginError"; // El nombre del template para el login.
        }
    }
}
