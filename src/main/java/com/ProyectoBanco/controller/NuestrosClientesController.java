package com.ProyectoBanco.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/nuestros_clientes")
public class NuestrosClientesController {

    @GetMapping("/nuestrosClientes")
    public String mostrarNuestrosClientes() {
        return "nuestros_clientes/nuestrosClientes";
    }
}
