package com.ProyectoBanco.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/contactenos")
public class ContactenosController {

    @GetMapping("/contactenos")
    public String mostrarContactenos() {
        return "contactenos/contactenos";
    }
}
