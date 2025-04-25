package com.ProyectoBanco.controller;

import com.ProyectoBanco.domain.Conversor;
import com.ProyectoBanco.service.ConversorService;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author kcalm
 */
@Controller
@RequestMapping("/conversor")
public class ConversorController {

    // --- AJAX endpoint para conversión en tiempo real ---
    @PostMapping("/convertirAjax")
    @ResponseBody
    public java.util.Map<String, String> convertirMonedaAjax(
            @RequestParam java.math.BigDecimal monto,
            @RequestParam Conversor.Moneda monedaOrigen,
            @RequestParam Conversor.Moneda monedaDestino) {
        java.util.Map<String, String> response = new java.util.HashMap<>();
        try {
            java.math.BigDecimal montoConvertido = conversorService.calcularMontoConvertido(monto, monedaOrigen, monedaDestino);
            response.put("resultado", String.format("%.2f %s son %.2f %s.", monto, monedaOrigen, montoConvertido, monedaDestino));
        } catch (IllegalArgumentException e) {
            response.put("resultado", "Error: " + e.getMessage());
        }
        return response;
    }


    @Autowired
    private ConversorService conversorService;

    
    @GetMapping("/convertire")
    public ResponseEntity<String> convertirMoneda(
            @RequestParam BigDecimal monto,
            @RequestParam Conversor.Moneda monedaOrigen,
            @RequestParam Conversor.Moneda monedaDestino) {

        try {
            BigDecimal montoConvertido = conversorService.calcularMontoConvertido(monto, monedaOrigen, monedaDestino);
            return ResponseEntity.ok(String.format("%.2f %s son %.2f %s.",
                    monto, monedaOrigen, montoConvertido, monedaDestino));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/formulario")
    public String mostrarFormulario(Model model, jakarta.servlet.http.HttpSession session) {
        // Si hay cliente logueado, redirigir al portal
        if (session != null && session.getAttribute("clienteLogueado") != null) {
            return "redirect:/banca/portal";
        }
        // Agregar valores iniciales al modelo para la vista
        var listaConversores  = conversorService.obtenerTasasDeConversion();
        model.addAttribute("listas", listaConversores );

        model.addAttribute("monto", BigDecimal.ZERO);
        model.addAttribute("monedaOrigen", null);
        model.addAttribute("monedaDestino", null);
        model.addAttribute("resultado", null);
        return "conversor/formulario";
    }

    @PostMapping("/convertir")
    public String convertirMoneda(
            @RequestParam BigDecimal monto,
            @RequestParam Conversor.Moneda monedaOrigen,
            @RequestParam Conversor.Moneda monedaDestino,
            Model model) {

        try {
            BigDecimal montoConvertido = conversorService.calcularMontoConvertido(monto, monedaOrigen, monedaDestino);
            model.addAttribute("resultado", String.format("%.2f %s son %.2f %s.",
                    monto, monedaOrigen, montoConvertido, monedaDestino));
        } catch (IllegalArgumentException e) {
            model.addAttribute("resultado", "Error: " + e.getMessage());
        }

        // Pasar los datos nuevamente al formulario
        var listaConversores  = conversorService.obtenerTasasDeConversion();
        model.addAttribute("listas", listaConversores );
        model.addAttribute("monto", monto);
        model.addAttribute("monedaOrigen", monedaOrigen);
        model.addAttribute("monedaDestino", monedaDestino);

        return "conversor/formulario";
    }
}
