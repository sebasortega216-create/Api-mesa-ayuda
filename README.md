## Endpoints de la API

### Autenticación
- `POST /api/auth/registro` - Registrar un nuevo usuario
- `POST /api/auth/login` - Iniciar sesión y obtener tokens
- `POST /api/auth/refresh` - Renovar el access token
- `POST /api/auth/logout` - Cerrar sesión

### Tickets
- `POST /api/tickets` - Crear un nuevo ticket (requiere autenticación)
- `GET /api/tickets/mios` - Ver tickets del usuario autenticado
- `GET /api/tickets/{id}` - Ver un ticket por su ID
- `GET /api/tickets` - Ver todos los tickets (solo ADMIN/SOPORTE)
- `PATCH /api/tickets/{id}/estado?estado=...` - Cambiar estado (solo ADMIN/SOPORTE)
- `GET /api/tickets/vencidos` - Ver tickets vencidos (solo ADMIN/SOPORTE)

### Utilidad
- `GET /api/ping` - Verificar que la API está activa