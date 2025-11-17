# TurneroPro - Barber Shop Edition 💈

> Sistema de gestión de turnos para barberías - Sprint 2  
> **Equipo:** Innovatech DCJ  
> **Institución:** Escuela Politécnica Nacional  
> **Materia:** Calidad de Software

## 📋 Descripción del Proyecto

**TurneroPro – Barber Shop Edition** es una aplicación web diseñada especialmente para barberías que desean organizar sus turnos de manera digital. Los clientes pueden ver horarios disponibles, reservar citas, cancelarlas o reprogramarlas, mientras los barberos gestionan su agenda diaria y sus servicios ofrecidos.

### 🎯 Objetivos del Sprint 1 ✅

Este sprint implementó las funcionalidades básicas:
- **O1H2**: Registro e inicio de sesión del cliente
- **O1H5**: Definir horas de atención del barbero
- **O1H3**: Reservar un turno eligiendo barbero, servicio, día y hora
- **O1H1**: Organizar el tiempo asignado para evitar solapamientos

### 🎯 Objetivos del Sprint 2 ✅

Este sprint amplía la gestión de citas y notificaciones:
- **O1H4**: Cancelación y reprogramación de citas
  - Cliente puede cancelar reservas con motivo
  - Cliente puede reprogramar citas a nueva fecha/hora
  - Validación de disponibilidad en tiempo real
- **Notificaciones por Email**:
  - Confirmación de reserva al cliente
  - Notificación al barbero de nueva reserva
  - Notificación de cancelación a ambas partes
  - Notificación de reprogramación a ambas partes
- **Recordatorios Automáticos**:
  - Sistema automático que envía recordatorio 24 horas antes de la cita
  - Ejecución programada cada hora mediante scheduler

## 👥 Equipo de Desarrollo

| Nombre | Rol | Responsabilidades |
|--------|-----|-------------------|
| **Dennis Morales** | Desarrollador Backend | Diseño de base de datos, API REST, lógica de negocio y autenticación JWT |
| **Claudio Peñaherrera** | Desarrollador Frontend | Diseño de interfaz, experiencia de usuario y componentes interactivos |
| **Jhonathan Pulig** | QA y DevOps | Pruebas funcionales, despliegue con Docker y documentación técnica |

## 🛠️ Stack Tecnológico

### Backend
- **Java 21** - Lenguaje de programación
- **Spring Boot 3.2.0** - Framework principal
  - Spring Web (REST API)
  - Spring Data JPA (Persistencia)
  - Spring Security (Autenticación JWT)
  - Spring Mail (Notificaciones por email)
  - Spring Scheduling (Tareas programadas)
- **SQLite 3.40** - Base de datos embebida (archivo local, sin servidor)
- **Maven 3.9** - Gestión de dependencias

### Frontend
- **HTML5** - Estructura
- **CSS3** - Estilos (diseño responsive)
- **JavaScript (Vanilla)** - Lógica del cliente y consumo de API REST

### DevOps y Herramientas
- **Docker & Docker Compose** - Contenedorización y orquestación
- **MailHog** - Simulador de correo electrónico para desarrollo
- **Git & GitHub** - Control de versiones

## 📦 Requisitos Previos

Para ejecutar este proyecto necesitas tener instalado:

1. **Docker Desktop** (Windows/Mac) o **Docker Engine + Docker Compose** (Linux)
   - [Descargar Docker Desktop](https://www.docker.com/products/docker-desktop)
   - Versión mínima: Docker 20.x, Docker Compose 2.x

2. **Git** (para clonar el repositorio)
   - [Descargar Git](https://git-scm.com/downloads)

### Verificar instalación

```powershell
# Verificar Docker
docker --version
docker-compose --version

# Verificar Git
git --version
```

## 🚀 Instalación y Ejecución Local (con Docker)

### 1. Clonar el repositorio

```powershell
git clone https://github.com/Claudio-Oumar/innovatech-dcj.git
cd innovatech-dcj
```

### 2. Copiar archivo de variables de entorno (opcional)

```powershell
cp .env.example .env
```

> **Nota:** Las variables de entorno ya están configuradas en `docker-compose.yml` para desarrollo local. Solo necesitas modificar `.env` si quieres cambiar credenciales o puertos.

### 3. Construir y ejecutar con Docker Compose

```powershell
# Construir las imágenes y levantar los contenedores
docker-compose up --build

# O en segundo plano
docker-compose up -d --build
```

Este comando:
- ✅ Descarga la imagen de MailHog
- ✅ Compila la aplicación Spring Boot con Maven
- ✅ Crea la base de datos SQLite en `./data/turnero_pro.db`
- ✅ Carga datos iniciales automáticamente (seeders via DataSeeder.java)
- ✅ Levanta todos los servicios

### 4. Acceder a la aplicación

Una vez que los contenedores estén corriendo:

| Servicio | URL | Descripción |
|----------|-----|-------------|
| **Aplicación Web** | http://localhost:8081 | Frontend y API REST |
| **MailHog UI** | http://localhost:8025 | Interfaz para ver correos enviados (desarrollo) |
| **Base de Datos** | `./data/turnero_pro.db` | Archivo SQLite embebido (creado automáticamente) |

### 5. Detener la aplicación

```powershell
# Detener contenedores
docker-compose down

# Detener y eliminar volúmenes (borra la BD)
docker-compose down -v
```

## 🧪 Usuarios de Prueba

La aplicación viene con usuarios pre-cargados para pruebas:

| Usuario | Contraseña | Rol | Email |
|---------|-----------|-----|-------|
| `admin` | `password123` | Administrador | admin@turneropro.com |
| `barbero1` | `password123` | Barbero | barbero833@gmail.com |
| `barbero2` | `password123` | Barbero | barbero2@turneropro.com |
| `cliente1` | `password123` | Cliente | cliente200201@gmail.com |
| `cliente2` | `password123` | Cliente | cliente2@turneropro.com |

### Servicios Pre-cargados

- Corte Clásico - $12.00 (30 min)
- Corte + Barba - $18.00 (45 min)
- Barba - $8.00 (20 min)
- Rapado - $10.00 (15 min)
- Corte Premium - $25.00 (60 min)
- Tinte de Cabello - $35.00 (90 min)

## 📚 Documentación de la API REST

### Base URL
```
http://localhost:8081/api
```

### Endpoints Principales

#### Autenticación

**Registro de Usuario**
```http
POST /api/auth/registro
Content-Type: application/json

{
  "username": "nuevo_usuario",
  "email": "usuario@example.com",
  "password": "password123",
  "nombreCompleto": "Nombre Completo",
  "telefono": "0991234567",
  "rol": "CLIENTE"  // CLIENTE, BARBERO, ADMINISTRADOR
}
```

**Inicio de Sesión**
```http
POST /api/auth/login
Content-Type: application/json

{
  "usernameOrEmail": "cliente1",
  "password": "password123"
}

Respuesta:
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "tipo": "Bearer",
  "id": 1,
  "username": "cliente1",
  "email": "cliente1@turneropro.com",
  "nombreCompleto": "Juan Pérez",
  "rol": "CLIENTE"
}
```

#### Servicios (Público)

**Listar Servicios Disponibles**
```http
GET /api/servicios
```

#### Barberos (Público)

**Listar Barberos Disponibles**
```http
GET /api/barberos/disponibles
```

#### Reservas (Requiere autenticación)

**Crear Reserva** (Solo CLIENTE)
```http
POST /api/reservas
Authorization: Bearer {token}
Content-Type: application/json

{
  "barberoId": 2,
  "servicioId": 1,
  "fechaHoraInicio": "2025-11-15T10:00:00",
  "notasCliente": "Prefiero corte clásico"
}
```

**Listar Mis Reservas**
```http
GET /api/reservas/mis-reservas
Authorization: Bearer {token}
```

**Cancelar Reserva** (Solo CLIENTE)
```http
PUT /api/reservas/{reservaId}/cancelar
Authorization: Bearer {token}
Content-Type: application/json

{
  "motivo": "Tengo un imprevisto"
}
```

**Reprogramar Reserva** (Solo CLIENTE)
```http
PUT /api/reservas/{reservaId}/reprogramar?nuevaFechaHora=2025-11-20T14:00:00
Authorization: Bearer {token}
```

#### Horarios (Requiere autenticación)

**Crear Horario** (Solo BARBERO)
```http
POST /api/horarios
Authorization: Bearer {token}
Content-Type: application/json

{
  "diaSemana": "MONDAY",  // MONDAY, TUESDAY, etc.
  "horaInicio": "09:00",
  "horaFin": "18:00"
}
```

**Listar Mis Horarios** (Solo BARBERO)
```http
GET /api/horarios/mis-horarios
Authorization: Bearer {token}
```

**Eliminar Horario** (Solo BARBERO)
```http
DELETE /api/horarios/{horarioId}
Authorization: Bearer {token}
```

## 🗂️ Estructura del Proyecto

```
innovatech-dcj/
├── src/
│   └── main/
│       ├── java/com/innovatech/turneropro/
│       │   ├── TurneroProApplication.java
│       │   ├── model/                    # Entidades JPA
│       │   ├── repository/               # Repositorios Spring Data JPA
│       │   ├── service/                  # Lógica de negocio
│       │   ├── controller/               # Controladores REST
│       │   ├── security/                 # Seguridad JWT
│       │   ├── config/                   # Configuraciones
│       │   └── dto/                      # Data Transfer Objects
│       └── resources/
│           ├── application.properties    # Configuración Spring Boot
│           └── static/                   # Frontend HTML/JS/CSS
├── db/init/                              # Scripts SQL de inicialización
├── Dockerfile                            # Imagen Docker de la aplicación
├── docker-compose.yml                    # Orquestación de servicios
├── pom.xml                               # Configuración Maven
└── README.md
```

## 🧪 Cómo Probar la Aplicación

### Escenario 1: Registro y Login de Cliente

1. Abre http://localhost:8081
2. Haz clic en "Registrarse"
3. Completa el formulario con rol "Cliente"
4. Serás redirigido automáticamente al panel de cliente

### Escenario 2: Barbero Configura Horarios

1. Inicia sesión como `barbero1` / `password123`
2. Serás llevado al panel de barbero
3. En "Configurar Horarios de Atención":
   - Selecciona un día de la semana
   - Define hora de inicio y fin
   - Haz clic en "Agregar Horario"
4. Verás tus horarios configurados en la tabla

### Escenario 3: Cliente Reserva un Turno

1. Inicia sesión como `cliente1` / `password123`
2. En el panel de cliente, sección "Nueva Reserva":
   - Selecciona un barbero (ej: Carlos Martínez)
   - Selecciona un servicio (ej: Corte Clásico)
   - Elige fecha y hora (debe estar dentro de horarios configurados)
   - Opcional: agrega notas
   - Haz clic en "Reservar Turno"
3. Verás la reserva en "Mis Reservas"
4. Abre http://localhost:8025 (MailHog) para ver los correos:
   - Confirmación al cliente (cliente200201@gmail.com)
   - Notificación al barbero (barbero833@gmail.com)

### Escenario 4: Cliente Cancela una Reserva

1. En el panel de cliente, sección "Mis Reservas"
2. Haz clic en "Cancelar" en una reserva con estado "CONFIRMADA"
3. Ingresa el motivo de cancelación en el modal
4. Confirma la cancelación
5. Verifica que el estado cambió a "CANCELADA"
6. Revisa MailHog para ver los correos de cancelación enviados al cliente y al barbero

### Escenario 5: Cliente Reprograma una Reserva (Nuevo en Sprint 2)

1. En el panel de cliente, sección "Mis Reservas"
2. Haz clic en "Reprogramar" en una reserva con estado "CONFIRMADA"
3. En el modal, selecciona:
   - Nueva fecha
   - Nueva hora (se cargan automáticamente las horas disponibles del barbero)
4. Confirma la reprogramación
5. Verifica que la fecha y hora se actualizaron
6. Revisa MailHog para ver los correos de reprogramación enviados al cliente y al barbero

### Escenario 6: Recordatorios Automáticos (Nuevo en Sprint 2)

1. El sistema ejecuta automáticamente cada hora un scheduler
2. Busca reservas confirmadas que ocurrirán en 24 horas (±1 hora)
3. Envía recordatorio por email al cliente
4. Marca la reserva como "recordatorio enviado"
5. Para verificar:
   - Crea una reserva para mañana a esta misma hora
   - Espera a la siguiente ejecución del scheduler (cada hora en punto)
   - Revisa MailHog para ver el recordatorio

## 🔧 Ejecutar sin Docker (Manual)

Si prefieres ejecutar la aplicación sin Docker:

### Requisitos

- **Java 17** o superior
- **Maven 3.9** o superior
- El driver SQLite ya está incluido en `lib/sqlite-jdbc-3.40.0.0.jar`

### Pasos

1. **Compilar el proyecto**
```powershell
mvn clean package -DskipTests
```

2. **Ejecutar la aplicación**
```powershell
java -jar target/turnero-pro-1.0.0.jar
```

3. **Acceder a la aplicación**
   - Abre http://localhost:8081
   - La base de datos SQLite se crea automáticamente en `./data/turnero_pro.db`
   - Los datos de prueba se cargan automáticamente al iniciar

4. **Para probar emails (Opcional con MailHog)**
```powershell
# Iniciar Docker Desktop primero, luego:
docker run -d -p 1025:1025 -p 8025:8025 --name mailhog mailhog/mailhog

# Ver correos en: http://localhost:8025
```

> **Nota:** No necesitas instalar ni configurar ninguna base de datos. SQLite es un archivo embebido que se crea automáticamente.

## 🐛 Troubleshooting

### Error: "Puerto 8081 ya está en uso"

```powershell
# Windows: buscar proceso usando el puerto
netstat -ano | findstr :8081

# Matar el proceso (reemplaza PID)
taskkill /PID <PID> /F
```

### Error: "No se puede crear/acceder a la base de datos"

```powershell
# Verificar que la carpeta data existe
mkdir data

# Si hay problemas con permisos en Docker, verificar volúmenes
docker-compose logs app

# Eliminar la BD y dejar que se recree
Remove-Item ./data/turnero_pro.db
docker-compose restart app
```

## 📧 Sistema de Notificaciones (Sprint 2)

La aplicación incluye un sistema completo de notificaciones por email:

### Configuración de Gmail

El sistema usa **ServicioCorreoSingleton** (patrón del proyecto CineMax) para enviar correos reales a Gmail.

**Pasos para configurar:**

1. **Obtener App Password de Google:**
   - Ve a https://myaccount.google.com/apppasswords
   - Activa la verificación en 2 pasos si no la tienes
   - Genera una contraseña de aplicación:
     - Selecciona "Correo"
     - Selecciona "Otro dispositivo"
     - Copia el password de 16 caracteres (ej: `abcdefghijklmnop`)

2. **Editar `ServicioCorreoSingleton.java`:**
   ```java
   // Líneas 16-17
   private final String remitente = "barbero833@gmail.com";
   private final String clave = "tu-app-password-de-16-caracteres";  // Sin espacios
   ```

3. **Recompilar y reiniciar Docker:**
   ```powershell
   docker-compose down
   docker-compose up --build
   ```

### Tipos de Emails Enviados

1. **Confirmación de reserva** → Cliente (cliente200201@gmail.com)
2. **Notificación de nueva reserva** → Barbero (barbero833@gmail.com)
3. **Cancelación de reserva** → Cliente y Barbero
4. **Reprogramación de reserva** → Cliente y Barbero
5. **Recordatorio 24h antes** → Cliente (automático)

**Nota:** Todos los correos se envían desde `barbero833@gmail.com` a direcciones reales.

## 🚀 Próximos Pasos (Sprint 3)

- **Sprint 3**:
  - Panel administrativo con reportes y gráficos
  - Gestión de feriados y bloqueos de agenda
  - Sistema de calificaciones y reseñas
  - Mejoras de UI/UX con framework moderno
  - Suite completa de tests automatizados

## 📄 Licencia

Este proyecto es parte de un trabajo académico para la materia de Calidad de Software en la Escuela Politécnica Nacional.

---

Desarrollado con ❤️ por el equipo **Innovatech DCJ** - EPN 2025
