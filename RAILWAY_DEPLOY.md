# 🚀 Despliegue en Railway - TurneroPro

## Pasos para desplegar:

### 1. Crear cuenta en Railway
- Ve a: https://railway.app
- Inicia sesión con GitHub

### 2. Crear nuevo proyecto
- Haz clic en "New Project"
- Selecciona "Deploy from GitHub repo"
- Autoriza Railway a acceder a tu repo
- Selecciona el repositorio `innovatech`

### 3. Agregar PostgreSQL
- En el dashboard del proyecto, haz clic en "+ New"
- Selecciona "Database" → "Add PostgreSQL"
- Railway creará automáticamente la base de datos
- La variable `DATABASE_URL` se configura automáticamente

### 4. Configurar variables de entorno
En el servicio de tu aplicación, ve a "Variables" y agrega:

```env
# Railway ya proporciona estas automáticamente:
# - PORT
# - DATABASE_URL

# Agregar manualmente:
DB_DRIVER=org.postgresql.Driver
DB_DIALECT=org.hibernate.dialect.PostgreSQLDialect
JWT_SECRET=tu-clave-secreta-super-segura-de-al-menos-512-bits-generada-aleatoriamente
CORS_ALLOWED_ORIGINS=https://tu-dominio.railway.app
LOG_LEVEL=INFO
```

### 5. Configurar dominio público
- Ve a "Settings" en tu servicio
- En "Networking" → "Generate Domain"
- Railway te dará una URL como: `https://turneropro-production.up.railway.app`

### 6. Actualizar CORS_ALLOWED_ORIGINS
- Copia la URL generada
- Actualiza la variable `CORS_ALLOWED_ORIGINS` con esa URL

### 7. Deploy automático
- Railway detectará `railway.json` y `pom.xml`
- Iniciará el build automáticamente
- Compilará con Maven
- Ejecutará el JAR

### 8. Verificar deployment
- Ve a "Deployments" para ver el log
- Busca: `Started TurneroProApplication in X seconds`
- Abre tu URL: `https://tu-app.railway.app`

## 🔧 Configuración adicional (Opcional)

### Configurar dominio custom
Si tienes un dominio propio:
1. Ve a "Settings" → "Networking"
2. Haz clic en "Custom Domain"
3. Ingresa tu dominio (ej: `turneropro.com`)
4. Configura los registros DNS según Railway te indique

### Monitoreo
- Railway muestra logs en tiempo real
- Métricas de CPU y memoria
- Reinicio automático en caso de fallos

## ✅ Verificaciones post-deploy

1. **Página principal:**
   ```
   https://tu-app.railway.app
   ```

2. **API Health Check:**
   ```
   https://tu-app.railway.app/api/servicios
   ```

3. **Login:**
   ```
   https://tu-app.railway.app/login.html
   ```

4. **Base de datos:**
   - Railway muestra el estado de PostgreSQL
   - Puedes conectarte con herramientas externas si necesitas

## 🐛 Troubleshooting

### Error: "Application failed to respond"
- Verifica que `PORT` esté configurado correctamente
- Revisa los logs: "View Logs"

### Error: "Database connection failed"
- Verifica que PostgreSQL esté running
- Confirma que `DATABASE_URL` está presente en variables

### Error 503 Service Unavailable
- El build está en progreso
- Espera 2-3 minutos y recarga

## 💰 Costos

Railway ofrece:
- **$5 USD gratis al mes** (Hobby plan)
- Suficiente para proyectos pequeños/medianos
- PostgreSQL incluido en el plan gratuito

## 📝 Notas importantes

1. Railway detecta automáticamente Java/Maven
2. PostgreSQL se crea vacío, las tablas se crean automáticamente con `hibernate.ddl-auto=update`
3. Los datos de prueba (seeders) se cargan automáticamente al iniciar
4. Los correos funcionarán con el App Password configurado en `ServicioCorreoSingleton.java`

---

¿Preguntas? Revisa: https://docs.railway.app
