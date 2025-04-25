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

// ===============================
// Activar pestaña de Transferencias desde Thymeleaf
// ===============================
/**
 * Activa la pestaña de Transferencias si el flag es true
 * @param {boolean} flag
 */
// ===============================
// Función global para activar la pestaña de transferencias
// ===============================
function activarPestanaTransferencias(flag) {
    if (!flag) return;
    document.addEventListener('DOMContentLoaded', function() {
        // Bootstrap 5: activar tab por id
        var tabTrigger = document.getElementById('transferencias-tab');
        if (tabTrigger) {
            var tab = new bootstrap.Tab(tabTrigger);
            tab.show();
        }
    });
}
// Hacerla global explícitamente (por si se usa en inline script)
window.activarPestanaTransferencias = activarPestanaTransferencias;

// Abrir modal de transferencia automáticamente si hay mensaje de error o éxito desde el backend
// Usado en portal-banca (fragmentos.html)
document.addEventListener('DOMContentLoaded', function() {
    var error = /*[[${errorMensaje != null}]]*/ false;
    var success = /*[[${successMessage != null}]]*/ false;
    if (error || success) {
        var transferenciaModal = new bootstrap.Modal(document.getElementById('transferenciaModal'));
        transferenciaModal.show();
    }
});

// ===============================
// AJAX para formulario de transferencia
// ===============================

// ===============================
// AJAX para cambio de contraseña
// ===============================
document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('formCambioContrasena');
    if (form) {
        form.addEventListener('submit', function(e) {
            e.preventDefault();
            const data = new FormData(form);
            fetch('/banca/cambiarContrasena', {
                method: 'POST',
                body: data,
                headers: {
                    'X-Requested-With': 'XMLHttpRequest'
                }
            })
            .then(async response => {
                const contentType = response.headers.get('content-type');
                if (contentType && contentType.includes('application/json')) {
                    let result = await response.json();
                    return {json: true, data: result};
                } else {
                    // Si la respuesta NO es JSON, puede ser un redirect Spring como texto
                    const text = await response.text();
                    if (text.includes('redirect:/banca/portal')) {
                        window.location.href = '/banca/portal';
                        return {json: false};
                    }
                    // Intentar detectar un redirect por JS (Spring podría devolver un HTML con script)
                    const redirectMatch = text.match(/window\.location\.replace\(['"]([^'"]+)['"]\)/);
                    if (redirectMatch) {
                        window.location.href = redirectMatch[1];
                    } else {
                        // Si no se puede extraer, recarga la página
                        window.location.reload();
                    }
                    return {json: false};
                }
            })
            .then(resultObj => {
                if (!resultObj.json) return;
                const result = resultObj.data;
                const mensajeDiv = document.getElementById('errorMensaje');
                if (result.error) {
                    mensajeDiv.textContent = result.error;
                    mensajeDiv.classList.remove('text-success');
                    mensajeDiv.classList.add('text-danger');
                } else if (result.success) {
                    mensajeDiv.textContent = result.success;
                    mensajeDiv.classList.remove('text-danger');
                    mensajeDiv.classList.add('text-success');
                    form.reset();
                    setTimeout(() => {
                        var modal = bootstrap.Modal.getInstance(document.getElementById('cambiarContrasenaModal'));
                        if (modal) modal.hide();
                        mensajeDiv.textContent = '';
                    }, 1500);
                }
            })
            .catch((err) => {
                const mensajeDiv = document.getElementById('errorMensaje');
                mensajeDiv.textContent = err.message || 'Error al cambiar la contraseña.';
                mensajeDiv.classList.remove('text-success');
                mensajeDiv.classList.add('text-danger');
            });
        });
    }
});
// ===============================
// AJAX para formulario de transferencia (Corregido)
// ===============================
document.addEventListener('DOMContentLoaded', function() {
    // --- Transferencia ---
    const form = document.getElementById('formTransferencia');
    if (form) {
        form.addEventListener('submit', function(e) {
            e.preventDefault();
            const data = new FormData(form);
            fetch('/banca/realizarTransferencia', {
                method: 'POST',
                body: data,
                headers: {
                    'X-Requested-With': 'XMLHttpRequest'
                }
            })
            .then(async response => {
                const contentType = response.headers.get('content-type');
                if (contentType && contentType.includes('application/json')) {
                    let result = await response.json();
                    return {json: true, data: result};
                } else {
                    // Si la respuesta NO es JSON, probablemente es un redirect HTML
                    const text = await response.text();
                    if (text.includes('redirect:/banca/portal')) {
                        window.location.href = '/banca/portal';
                        return {json: false};
                    }
                    // Intentar detectar un redirect por JS
                    const redirectMatch = text.match(/window\.location\.replace\(['"]([^'"]+)['"]\)/);
                    if (redirectMatch) {
                        window.location.href = redirectMatch[1];
                    } else {
                        window.location.reload();
                    }
                    return {json: false};
                }
            })
            .then(resultObj => {
                if (!resultObj.json) return;
                const result = resultObj.data;
                const mensajeDiv = document.getElementById('mensajeTransferencia');
                if (result.error) {
                    mensajeDiv.textContent = result.error;
                    mensajeDiv.classList.remove('text-success');
                    mensajeDiv.classList.add('text-danger');
                } else if (result.success) {
                    mensajeDiv.textContent = result.success;
                    mensajeDiv.classList.remove('text-danger');
                    mensajeDiv.classList.add('text-success');
                    setTimeout(() => {
                        // Cierra el modal y limpia el mensaje
                        var modal = bootstrap.Modal.getInstance(document.getElementById('transferenciaModal'));
                        if (modal) modal.hide();
                        mensajeDiv.textContent = '';
                        form.reset();
                        window.location.reload(); // Refresca para ver la transferencia en el historial
                    }, 1500); // Solo recarga si fue éxito
                }
            })
            .catch((err) => {
                console.error('[TRANSFERENCIA][JS][ERROR] Error en fetch:', err);
                const mensajeDiv = document.getElementById('mensajeTransferencia');
                mensajeDiv.textContent = err.message || 'Error al realizar la transferencia.';
                mensajeDiv.classList.remove('text-success');
                mensajeDiv.classList.add('text-danger');
            });
        });
    }
});

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
