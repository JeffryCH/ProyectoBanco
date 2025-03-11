package com.ProyectoBanco.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/servicios")
public class ServiciosController {

    @GetMapping("")
    public String mostrarServicios() {
        return "servicios/nuestrosServicios";
    }

    @GetMapping("/ahorros")
    public String mostrarAhorros() {
        return "servicios/ahorro";
    }

    @GetMapping("/inversion-digital")
    public String mostrarInversionDigital() {
        return "servicios/inversionDigital";
    }

    @GetMapping("/credito")
    public String mostrarCredito() {
        return "servicios/credito";
    }

    @GetMapping("/cuentas")
    public String mostrarCuentas() {
        return "servicios/cuentas";
    }
}
