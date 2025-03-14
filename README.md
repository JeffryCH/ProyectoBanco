# Proyecto Banco Black Rock

Este repositorio contiene el código fuente para la aplicación web del Banco Black Rock, un sistema bancario completo desarrollado con Spring Boot y Thymeleaf. El proyecto implementa una arquitectura MVC (Modelo-Vista-Controlador) para ofrecer una experiencia de usuario intuitiva y funcionalidades bancarias completas.

## Características Principales

### Gestión de Clientes y Cuentas

Se ha implementado una nueva sección de contacto con:

1. **Gestión de Clientes y Cuentas**:

   - Registro de nuevos clientes
   - Administración de cuentas bancarias
   - Perfiles de usuario personalizables
   - Historial de transacciones

2. **Servicios Financieros**:

   - Cuentas de ahorro y corrientes
   - Productos de inversión digital
   - Opciones de crédito personalizadas
   - Tarjetas de crédito con diferentes beneficios

3. **Portal Bancario**:

   - Interfaz de banca en línea segura
   - Dashboard con resumen financiero
   - Transferencias entre cuentas
   - Pagos de servicios

4. **Sección de Contacto**:

   - Formulario de contacto interactivo
   - Múltiples canales de comunicación (WhatsApp, correo, Instagram)
   - Diseño responsivo y accesible
   - Validación de datos de contacto

5. **Área de Clientes Corporativos**:
   - Showcase de clientes destacados
   - Testimonios y casos de éxito
   - Soluciones empresariales personalizadas
   - Galería de logos de empresas asociadas

## Estructura del Proyecto

El proyecto sigue una estructura estándar de Spring Boot:

```
src/
├── main/
│ ├── java/com/ProyectoBanco/
│ │ ├── controller/ # Controladores MVC
│ │ ├── domain/ # Entidades y modelos
│ │ ├── repository/ # Interfaces de repositorio
│ │ ├── service/ # Servicios de negocio
│ │ └── ProyectoApplication.java
│ │
│ └── resources/
│ ├── static/
│ │ ├── IMG/ # Imágenes del sitio
│ │ └── js/ # Scripts JavaScript
│ │
│ ├── templates/
│ │ ├── banca/ # Plantillas del portal bancario
│ │ ├── carrito/ # Plantillas del carrito de compras
│ │ ├── contactenos/ # Plantillas de la sección de contacto
│ │ ├── iniciosesion/ # Plantillas de autenticación
│ │ ├── layout/ # Plantillas base y componentes comunes
│ │ ├── nuestros_clientes/ # Plantillas de clientes corporativos
│ │ ├── productos/ # Plantillas de productos financieros
│ │ └── servicios/ # Plantillas de servicios bancarios
│ │
│ └── application.properties # Configuración de la aplicación
```

## Tecnologías Utilizadas

- **Backend**: Java 17, Spring Boot 3.x
- **Persistencia**: Spring Data JPA, Hibernate
- **Frontend**: Thymeleaf, Bootstrap 5, HTML5/CSS3, JavaScript
- **Base de Datos**: MySQL (configurada)
- **Seguridad**: Spring Security
- **Herramientas de Construcción**: Maven

## Actualizaciones Recientes

### Mejoras en el Carrito de Compras (14/03/2025)

- **Corrección de Relaciones entre Entidades**:
  - Resolución del problema de recursión infinita en las relaciones Cliente-Producto
  - Implementación de `@ToString.Exclude` en las entidades para gestionar referencias circulares
  - Optimización de la serialización de objetos para el carrito

- **Mejoras en la Interfaz del Carrito**:
  - Fragmentación de plantillas para mejor mantenibilidad
  - Nuevo archivo `fragmentos.html` para componentes reutilizables del carrito
  - Actualización de `ver.html` para utilizar los nuevos fragmentos

- **Optimizaciones de JavaScript**:
  - Corrección de sintaxis en el código del carrito
  - Mejora en el manejo de eventos y respuestas del servidor
  - Implementación de mejores prácticas en la estructura del código

### Mejoras Anteriores

- **Mejoras en Interfaz de Usuario**:
  - Implementación de diseño responsivo en todas las secciones
  - Actualización del sistema de navegación
  - Mejora en formularios con validación en tiempo real
  - Optimización para visualización en dispositivos móviles

- **Actualización de Componentes**:
  - Sintaxis Thymeleaf actualizada a la notación recomendada con llaves (~{})
  - Estructura de fragmentos mejorada para reutilización de código
  - Organización modular de plantillas y estilos

- **Ampliación de Funcionalidades**:
  - Nueva implementación de simuladores financieros
  - Sistema de notificaciones para el usuario
  - Mejora en el proceso de registro y autenticación

- **Integración de Servicios Externos**:
  - Conexión con APIs de terceros para pagos y transferencias
  - Implementación de servicios de verificación de identidad
  - Integración con plataformas de análisis de datos para mejorar la experiencia del usuario
