// Función para manejar el envío del formulario de contacto
document.addEventListener("DOMContentLoaded", function () {
  const formularioContacto = document.getElementById("formularioContacto");
  const mensajeAgradecimiento = document.getElementById(
    "mensajeAgradecimiento"
  );
  const contenidoFormulario = document.getElementById("contenidoFormulario");

  if (formularioContacto) {
    formularioContacto.addEventListener("submit", function (event) {
      event.preventDefault(); // Prevenir el envío normal del formulario

      // Aquí se podría agregar código para enviar los datos del formulario mediante AJAX
      // Por ahora, simplemente mostraremos el mensaje de agradecimiento

      // Ocultar el formulario
      contenidoFormulario.style.display = "none";

      // Mostrar el mensaje de agradecimiento
      mensajeAgradecimiento.style.display = "block";

      // Desplazarse hacia el mensaje
      mensajeAgradecimiento.scrollIntoView({ behavior: "smooth" });
    });
  }
});
