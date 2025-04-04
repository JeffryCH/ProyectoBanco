package com.ProyectoBanco.controller;

import com.ProyectoBanco.service.*;
import com.ProyectoBanco.domain.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;
import java.util.Date;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.HashMap;
import org.springframework.http.HttpStatus;
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

        return "banca/portal";
    }

    @GetMapping("/cerrar-sesion")
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/perfil")
    public String mostrarPerfil() {
        return "banca/portal";
    }
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    @PostMapping("/cambiarContrasena")
    public String cambiarContrasena(@RequestParam String contrasenaActual,
                                    @RequestParam String contrasenaNueva,
                                    @RequestParam String confirmarContrasena,
                                    HttpSession session,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {
    /// Obtener el email del usuario autenticado desde la sesión
        Cliente usuario = (Cliente) session.getAttribute("clienteLogueado");
        String emailUsuario = usuario.getEmail();
        
        System.out.println("BANCA CONTROLLER - email = "+emailUsuario);
        // Verificar si las contraseñas coinciden
        if (!contrasenaNueva.equals(confirmarContrasena)) {
            redirectAttributes.addFlashAttribute("errorMensaje", "Las contraseñas nuevas no coinciden.");
            return "redirect:/banca/portal"; // Nombre de la vista donde se muestra el formulario
        }
        //String contrasenaActual1 = passwordEncoder.encode(contrasenaActual);
        //String contrasenaNueva1 = passwordEncoder.encode(contrasenaNueva);
        // Llamamos al servicio para cambiar la contraseña del usuario
        System.out.println("USUARIO SERVICE = "+ emailUsuario+""+contrasenaActual+""+contrasenaNueva);
        boolean exito = usuarioService.cambiarContrasena(emailUsuario, contrasenaActual, contrasenaNueva);
        
        if (exito) {
            redirectAttributes.addFlashAttribute("successMessage", "Contraseña actualizada correctamente.");
            System.out.println("MENSAJE DE EXITO!!!!!!!!");
        } else {
            redirectAttributes.addFlashAttribute("errorMensaje", "La contraseña actual es incorrecta.");
            System.out.println("FALLOOOOOO!!!!!!!!");
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
    public String realizarTransferencia(@RequestParam Long cuentaOrigen,
                                     @RequestParam String cuentaDestino,
                                     @RequestParam String descripcion,
                                     @RequestParam String monto, // El monto se recibe como String y luego se convierte a BigDecimal
                                     HttpSession session,
                                     Model model) {

        // Obtener el usuario de la sesión
        Cliente usuario = (Cliente) session.getAttribute("clienteLogueado");
        if (usuario == null) {
            return "redirect:/iniciosesion/login"; // Redirigir si no hay usuario logueado
        }

        // Verificar si la cuenta origen y destino son válidas
        Optional<Cuenta> cuentaOrigenOptional = cuentaService.obtenerCuentaPorId(cuentaOrigen);
        Optional<Cuenta> cuentaDestinoOptional = cuentaService.obtenerCuentaPorNumero(cuentaDestino);

        if (!cuentaOrigenOptional.isPresent() || !cuentaDestinoOptional.isPresent()) {
            model.addAttribute("errorMensaje", "Cuenta origen o destino no válida.");
            return "redirect:/banca/portal";
        }

        Cuenta cuentaOrigenObj = cuentaOrigenOptional.get();
        Cuenta cuentaDestinoObj = cuentaDestinoOptional.get();

        // Verificar si hay suficiente saldo en la cuenta origen
        BigDecimal montoTransferencia = new BigDecimal(monto); // Convertir el monto a BigDecimal
        if (cuentaOrigenObj.getSaldo().compareTo(montoTransferencia) < 0) { // Verificar saldo suficiente
            model.addAttribute("errorMensaje", "Saldo insuficiente.");
            return "redirect:/banca/portal";
        }

        // Realizar la transferencia
        cuentaOrigenObj.setSaldo(cuentaOrigenObj.getSaldo().subtract(montoTransferencia)); // Descontamos el monto de la cuenta origen
        cuentaDestinoObj.setSaldo(cuentaDestinoObj.getSaldo().add(montoTransferencia)); // Agregamos el monto a la cuenta destino

        // Crear un registro de la transferencia
        Transferencia transferencia = new Transferencia();
        transferencia.setCuentaOrigen(cuentaOrigenObj);
        transferencia.setCuentaDestino(cuentaDestinoObj);
        transferencia.setMonto(montoTransferencia);
        transferencia.setConcepto(descripcion); // Asignar la descripción
        transferencia.setFechaTransferencia(new Date()); // Asignar la fecha actual

        // Guardar la transferencia
        transferenciaService.guardarTransferencia(transferencia);

        // Actualizar las cuentas
        cuentaService.actualizarCuenta(cuentaOrigenObj);
        cuentaService.actualizarCuenta(cuentaDestinoObj);

        model.addAttribute("successMessage", "Transferencia realizada con éxito.");
        return "redirect:/banca/portal"; // Redirigir a la página principal
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
}