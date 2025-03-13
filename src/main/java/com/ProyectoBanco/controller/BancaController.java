

package com.ProyectoBanco.controller;

import com.ProyectoBanco.service.*;
import com.ProyectoBanco.domain.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.security.Principal;
import java.util.List;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/banca")
public class BancaController {
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private CuentaService cuentaService;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private TransaccionService transaccionService;

    @GetMapping("/portal")
    public String mostrarPortalBanca(Model model, HttpSession session) {
        //String emailUsuario = principal.getName();
        //Cliente usuario = usuarioService.obtenerPorEmail(emailUsuario);
        Cliente usuario = (Cliente) session.getAttribute("usuario");
        System.out.println("Usuario en sesión: " + usuario);
        model.addAttribute("nombreUsuario", usuario.getNombre());
        model.addAttribute("ultimaConexion", usuario.getUltimoAcceso());
        model.addAttribute("cuentas", cuentaService.obtenerCuentasPorCliente(usuario.getIdCliente()));
        model.addAttribute("servicios", productoService.obtenerTodosServicios());
        model.addAttribute("transacciones", transaccionService.obtenerTransaccionesPorUsuario(usuario.getIdCliente()));

        return "banca/portal";
    }
    
    @GetMapping("/cerrar-sesion")
    public String cerrarSesion(HttpSession session) {
        // Invalidar la sesión
        session.invalidate();
        
        // Redirigir a la página de inicio o login
        return "redirect:/iniciosesion/login";  // O donde quieras redirigir después de cerrar sesión
    }
    
}
