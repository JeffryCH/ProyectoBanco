package com.ProyectoBanco.controller;

import com.ProyectoBanco.service.*;
import com.ProyectoBanco.domain.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.http.ResponseEntity;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;
import java.util.Date;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Map;
import java.text.SimpleDateFormat;
import java.text.ParseException;

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
    
    @Autowired
    private TransferenciaService transferenciaService;

    @Autowired
    private ConversorService conversorService;

    @GetMapping("/portal")
    public String mostrarPortalBanca(Model model, HttpSession session) {
        Cliente usuario = (Cliente) session.getAttribute("clienteLogueado");
        if (usuario == null) {
            return "redirect:/iniciosesion/login"; // Redirigir si no hay usuario logueado
        }
        System.out.println("Usuario en sesión: " + usuario);
        model.addAttribute("nombreUsuario", usuario.getNombre());
        model.addAttribute("ultimaConexion", usuario.getUltimoAcceso());
        model.addAttribute("cuentas", cuentaService.obtenerCuentasPorCliente(usuario.getIdCliente()));
        model.addAttribute("servicios", productoService.obtenerTodosServicios());
        model.addAttribute("transacciones", transaccionService.obtenerTransaccionesPorUsuario(usuario.getIdCliente()));
        model.addAttribute("transferencias", transferenciaService.obtenerTransferenciasPorUsuario(usuario.getIdCliente()));
        // Agregar lista de monedas para el fragmento de tipo de cambio
        var listaConversores = conversorService.obtenerTasasDeConversion();
        model.addAttribute("listas", listaConversores);
        return "banca/portal";
    }

    @GetMapping("/cerrar-sesion")
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @PostMapping("/convertirMonedaDesdePortal")
    public String convertirMonedaDesdePortal(
            @RequestParam BigDecimal monto,
            @RequestParam Conversor.Moneda monedaOrigen,
            @RequestParam Conversor.Moneda monedaDestino,
            Model model,
            HttpSession session) {
        try {
            BigDecimal montoConvertido = conversorService.calcularMontoConvertido(monto, monedaOrigen, monedaDestino);
            model.addAttribute("resultadoConversion", String.format("%.2f %s son %.2f %s.",
                    monto, monedaOrigen, montoConvertido, monedaDestino));
        } catch (IllegalArgumentException e) {
            model.addAttribute("resultadoConversion", "Error: " + e.getMessage());
        }
        // Repoblar el modelo igual que en mostrarPortalBanca
        return mostrarPortalBanca(model, session);
    }

    @GetMapping("/perfil")
    public String mostrarPerfil() {
        return "banca/portal";
    }
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    @PostMapping("/cambiarContrasena")
    public Object cambiarContrasena(@RequestParam String contrasenaActual,
                                    @RequestParam String contrasenaNueva,
                                    @RequestParam String confirmarContrasena,
                                    HttpSession session,
                                    Model model,
                                    RedirectAttributes redirectAttributes,
                                    jakarta.servlet.http.HttpServletRequest request) {
    /// Obtener el email del usuario autenticado desde la sesión
        Cliente usuario = (Cliente) session.getAttribute("clienteLogueado");
        String emailUsuario = usuario.getEmail();
        boolean isAjax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
        System.out.println("BANCA CONTROLLER - email = "+emailUsuario);
        // Verificar si las contraseñas coinciden
        if (!contrasenaNueva.equals(confirmarContrasena)) {
            if (isAjax) {
                return ResponseEntity.ok(java.util.Collections.singletonMap("error", "Las contraseñas nuevas no coinciden."));
            }
            redirectAttributes.addFlashAttribute("errorMensaje", "Las contraseñas nuevas no coinciden.");
            return "redirect:/banca/portal";
        }
        boolean exito = usuarioService.cambiarContrasena(emailUsuario, contrasenaActual, contrasenaNueva);
        if (exito) {
            if (isAjax) {
                return ResponseEntity.ok(java.util.Collections.singletonMap("success", "Contraseña actualizada correctamente."));
            }
            redirectAttributes.addFlashAttribute("successMessage", "Contraseña actualizada correctamente.");
        } else {
            if (isAjax) {
                return ResponseEntity.ok(java.util.Collections.singletonMap("error", "La contraseña actual es incorrecta."));
            }
            redirectAttributes.addFlashAttribute("errorMensaje", "La contraseña actual es incorrecta.");
        }
        return "redirect:/banca/portal"; 
    }
    
    @GetMapping("/transferencia")
    public String mostrarTransferenciaForm(Model model, HttpSession session) {
        Cliente usuario = (Cliente) session.getAttribute("clienteLogueado");
        if (usuario == null) {
            return "redirect:/iniciosesion/login"; // Redirigir si no hay usuario logueado
        }

        List<Cuenta> cuentas = cuentaService.obtenerCuentasPorCliente(usuario.getIdCliente());
        model.addAttribute("cuentas", cuentas);
        return "banca/portal"; // El nombre de la vista donde se muestra el modal
    }
    
    @PostMapping("/realizarTransferencia")
    public Object realizarTransferencia(@RequestParam Long cuentaOrigen,
                                     @RequestParam String cuentaDestino,
                                     @RequestParam String descripcion,
                                     @RequestParam String monto, // El monto se recibe como String y luego se convierte a BigDecimal
                                     HttpSession session,
                                     Model model,
                                     HttpServletRequest request) {
        System.out.println("[TRANSFERENCIA] LLEGA A realizarTransferencia");
        System.out.println("[TRANSFERENCIA] Datos recibidos: cuentaOrigen=" + cuentaOrigen + ", cuentaDestino=" + cuentaDestino + ", descripcion=" + descripcion + ", monto=" + monto);

        // Obtener el usuario de la sesión
        boolean isAjax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
        Cliente usuario = (Cliente) session.getAttribute("clienteLogueado");
        if (usuario == null) {
            if (isAjax) {
                return java.util.Collections.singletonMap("redirect", "/iniciosesion/login");
            }
            return "redirect:/iniciosesion/login"; // Redirigir si no hay usuario logueado
        }

        // Verificar si la cuenta origen y destino son válidas
        Optional<Cuenta> cuentaOrigenOptional = cuentaService.obtenerCuentaPorId(cuentaOrigen);
        
        if (!cuentaOrigenOptional.isPresent()) {
    if (isAjax) return ResponseEntity.badRequest().body(java.util.Collections.singletonMap("error", "La cuenta de origen no existe."));
    // Solo para no-AJAX:
    model.addAttribute("errorMensaje", "La cuenta de origen no existe.");
    model.addAttribute("activarTransferencia", true);
    model.addAttribute("nombreUsuario", usuario.getNombre());
    model.addAttribute("ultimaConexion", usuario.getUltimoAcceso());
    model.addAttribute("cuentas", cuentaService.obtenerCuentasPorCliente(usuario.getIdCliente()));
    model.addAttribute("servicios", productoService.obtenerTodosServicios());
    model.addAttribute("transacciones", transaccionService.obtenerTransaccionesPorUsuario(usuario.getIdCliente()));
    model.addAttribute("transferencias", transferenciaService.obtenerTransferenciasPorUsuario(usuario.getIdCliente()));
    var listaConversores = conversorService.obtenerTasasDeConversion();
    model.addAttribute("listas", listaConversores);
    return "banca/portal";
}
        Optional<Cuenta> cuentaDestinoOptional = cuentaService.obtenerCuentaPorNumero(cuentaDestino);
        if (!cuentaDestinoOptional.isPresent()) {
    if (isAjax) return ResponseEntity.badRequest().body(java.util.Collections.singletonMap("error", "La cuenta destino no existe."));
    // Solo para no-AJAX:
    model.addAttribute("errorMensaje", "La cuenta destino no existe.");
    model.addAttribute("activarTransferencia", true);
    model.addAttribute("nombreUsuario", usuario.getNombre());
    model.addAttribute("ultimaConexion", usuario.getUltimoAcceso());
    model.addAttribute("cuentas", cuentaService.obtenerCuentasPorCliente(usuario.getIdCliente()));
    model.addAttribute("servicios", productoService.obtenerTodosServicios());
    model.addAttribute("transacciones", transaccionService.obtenerTransaccionesPorUsuario(usuario.getIdCliente()));
    model.addAttribute("transferencias", transferenciaService.obtenerTransferenciasPorUsuario(usuario.getIdCliente()));
    var listaConversores = conversorService.obtenerTasasDeConversion();
    model.addAttribute("listas", listaConversores);
    return "banca/portal";
}
        Cuenta cuentaOrigenObj = cuentaOrigenOptional.get();
        Cuenta cuentaDestinoObj = cuentaDestinoOptional.get();

        // Verificar si hay suficiente saldo en la cuenta origen
        BigDecimal montoTransferencia;
        try {
    montoTransferencia = new BigDecimal(monto);
} catch (NumberFormatException e) {
    if (isAjax) return ResponseEntity.badRequest().body(java.util.Collections.singletonMap("error", "El monto ingresado no es válido."));
    // Solo para no-AJAX:
    model.addAttribute("errorMensaje", "El monto ingresado no es válido.");
    model.addAttribute("activarTransferencia", true);
    model.addAttribute("nombreUsuario", usuario.getNombre());
    model.addAttribute("ultimaConexion", usuario.getUltimoAcceso());
    model.addAttribute("cuentas", cuentaService.obtenerCuentasPorCliente(usuario.getIdCliente()));
    model.addAttribute("servicios", productoService.obtenerTodosServicios());
    model.addAttribute("transacciones", transaccionService.obtenerTransaccionesPorUsuario(usuario.getIdCliente()));
    model.addAttribute("transferencias", transferenciaService.obtenerTransferenciasPorUsuario(usuario.getIdCliente()));
    var listaConversores = conversorService.obtenerTasasDeConversion();
    model.addAttribute("listas", listaConversores);
    return "banca/portal";
}
        // Validar monto positivo y mayor que cero
        if (montoTransferencia.compareTo(BigDecimal.ZERO) <= 0) {
    if (isAjax) return ResponseEntity.badRequest().body(java.util.Collections.singletonMap("error", "El monto debe ser mayor a cero para realizar una transferencia."));
    // Solo para no-AJAX:
    model.addAttribute("errorMensaje", "El monto debe ser mayor a cero para realizar una transferencia.");
    model.addAttribute("activarTransferencia", true);
    model.addAttribute("nombreUsuario", usuario.getNombre());
    model.addAttribute("ultimaConexion", usuario.getUltimoAcceso());
    model.addAttribute("cuentas", cuentaService.obtenerCuentasPorCliente(usuario.getIdCliente()));
    model.addAttribute("servicios", productoService.obtenerTodosServicios());
    model.addAttribute("transacciones", transaccionService.obtenerTransaccionesPorUsuario(usuario.getIdCliente()));
    model.addAttribute("transferencias", transferenciaService.obtenerTransferenciasPorUsuario(usuario.getIdCliente()));
    var listaConversores = conversorService.obtenerTasasDeConversion();
    model.addAttribute("listas", listaConversores);
    return "banca/portal";
}
        if (cuentaOrigenObj.getSaldo().compareTo(montoTransferencia) < 0) { // Verificar saldo suficiente
    if (isAjax) return ResponseEntity.badRequest().body(java.util.Collections.singletonMap("error", "Saldo insuficiente. Por favor, realice un depósito para poder transferir."));
    // Solo para no-AJAX:
    model.addAttribute("errorMensaje", "Saldo insuficiente. Por favor, realice un depósito para poder transferir.");
    model.addAttribute("activarTransferencia", true);
    model.addAttribute("nombreUsuario", usuario.getNombre());
    model.addAttribute("ultimaConexion", usuario.getUltimoAcceso());
    model.addAttribute("cuentas", cuentaService.obtenerCuentasPorCliente(usuario.getIdCliente()));
    model.addAttribute("servicios", productoService.obtenerTodosServicios());
    model.addAttribute("transacciones", transaccionService.obtenerTransaccionesPorUsuario(usuario.getIdCliente()));
    model.addAttribute("transferencias", transferenciaService.obtenerTransferenciasPorUsuario(usuario.getIdCliente()));
    var listaConversores = conversorService.obtenerTasasDeConversion();
    model.addAttribute("listas", listaConversores);
    return "banca/portal";
}

        // Realizar la transferencia
        System.out.println("[TRANSFERENCIA] Realizando descuento y abono en cuentas...");
        cuentaOrigenObj.setSaldo(cuentaOrigenObj.getSaldo().subtract(montoTransferencia)); // Descontamos el monto de la cuenta origen
        cuentaDestinoObj.setSaldo(cuentaDestinoObj.getSaldo().add(montoTransferencia)); // Agregamos el monto a la cuenta destino

        // Crear un registro de la transferencia
        Transferencia transferencia = new Transferencia();
        transferencia.setCuentaOrigen(cuentaOrigenObj);
        transferencia.setCuentaDestino(cuentaDestinoObj);
        transferencia.setMonto(montoTransferencia);
        transferencia.setConcepto(descripcion); // Asignar la descripción
        transferencia.setFechaTransferencia(new Date()); // Asignar la fecha actual

        try {
            // Guardar la transferencia y actualizar cuentas
            transferenciaService.guardarTransferencia(transferencia);
            System.out.println("[TRANSFERENCIA] Transferencia guardada: " + transferencia);
            cuentaService.actualizarCuenta(cuentaOrigenObj);
            cuentaService.actualizarCuenta(cuentaDestinoObj);
            System.out.println("[TRANSFERENCIA] Cuentas actualizadas. Consultando historial...");
            java.util.List<Transferencia> transferencias = transferenciaService.obtenerTransferenciasPorUsuario(usuario.getIdCliente());
            System.out.println("[TRANSFERENCIA] Transferencias encontradas (después de guardar): " + transferencias.size());
            if (isAjax) return ResponseEntity.ok(java.util.Collections.singletonMap("success", "Transferencia realizada con éxito."));
            // Solo para no-AJAX:
            model.addAttribute("successMessage", "Transferencia realizada con éxito.");
            model.addAttribute("activarTransferencia", true);
            model.addAttribute("nombreUsuario", usuario.getNombre());
            model.addAttribute("ultimaConexion", usuario.getUltimoAcceso());
            model.addAttribute("cuentas", cuentaService.obtenerCuentasPorCliente(usuario.getIdCliente()));
            model.addAttribute("servicios", productoService.obtenerTodosServicios());
            model.addAttribute("transacciones", transaccionService.obtenerTransaccionesPorUsuario(usuario.getIdCliente()));
            model.addAttribute("transferencias", transferencias);
            model.addAttribute("listas", conversorService.obtenerTasasDeConversion());
            return "banca/portal"; // Mantener en la pestaña de transferencia y mostrar historial actualizado
        } catch (Exception ex) {
            System.out.println("[TRANSFERENCIA][ERROR] " + ex.getMessage());
            ex.printStackTrace();
            if (isAjax) return ResponseEntity.badRequest().body(java.util.Collections.singletonMap("error", "Error inesperado al procesar la transferencia. Intente de nuevo más tarde."));
            model.addAttribute("errorMensaje", "Error inesperado al procesar la transferencia. Intente de nuevo más tarde.");
            model.addAttribute("activarTransferencia", true);
            model.addAttribute("nombreUsuario", usuario.getNombre());
            model.addAttribute("ultimaConexion", usuario.getUltimoAcceso());
            model.addAttribute("cuentas", cuentaService.obtenerCuentasPorCliente(usuario.getIdCliente()));
            model.addAttribute("servicios", productoService.obtenerTodosServicios());
            model.addAttribute("transacciones", transaccionService.obtenerTransaccionesPorUsuario(usuario.getIdCliente()));
            java.util.List<Transferencia> transferencias = transferenciaService.obtenerTransferenciasPorUsuario(usuario.getIdCliente());
            System.out.println("[TRANSFERENCIA][ERROR] Transferencias encontradas (en error): " + transferencias.size());
            model.addAttribute("transferencias", transferencias);
            model.addAttribute("listas", conversorService.obtenerTasasDeConversion());
            return "banca/portal";
        }
    }    
    //Método para actualizar la información personal del usuario loguueado
    @PostMapping("/actualizar-datos")
    public String actualizarDatos(
        @RequestParam String nombre,
        @RequestParam String apellido,
        @RequestParam String telefono,
        @RequestParam String email,
        @RequestParam String direccion,
        @RequestParam String fechaNacimiento,
        HttpSession session
    ) {
        Cliente cliente = (Cliente) session.getAttribute("clienteLogueado");

        if (cliente != null) {
            cliente.setNombre(nombre);
            cliente.setApellido(apellido);
            cliente.setTelefono(telefono);
            cliente.setEmail(email);
            cliente.setDireccion(direccion);

            try {
                // Parsear fechaNacimiento a Date
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date fecha = sdf.parse(fechaNacimiento);
                cliente.setFechaNacimiento(fecha);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            usuarioService.guardarUsuario(cliente);
            session.setAttribute("clienteLogueado", cliente);
        }

        return "redirect:/banca/portal";
    }

    // --- Endpoint AJAX funcional para conversión de moneda ---
    @PostMapping("/convertirMonedaAjaxPropio")
    @org.springframework.web.bind.annotation.ResponseBody
    public java.util.Map<String, String> convertirMonedaAjaxPropio(
            @RequestParam("monto") String montoStr,
            @RequestParam("monedaOrigen") String monedaOrigen,
            @RequestParam("monedaDestino") String monedaDestino) {
        java.util.Map<String, String> response = new java.util.HashMap<>();

        try {
            BigDecimal monto = new BigDecimal(montoStr);
            Conversor.Moneda monedaOrigenEnum = Conversor.Moneda.valueOf(monedaOrigen);
            Conversor.Moneda monedaDestinoEnum = Conversor.Moneda.valueOf(monedaDestino);

            BigDecimal montoConvertido = conversorService.calcularMontoConvertido(monto, monedaOrigenEnum, monedaDestinoEnum);

            String mensaje = String.format("%.2f %s son %.2f %s", monto, monedaOrigen, montoConvertido, monedaDestino);
            response.put("resultado", mensaje);
        } catch (Exception e) {
            response.put("resultado", "Error en la conversión");
        }
        return response;
    }
}