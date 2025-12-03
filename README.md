# Zarp 🏠

Zarp es una aplicación web que conecta propietarios y huéspedes para alquileres temporales de departamentos  
Los anfitriones publican sus propiedades y los usuarios pueden alquilarlas por unos días un fin de semana o una semana completa de forma simple y rápida

---

## 🚀 Tecnologías utilizadas

### Backend
Java 17  
Spring Boot 3.5.3  
Spring Web  
Spring Data JPA  
Spring Security  
Spring Validation  
Spring WebSocket  
Spring Mail  
MySQL conector mysql-connector-j  
MapStruct mapeo de DTOs  
Lombok reducción de boilerplate  
Firebase Admin SDK autenticación y verificación de tokens  
MercadoPago SDK pagos online  
PayPal Checkout SDK pagos online  
DotEnv gestión de variables de entorno  
iTextPDF generación de PDFs  
DevTools hot reload en desarrollo

### Frontend
El frontend del proyecto se encuentra en el siguiente repositorio  
👉 [Zarp Frontend](https://github.com/DiegoCanaless/Zarp)

---

## ⚙️ Instalación y ejecución

### Prerrequisitos
JDK 17  
Maven 3+  
MySQL en ejecución  
IntelliJ IDEA opcional recomendado

### Clonar y ejecutar el proyecto backend
```
   git clone https://github.com/BuchaillotBenjamin/Zarp_Back.git
   cd Zarp_Back
   mvn clean install
   mvn spring-boot:run
```
---
## ✨ Características del sistema

- **Publicación y administración de propiedades**  
  Los anfitriones pueden crear, editar y eliminar publicaciones de departamentos, incluyendo fotos, precios y disponibilidad.

- **Gestión de usuarios y empleados**  
  Administración de cuentas de huéspedes, anfitriones y personal interno.

- **Reservas y alquileres temporales**  
  Los usuarios pueden realizar reservas por días, fines de semana o semanas completas de manera simple y rápida.

- **Validaciones de usuario**
  Comprobación del **DNI** para asegurar la identidad del usuario.
  Verificación del **correo electrónico asociado a la cuenta** para mayor seguridad y trazabilidad.

- **Pagos online integrados**  
  Soporte para pagos seguros mediante **PayPal** y **MercadoPago**, con generación de comprobantes en PDF.

- **Autenticación y seguridad**  
  Validación de usuarios con **Firebase** y protección de rutas con **Spring Security**.

- **Notificaciones en tiempo real**  
  Uso de **WebSockets** para avisar sobre nuevas reservas, actualizaciones de estado y mensajes entre anfitrión y huésped.

- **Generación de documentos PDF**  
  Contratos de alquiler y comprobantes de pago generados automáticamente con **iTextPDF**.

- **Sistema de auditoría y trazabilidad**  
  Registro persistente de acciones críticas para garantizar transparencia y control operativo.  

