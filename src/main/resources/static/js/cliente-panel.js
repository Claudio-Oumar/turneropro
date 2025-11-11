const API_URL = 'http://localhost:8081/api';
let token = null;
let usuario = null;

// Verificar autenticación inmediatamente al cargar
(function() {
    console.log('🔍 Cliente Panel - Verificando autenticación...');
    token = localStorage.getItem('token');
    const usuarioStr = localStorage.getItem('usuario');
    
    console.log('Token existe:', !!token);
    console.log('Usuario existe:', !!usuarioStr);
    
    if (!token || !usuarioStr) {
        console.error('❌ No hay sesión activa. Redirigiendo al login...');
        alert('No hay sesión activa. Por favor inicia sesión.');
        window.location.replace('/login.html');
        return;
    }
    
    try {
        usuario = JSON.parse(usuarioStr);
        console.log('✅ Usuario parseado:', usuario.username, 'Rol:', usuario.rol);
        
        if (usuario.rol !== 'CLIENTE') {
            console.error('❌ Rol incorrecto. Esta página es solo para CLIENTES');
            alert('Esta página es solo para CLIENTES');
            window.location.replace('/login.html');
            return;
        }
        
        console.log('✅ Autenticación válida. Inicializando panel...');
    } catch (error) {
        console.error('❌ Error al parsear usuario:', error);
        localStorage.clear();
        window.location.replace('/login.html');
        return;
    }
})();

window.addEventListener('DOMContentLoaded', function() {
    console.log('📄 DOM cargado, inicializando panel cliente...');
    if (token && usuario) {
        inicializarPanel();
    }
});

function inicializarPanel() {
    console.log('🚀 Inicializando panel para:', usuario.nombreCompleto);
    document.getElementById('nombreUsuario').textContent = usuario.nombreCompleto;
    document.getElementById('nombreCliente').textContent = usuario.nombreCompleto;
    configurarEventListeners();
    cargarBarberos();
    cargarServicios();
    
    // Intentar cargar reservas sin bloquear si falla
    cargarMisReservas().catch(err => {
        console.warn('⚠️ No se pudieron cargar las reservas inicialmente:', err);
        document.getElementById('listaReservas').innerHTML = 
            '<p>No se pudieron cargar tus reservas. Intenta crear una nueva reserva.</p>';
    });
}

function verificarAutenticacion() {
    if (!token || !usuario) {
        console.error('❌ Sesión perdida. Redirigiendo...');
        alert('Sesión expirada. Por favor inicia sesión nuevamente.');
        localStorage.clear();
        window.location.replace('/login.html');
        return false;
    }
    return true;
}

function configurarEventListeners() {
    document.getElementById('btnCerrarSesion').addEventListener('click', function(e) {
        e.preventDefault();
        localStorage.clear();
        window.location.replace('/login.html');
    });
    document.getElementById('formNuevaReserva').addEventListener('submit', crearReserva);
}

async function crearReserva(e) {
    e.preventDefault();
    
    if (!verificarAutenticacion()) return;
    
    const data = {
        barberoId: parseInt(document.getElementById('barberoId').value),
        servicioId: parseInt(document.getElementById('servicioId').value),
        fechaHoraInicio: document.getElementById('fechaHora').value,
        notasCliente: document.getElementById('notas').value
    };
    
    try {
        console.log('📝 Creando reserva...', data);
        const response = await fetch(API_URL + '/reservas', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json', 'Authorization': 'Bearer ' + token },
            body: JSON.stringify(data)
        });
        
        console.log('📝 Respuesta crear reserva:', response.status);
        
        if (!response.ok) {
            const error = await response.json().catch(() => ({ mensaje: 'Error desconocido' }));
            alert('❌ Error al crear la reserva: ' + (error.mensaje || 'Error desconocido'));
            return;
        }
        
        const reserva = await response.json();
        console.log('✅ Reserva creada:', reserva);
        alert('✅ Reserva creada exitosamente');
        document.getElementById('formNuevaReserva').reset();
        cargarMisReservas().catch(err => console.warn('No se pudieron recargar las reservas'));
    } catch (error) {
        console.error('❌ Error al crear reserva:', error);
        alert('❌ Error de conexión con el servidor');
    }
}

async function cargarBarberos() {
    try {
        console.log('👨‍💼 Cargando barberos...');
        const response = await fetch(API_URL + '/barberos/disponibles');
        
        if (!response.ok) {
            console.error('❌ Error al cargar barberos:', response.status);
            return;
        }
        
        const barberos = await response.json();
        console.log('✅ Barberos cargados:', barberos.length);
        const select = document.getElementById('barberoId');
        select.innerHTML = '<option value="">Seleccione un barbero...</option>';
        barberos.forEach(function(b) {
            const option = document.createElement('option');
            option.value = b.id;
            option.textContent = b.nombreCompleto;
            select.appendChild(option);
        });
    } catch (error) {
        console.error('❌ Error al cargar barberos:', error);
    }
}

async function cargarServicios() {
    try {
        console.log('💇 Cargando servicios...');
        const response = await fetch(API_URL + '/servicios');
        
        if (!response.ok) {
            console.error('❌ Error al cargar servicios:', response.status);
            return;
        }
        
        const servicios = await response.json();
        console.log('✅ Servicios cargados:', servicios.length);
        const select = document.getElementById('servicioId');
        select.innerHTML = '<option value="">Seleccione un servicio...</option>';
        servicios.forEach(function(s) {
            const option = document.createElement('option');
            option.value = s.id;
            option.textContent = s.nombre + ' - $' + s.precio + ' (' + s.duracionMinutos + ' min)';
            select.appendChild(option);
        });
    } catch (error) {
        console.error('❌ Error al cargar servicios:', error);
    }
}

async function cargarMisReservas() {
    if (!verificarAutenticacion()) return;
    
    try {
        console.log('📋 Cargando reservas con token...');
        const response = await fetch(API_URL + '/reservas/mis-reservas', {
            headers: { 'Authorization': 'Bearer ' + token }
        });
        
        console.log('📋 Respuesta de mis-reservas:', response.status);
        
        if (!response.ok) {
            // Si falla, solo mostrar mensaje sin expulsar al usuario
            console.warn('⚠️ No se pudieron cargar las reservas:', response.status);
            document.getElementById('listaReservas').innerHTML = 
                '<p>⚠️ No hay reservas disponibles o no se pudieron cargar. Puedes crear una nueva reserva usando el formulario de arriba.</p>';
            return;
        }
        
        const reservas = await response.json();
        console.log('✅ Reservas cargadas:', reservas.length);
        const listaDiv = document.getElementById('listaReservas');
        if (reservas.length === 0) {
            listaDiv.innerHTML = '<p>✅ No tienes reservas todavía. Usa el formulario de arriba para crear tu primera reserva.</p>';
            return;
        }
        let html = '<table><thead><tr><th>Barbero</th><th>Servicio</th><th>Fecha/Hora</th><th>Estado</th><th>Acciones</th></tr></thead><tbody>';
        reservas.forEach(function(r) {
            const fecha = new Date(r.fechaHoraInicio).toLocaleString('es-ES');
            const puedeCancel = r.estado !== 'CANCELADA' && r.estado !== 'COMPLETADA';
            html += '<tr><td>' + r.barbero.nombreCompleto + '</td><td>' + r.servicio.nombre + '</td><td>' + fecha + '</td><td>' + r.estado + '</td><td>' + (puedeCancel ? '<button class="btn-small btn-danger" onclick="cancelarReserva(' + r.id + ')">Cancelar</button>' : '-') + '</td></tr>';
        });
        html += '</tbody></table>';
        listaDiv.innerHTML = html;
    } catch (error) {
        console.error('❌ Error al cargar reservas:', error);
        document.getElementById('listaReservas').innerHTML = '<p>⚠️ Error al cargar las reservas. Intenta recargar la página.</p>';
    }
}

async function cancelarReserva(reservaId) {
    if (!confirm('Cancelar esta reserva?')) return;
    const motivo = prompt('Motivo (opcional):') || 'Sin motivo';
    try {
        const response = await fetch(API_URL + '/reservas/' + reservaId + '/cancelar', {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json', 'Authorization': 'Bearer ' + token },
            body: JSON.stringify({ motivo: motivo })
        });
        if (response.ok) {
            alert('Reserva cancelada');
            cargarMisReservas();
        } else {
            alert('Error al cancelar');
        }
    } catch (error) {
        alert('Error de conexion');
    }
}
