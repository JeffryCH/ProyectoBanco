package com.ProyectoBanco.controller;

import com.ProyectoBanco.domain.Noticia;
import com.ProyectoBanco.service.NoticiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class NoticiasController {
    @Autowired
    private NoticiaService noticiaService;

    @GetMapping("/noticias/noticias")
    public String mostrarNoticias(Model model) {
        List<Noticia> noticias = noticiaService.listarNoticias();
        if (noticias == null || noticias.isEmpty()) {
            noticias = new java.util.ArrayList<>();
            noticias.add(new Noticia(
                "¡Lanzamos nuestra nueva App Móvil!",
                new java.util.Date(),
                "Ahora puedes gestionar tus cuentas y hacer transferencias desde cualquier lugar. Descarga la app desde Google Play o App Store.",
                "/IMG/APPMOVILBANCO.png"
            ));
            noticias.add(new Noticia(
                "Tips de Seguridad Bancaria",
                new java.util.Date(System.currentTimeMillis() - 3L * 24 * 60 * 60 * 1000),
                "Nunca compartas tu contraseña ni tus códigos de verificación. Recuerda que el banco nunca te los solicitará por teléfono ni correo.",
                "/IMG/SEGURIDADBANCARIA.png"
            ));
            noticias.add(new Noticia(
                "Nueva sucursal en tu ciudad",
                new java.util.Date(System.currentTimeMillis() - 7L * 24 * 60 * 60 * 1000),
                "Visítanos en nuestra nueva sucursal ubicada en el centro comercial Plaza Viva. ¡Te esperamos con grandes sorpresas!",
                "/IMG/NUEVASUCURSAL.png"
            ));
        }
        model.addAttribute("noticias", noticias);
        return "noticias/noticias";
    }
}
