package com.ProyectoBanco.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SucursalesController {
    @GetMapping("/sucursales")
    public String mostrarSucursales() {
        return "sucursales/sucursales";
    }
}
