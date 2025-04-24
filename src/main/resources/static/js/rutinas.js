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

document.addEventListener('DOMContentLoaded', function() {
    // --- Mantener abierto el modal de tipo de cambio si hay resultado ---
    try {
        // Usamos Thymeleaf para inyectar si hay resultadoConversion
        var resultadoConversion = false;
        if (typeof window.resultadoConversionFromThymeleaf !== 'undefined') {
            resultadoConversion = window.resultadoConversionFromThymeleaf;
        }
        if (resultadoConversion) {
            // Activar la pestaña 'Servicios'
            var serviciosTab = document.getElementById('servicios-tab');
            if (serviciosTab) {
                new bootstrap.Tab(serviciosTab).show();
            }
            // Mostrar el modal de Tipo de Cambio
            var tipoCambioModalEl = document.getElementById('tipoCambioModal');
            if (tipoCambioModalEl) {
                var tipoCambioModal = new bootstrap.Modal(tipoCambioModalEl);
                tipoCambioModal.show();
            }
        }
    } catch (e) {
        // Silenciar errores para no afectar otras funciones
        console.warn('Error al intentar mantener abierto el modal de tipo de cambio:', e);
    }

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
document.addEventListener('DOMContentLoaded', function() {
    // --- AJAX para conversión de moneda en tiempo real ---
    // Soporte AJAX para el TAB de tipo de cambio
    var formTipoCambioTab = document.getElementById('formTipoCambioTab');
    if (formTipoCambioTab) {
        formTipoCambioTab.addEventListener('submit', function(e) {
            e.preventDefault();
            var monto = document.getElementById('montoTab').value;
            var monedaOrigen = document.getElementById('monedaOrigenTab').value;
            var monedaDestino = document.getElementById('monedaDestinoTab').value;

            fetch('/banca/convertirMonedaAjaxPropio', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: `monto=${encodeURIComponent(monto)}&monedaOrigen=${encodeURIComponent(monedaOrigen)}&monedaDestino=${encodeURIComponent(monedaDestino)}`
            })
            .then(res => res.json())
            .then(data => {
                var resultadoElem = document.getElementById('resultadoTab');
                resultadoElem.textContent = data.resultado;
                if (data.resultado.startsWith('Error')) {
                    resultadoElem.style.color = 'red';
                    resultadoElem.style.fontWeight = 'bold';
                } else {
                    resultadoElem.style.color = 'green';
                    resultadoElem.style.fontWeight = 'bold';
                    document.getElementById('montoTab').value = '';
                }
                document.getElementById('montoTab').focus();
            })
            .catch(() => {
                var resultadoElem = document.getElementById('resultadoTab');
                resultadoElem.textContent = 'Error en la conversión';
                resultadoElem.style.color = 'red';
                resultadoElem.style.fontWeight = 'bold';
            });
        });
    }

    // Soporte AJAX para el MODAL de tipo de cambio (IDs originales)
    var formTipoCambio = document.getElementById('formTipoCambio');
    if (formTipoCambio) {
        formTipoCambio.addEventListener('submit', function(e) {
            e.preventDefault();
            var monto = document.getElementById('monto').value;
            var monedaOrigen = document.getElementById('monedaOrigen').value;
            var monedaDestino = document.getElementById('monedaDestino').value;

            fetch('/banca/convertirMonedaAjaxPropio', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                body: `monto=${encodeURIComponent(monto)}&monedaOrigen=${encodeURIComponent(monedaOrigen)}&monedaDestino=${encodeURIComponent(monedaDestino)}`
            })
            .then(res => res.json())
            .then(data => {
                var resultadoElem = document.getElementById('resultado');
                resultadoElem.textContent = data.resultado;
                if (data.resultado.startsWith('Error')) {
                    resultadoElem.style.color = 'red';
                    resultadoElem.style.fontWeight = 'bold';
                } else {
                    resultadoElem.style.color = 'green';
                    resultadoElem.style.fontWeight = 'bold';
                    document.getElementById('monto').value = '';
                }
                document.getElementById('monto').focus();
            })
            .catch(() => {
                var resultadoElem = document.getElementById('resultado');
                resultadoElem.textContent = 'Error en la conversión';
                resultadoElem.style.color = 'red';
                resultadoElem.style.fontWeight = 'bold';
            });
        });
    }

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
