# 👥 Usuarios Disponibles en TurneroPro

## ✅ La aplicación está corriendo en: **http://localhost:8081**

---

## 🔐 Usuarios de Prueba Disponibles

Todos los usuarios tienen la contraseña: **`password123`**

### 👨‍💼 Administrador
- **Usuario:** `admin`
- **Email:** admin@turneropro.com
- **Nombre:** Administrador Sistema
- **Teléfono:** 0991234567

### 💈 Barberos
1. **Usuario:** `barbero1`
   - **Email:** barbero1@turneropro.com
   - **Nombre:** Carlos Martínez
   - **Teléfono:** 0991234568
   - **Horario:** Lunes a Viernes, 9:00 AM - 6:00 PM

2. **Usuario:** `barbero2`
   - **Email:** barbero2@turneropro.com
   - **Nombre:** Miguel Sánchez
   - **Teléfono:** 0991234569
   - **Horario:** Lunes a Sábado, 10:00 AM - 7:00 PM

### 👤 Clientes
1. **Usuario:** `cliente1`
   - **Email:** cliente1@turneropro.com
   - **Nombre:** Juan Pérez
   - **Teléfono:** 0991234570

2. **Usuario:** `cliente2`
   - **Email:** cliente2@turneropro.com
   - **Nombre:** María González
   - **Teléfono:** 0991234571

3. **Usuario:** `testrail`
   - **Email:** testrail@turneropro.com
   - **Nombre:** Test Rail User
   - **Teléfono:** 0991234572

---

## 🎯 Cómo Usar la Aplicación

### 1. **Iniciar Sesión**
- Ve a: **http://localhost:8081/login.html**
- Ingresa uno de los usuarios de arriba
- Contraseña: `password123`
- Click en "Iniciar Sesión"

### 2. **Panel de Cliente** (cliente1, cliente2, testrail)
Después de iniciar sesión, podrás:
- ✅ Ver la página de bienvenida con tu nombre
- ✅ Seleccionar un barbero (Carlos Martínez o Miguel Sánchez)
- ✅ Seleccionar un servicio (Corte Clásico, Corte + Barba, etc.)
- ✅ Elegir fecha y hora para tu reserva
- ✅ Agregar notas opcionales
- ✅ Crear la reserva
- ✅ Ver tus reservas actuales
- ✅ Cancelar reservas si es necesario

### 3. **Panel de Barbero** (barbero1, barbero2)
Después de iniciar sesión, podrás:
- ✅ Ver la página de bienvenida con tu nombre
- ✅ Ver tus horarios configurados
- ✅ Agregar nuevos horarios de disponibilidad
- ✅ Ver las reservas de tus clientes
- ✅ Completar reservas cuando termines el servicio

---

## 📋 Servicios Disponibles

| Servicio | Duración | Precio |
|----------|----------|---------|
| Corte Clásico | 30 min | $12.00 |
| Corte + Barba | 45 min | $18.00 |
| Barba | 20 min | $8.00 |
| Rapado | 15 min | $10.00 |
| Corte Premium | 60 min | $25.00 |
| Tinte de Cabello | 90 min | $35.00 |

---

## 🔧 Cambios Realizados

### ✅ Problema Resuelto
- **Antes:** Los usuarios eran expulsados inmediatamente al entrar al panel
- **Ahora:** Los usuarios pueden entrar y usar la aplicación sin problemas

### 🛠️ Mejoras Implementadas
1. **Verificación de autenticación mejorada** - Ya no redirige inmediatamente si falla la carga de datos
2. **Mejor manejo de errores** - Muestra mensajes informativos en vez de expulsar al usuario
3. **Logs de depuración** - Facilita encontrar problemas con emojis 🔍 ✅ ❌
4. **Usuario testrail agregado** - Ahora disponible para pruebas
5. **Base de datos limpia** - Todos los usuarios funcionan correctamente

### 📝 Archivos Modificados
- ✅ `cliente-panel.js` - Mejor manejo de sesión y errores
- ✅ `barbero-panel.js` - Mejor manejo de sesión y errores
- ✅ `login.js` - Verificación de guardado en localStorage
- ✅ `registro.js` - Verificación de guardado en localStorage
- ✅ `DataSeeder.java` - Usuario testrail agregado

---

## 🚀 Comandos Útiles

### Ver logs de la aplicación:
```bash
docker logs turnero-pro-app -f
```

### Reiniciar la aplicación:
```bash
cd "c:\Users\ASUS TUF F15\Downloads\innovatech-dcj-main"
docker-compose restart app
```

### Detener todo:
```bash
cd "c:\Users\ASUS TUF F15\Downloads\innovatech-dcj-main"
docker-compose down
```

### Iniciar todo:
```bash
cd "c:\Users\ASUS TUF F15\Downloads\innovatech-dcj-main"
docker-compose up -d
```

---

## 📧 Servidor de Correo (MailHog)

Los correos de confirmación se pueden ver en:
- **URL:** http://localhost:8025
- Aquí verás todos los correos de confirmación de reservas

---

## ✨ ¡Todo Listo!

La aplicación está completamente funcional. Puedes iniciar sesión con cualquiera de los usuarios listados arriba y empezar a crear reservas.

**Fecha de configuración:** 11 de noviembre de 2025
**Estado:** ✅ Funcionando correctamente
**Puerto:** 8081
