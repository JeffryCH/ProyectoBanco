# Proyecto Banco Black Rock

Este repositorio contiene el código fuente para la aplicación web del Banco Black Rock, desarrollada con Spring Boot y Thymeleaf.

## Cambios Recientes

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
- `src/main/resources/templates/`: Plantillas Thymeleaf
- `src/main/resources/static/`: Recursos estáticos (CSS, JS, imágenes)

## Tecnologías Utilizadas

- Spring Boot
- Thymeleaf
- Bootstrap 5
- HTML5/CSS3
- JavaScript
