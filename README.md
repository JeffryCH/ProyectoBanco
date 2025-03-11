# Proyecto Banco Black Rock

Este repositorio contiene el código fuente para la aplicación web del Banco Black Rock, desarrollada con Spring Boot y Thymeleaf.

## Cambios Recientes

### Nueva Sección de Contáctenos

Se ha implementado una nueva sección de contacto con:

1. **Encabezado Adaptativo**:

   - Imagen de fondo responsiva
   - Título "¡Conozcámonos!" con diseño moderno

2. **Opciones de Contacto**:

   - Botones para WhatsApp, correo e Instagram
   - Diseño intuitivo y amigable

3. **Formulario de Contacto**:
   - Campos para información personal
   - Validación de datos
   - Diseño responsivo y accesible

### Mejoras en Sección Nuestros Clientes

Se ha actualizado la sección de clientes con:

1. **Nuevo Encabezado**:

   - Imagen de fondo adaptativa
   - Título destacado con efectos visuales

2. **Galería de Clientes Mejorada**:

   - Logos centrados y optimizados
   - Cuadrícula responsiva 3x3
   - Mejor visualización en dispositivos móviles

3. **Sección de Testimonios**:
   - Diseño moderno con efecto diagonal
   - Testimonio destacado de Café KICHA
   - Mejor legibilidad y presentación

### Actualización de Sintaxis Thymeleaf

Se ha actualizado la sintaxis de los fragmentos de Thymeleaf en las plantillas para utilizar la notación recomendada con llaves (`~{}`). Esta actualización mejora la legibilidad del código y sigue las mejores prácticas de Thymeleaf.

Ejemplo:

```html
<!-- Sintaxis anterior -->
<header th:replace="layout/plantilla :: header"></header>

<!-- Sintaxis actualizada -->
<header th:replace="~{layout/plantilla :: header}"></header>
```

### Mejoras en los Formularios

Se han rediseñado los formularios en las páginas de servicios para mejorar la experiencia de usuario:

1. **Diseño Horizontal**: Los campos de formulario ahora se muestran en una disposición horizontal, lo que mejora el uso del espacio y la estética.
2. **Centrado de Texto**: Los títulos y descripciones de los formularios están centrados para una mejor presentación visual.
3. **Botones Mejorados**: Se ha optimizado el diseño de los botones de envío, utilizando un ancho proporcional y centrado.

Estas mejoras se han aplicado a todos los formularios en las siguientes secciones:

- Ahorros
- Inversión Digital
- Crédito
- Cuentas

## Estructura del Proyecto

El proyecto sigue una estructura estándar de Spring Boot:

- `src/main/java/com/ProyectoBanco/`: Código fuente Java
  - `controller/`: Controladores de Spring MVC
    - `InicioSesionController.java`: Manejo de autenticación
    - `NuestrosClientesController.java`: Gestión de sección de clientes
    - `ContactenosController.java`: Gestión de sección de contacto
- `src/main/resources/templates/`: Plantillas Thymeleaf
  - `layout/`: Plantillas base y componentes comunes
  - `iniciosesion/`: Plantillas para registro y login
  - `nuestros_clientes/`: Plantillas para la sección de clientes
  - `contactenos/`: Plantillas para la sección de contacto
  - `servicios/`: Plantillas para servicios bancarios
- `src/main/resources/static/`: Recursos estáticos
  - `css/`: Hojas de estilo
  - `IMG/`: Imágenes del sitio
  - `js/`: Scripts de JavaScript

## Tecnologías Utilizadas

- Spring Boot
- Thymeleaf
- Bootstrap 5
- HTML5/CSS3
- JavaScript
