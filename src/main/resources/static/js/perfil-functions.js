// FUNCIONES DE PERFIL
function cargarDatosPerfil() {
    console.log('👤 Cargando datos de perfil del usuario');
    document.getElementById('perfilNombreCompleto').value = usuario.nombreCompleto || '';
    document.getElementById('perfilEmail').value = usuario.email || '';
    document.getElementById('perfilTelefono').value = usuario.telefono || '';
}

async function actualizarPerfil(e) {
    e.preventDefault();
    
    if (!verificarAutenticacion()) return;
    
    const mensajeDiv = document.getElementById('mensajePerfil');
    mensajeDiv.innerHTML = '';
    
    const passwordActual = document.getElementById('perfilPasswordActual').value;
    const passwordNueva = document.getElementById('perfilPasswordNueva').value;
    const passwordConfirmar = document.getElementById('perfilPasswordConfirmar').value;
    
    // Validar contraseña si se intenta cambiar
    if (passwordNueva || passwordConfirmar) {
        if (!passwordActual) {
            mensajeDiv.innerHTML = '<div class="alert alert-error">❌ Debes ingresar tu contraseña actual para cambiarla</div>';
            return;
        }
        
        if (passwordNueva.length < 6) {
            mensajeDiv.innerHTML = '<div class="alert alert-error">❌ La nueva contraseña debe tener al menos 6 caracteres</div>';
            return;
        }
        
        if (passwordNueva !== passwordConfirmar) {
            mensajeDiv.innerHTML = '<div class="alert alert-error">❌ Las contraseñas nuevas no coinciden</div>';
            return;
        }
    }
    
    const data = {
        nombreCompleto: document.getElementById('perfilNombreCompleto').value,
        email: document.getElementById('perfilEmail').value,
        telefono: document.getElementById('perfilTelefono').value
    };
    
    // Agregar contraseñas solo si se están cambiando
    if (passwordActual && passwordNueva) {
        data.passwordActual = passwordActual;
        data.passwordNueva = passwordNueva;
    }
    
    try {
        console.log('💾 Actualizando perfil...');
        const response = await fetch(API_URL + '/auth/perfil', {
            method: 'PUT',
            headers: { 
                'Content-Type': 'application/json', 
                'Authorization': 'Bearer ' + token 
            },
            body: JSON.stringify(data)
        });
        
        if (!response.ok) {
            const error = await response.json().catch(() => ({ mensaje: 'Error desconocido' }));
            mensajeDiv.innerHTML = '<div class="alert alert-error">❌ ' + (error.mensaje || 'Error al actualizar perfil') + '</div>';
            return;
        }
        
        const usuarioActualizado = await response.json();
        console.log('✅ Perfil actualizado:', usuarioActualizado);
        
        // Actualizar datos locales
        usuario.nombreCompleto = usuarioActualizado.nombreCompleto;
        usuario.email = usuarioActualizado.email;
        usuario.telefono = usuarioActualizado.telefono;
        localStorage.setItem('usuario', JSON.stringify(usuario));
        
        // Actualizar UI
        document.getElementById('nombreUsuario').textContent = usuario.nombreCompleto;
        document.getElementById('nombreCliente').textContent = usuario.nombreCompleto;
        
        // Limpiar campos de contraseña
        document.getElementById('perfilPasswordActual').value = '';
        document.getElementById('perfilPasswordNueva').value = '';
        document.getElementById('perfilPasswordConfirmar').value = '';
        
        mensajeDiv.innerHTML = '<div class="alert alert-success">✅ Perfil actualizado correctamente</div>';
        
        // Ocultar mensaje después de 5 segundos
        setTimeout(() => {
            mensajeDiv.innerHTML = '';
        }, 5000);
        
    } catch (error) {
        console.error('❌ Error al actualizar perfil:', error);
        mensajeDiv.innerHTML = '<div class="alert alert-error">❌ Error de conexión con el servidor</div>';
    }
}
