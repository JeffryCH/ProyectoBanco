/**
 * rutinas.js - Archivo principal de funciones JavaScript para el Banco Black Rock
 * Contiene todas las funciones necesarias para la interactividad del sitio web
 */

// ===============================
// Funciones del Carrito de Compras
// ===============================

/**
 * Agrega un producto al carrito de compras
 * Usado en: templates/layout/plantilla.html
 * @param {number} productoId - ID del producto a agregar
 * @param {number} cantidad - Cantidad del producto (default: 1)
 */
function agregarAlCarrito(productoId, cantidad) {
    if (cantidad === undefined) {
        cantidad = 1;
    }
    
    const clienteLogueado = window.clienteLogueado; // Esta variable se setea desde el HTML
    if (!clienteLogueado) {
        alert('Debe iniciar sesión para agregar productos al carrito');
        window.location.href = '/iniciosesion/login';
        return;
    }

    fetch('/carrito/agregar', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ 
            productoId: productoId, 
            cantidad: cantidad,
        }),
    })
    .then(response => response.json())
    .then(data => {
        if (data.totalItems !== undefined) {
            actualizarContadorCarrito(data.totalItems);
            alert('Producto agregado al carrito exitosamente');
        } else {
            alert(data.message || 'Error al agregar al carrito');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('Error al agregar al carrito');
    });
}

/**
 * Elimina un item específico del carrito
 * Usado en: templates/carrito/ver.html
 * @param {number} itemId - ID del item a eliminar
 */
function eliminarItem(itemId) {
    if (confirm('¿Estás seguro de que deseas eliminar este producto del carrito?')) {
        fetch(`/carrito/eliminar/${itemId}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                window.location.reload();
            } else {
                alert('Error al eliminar el producto del carrito');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Error al eliminar el producto del carrito');
        });
    }
}

/**
 * Vacía completamente el carrito
 * Usado en: templates/carrito/ver.html
 */
function vaciarCarrito() {
    if (confirm('¿Estás seguro de que deseas vaciar el carrito?')) {
        fetch('/carrito/vaciar', {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            }
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                window.location.reload();
            } else {
                alert('Error al vaciar el carrito');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Error al vaciar el carrito');
        });
    }
}

/**
 * Actualiza el contador del carrito en la interfaz
 * Usado en: templates/layout/plantilla.html y templates/carrito/ver.html
 * @param {number} cantidad - Nueva cantidad a mostrar
 */
function actualizarContadorCarrito(cantidad) {
    const contador = document.querySelector('#carritoContador');
    if (contador) {
        contador.textContent = cantidad;
    }
}

// ===============================
// Funciones del Formulario de Contacto
// ===============================

/**
 * Maneja el envío del formulario de contacto
 * Usado en: templates/contactenos/contactenos.html
 */
document.addEventListener("DOMContentLoaded", function () {
    const formularioContacto = document.getElementById("formularioContacto");
    const mensajeAgradecimiento = document.getElementById("mensajeAgradecimiento");
    const contenidoFormulario = document.getElementById("contenidoFormulario");

    if (formularioContacto) {
        formularioContacto.addEventListener("submit", function (event) {
            event.preventDefault();

            // Ocultar el formulario
            contenidoFormulario.style.display = "none";

            // Mostrar el mensaje de agradecimiento
            mensajeAgradecimiento.style.display = "block";

            // Desplazarse hacia el mensaje
            mensajeAgradecimiento.scrollIntoView({ behavior: "smooth" });
        });
    }
});

// ===============================
// Funciones del Formulario de Registro
// ===============================

/**
 * Muestra u oculta el campo de nombre del negocio según el tipo de cuenta
 * Usado en: templates/iniciosesion/formularioRegistroUsuario.html
 */
function mostrarCampoNegocio() {
    var tipoCuenta = document.getElementById("tipoCuenta").value;
    var divNombreNegocio = document.getElementById("divNombreNegocio");

    if (tipoCuenta === "PYME" || tipoCuenta === "Empresa") {
        divNombreNegocio.style.display = "block";
        document.getElementById("nombreNegocio").required = true;
    } else {
        divNombreNegocio.style.display = "none";
        document.getElementById("nombreNegocio").required = false;
        document.getElementById("nombreNegocio").value = "";
    }
}

// Inicializar los manejadores de eventos cuando el DOM esté listo
document.addEventListener("DOMContentLoaded", function() {
    // Inicializar manejadores para el carrito
    const eliminarBotones = document.querySelectorAll('.eliminar-item');
    if (eliminarBotones.length > 0) {
        eliminarBotones.forEach(button => {
            button.addEventListener('click', function() {
                const itemId = this.getAttribute('data-item-id');
                eliminarItem(itemId);
            });
        });
    }

    const vaciarCarritoBtn = document.getElementById('vaciarCarrito');
    if (vaciarCarritoBtn) {
        vaciarCarritoBtn.addEventListener('click', vaciarCarrito);
    }

    // Inicializar manejadores para el formulario de registro
    const tipoCuentaSelect = document.getElementById('tipoCuenta');
    if (tipoCuentaSelect) {
        mostrarCampoNegocio(); // Estado inicial
        tipoCuentaSelect.addEventListener('change', mostrarCampoNegocio);
    }
});
