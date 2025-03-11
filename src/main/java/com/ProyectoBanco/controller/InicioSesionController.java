package com.ProyectoBanco.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/iniciosesion")
public class InicioSesionController {

    @GetMapping("/login")
    public String mostrarLogin() {
        return "iniciosesion/login";
    }

    @GetMapping("/registro")
    public String mostrarRegistro() {
        return "iniciosesion/formularioRegistroUsuario";
    }
}
