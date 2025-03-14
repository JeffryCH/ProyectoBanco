package com.ProyectoBanco.controller;

import com.ProyectoBanco.domain.CarritoItem;
import com.ProyectoBanco.domain.Cliente;
import com.ProyectoBanco.domain.Producto;
import com.ProyectoBanco.service.CarritoService;
import com.ProyectoBanco.service.ProductoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/carrito")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @Autowired
    private ProductoService productoService;

    @PostMapping("/agregar")
    @ResponseBody
    public ResponseEntity<?> agregarAlCarrito(@RequestBody Map<String, Object> payload, HttpSession session) {
        Cliente cliente = (Cliente) session.getAttribute("clienteLogueado");
        if (cliente == null) {
            return ResponseEntity.badRequest().body("Debe iniciar sesión para agregar productos al carrito");
        }

        Long productoId = Long.parseLong(payload.get("productoId").toString());
        int cantidad = Integer.parseInt(payload.get("cantidad").toString());
        
        Producto producto = productoService.obtenerProductoPorId(productoId);
        if (producto == null) {
            return ResponseEntity.badRequest().body("Producto no encontrado");
        }

        carritoService.agregarAlCarrito(cliente, producto, cantidad);
        int totalItems = carritoService.obtenerCantidadItems(cliente);
        
        return ResponseEntity.ok().body(Map.of("totalItems", totalItems));
    }

    @GetMapping("/ver")
    public String verCarrito(Model model, HttpSession session) {
        Cliente cliente = (Cliente) session.getAttribute("clienteLogueado");
        if (cliente == null) {
            return "redirect:/iniciosesion/login";
        }

        List<CarritoItem> items = carritoService.obtenerCarrito(cliente);
        model.addAttribute("items", items);
        return "carrito/ver";
    }

    @DeleteMapping("/eliminar/{id}")
    @ResponseBody
    public ResponseEntity<?> eliminarItem(@PathVariable Long id, HttpSession session) {
        Cliente cliente = (Cliente) session.getAttribute("clienteLogueado");
        if (cliente == null) {
            return ResponseEntity.badRequest().body("Sesión no válida");
        }

        carritoService.eliminarItem(id);
        int totalItems = carritoService.obtenerCantidadItems(cliente);
        return ResponseEntity.ok().body(Map.of("totalItems", totalItems));
    }

    @DeleteMapping("/vaciar")
    @ResponseBody
    public ResponseEntity<?> vaciarCarrito(HttpSession session) {
        Cliente cliente = (Cliente) session.getAttribute("clienteLogueado");
        if (cliente == null) {
            return ResponseEntity.badRequest().body("Sesión no válida");
        }

        carritoService.vaciarCarrito(cliente);
        return ResponseEntity.ok().body(Map.of("totalItems", 0));
    }
}
