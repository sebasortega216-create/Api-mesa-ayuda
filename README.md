# API de Mesa de Ayuda — Etapa 1: Registro y Login con JWT

Esta es la primera entrega del proyecto. Por ahora **solo** incluye:

- Registro de usuarios (`POST /api/auth/registro`)
- Login con emisión de **access token JWT** (`POST /api/auth/login`)
- Endpoint público de verificación (`GET /api/ping`)
- Toda la infraestructura de seguridad (filtro JWT, encriptado BCrypt, manejo de errores 400/401/409)

Aún **no** incluye: refresh token, tickets, SLA ni RBAC por rol. Eso se agrega en las
siguientes etapas.

## Cómo ejecutar

Requisitos: Java 17+ y Maven.

```bash
mvn spring-boot:run
```

La API queda en `http://localhost:8080`.

Consola H2 (para ver los datos en memoria): `http://localhost:8080/h2-console`
JDBC URL: `jdbc:h2:mem:helpdeskdb` — usuario `sa`, sin password.

## Endpoints disponibles

### `GET /api/ping` (público)
Responde `{"mensaje": "pong"}`.

### `POST /api/auth/registro` (público)
```json
{
  "nombre": "Juan Pérez",
  "email": "juan@example.com",
  "password": "123456"
}
```
Respuesta `201 Created` con el usuario creado (rol `USUARIO` por defecto, sin password).
`409 Conflict` si el email ya existe. `400 Bad Request` si falla alguna validación
(email inválido, password < 6 caracteres, campos vacíos).

### `POST /api/auth/login` (público)
```json
{
  "email": "juan@example.com",
  "password": "123456"
}
```
Respuesta `200 OK`:
```json
{
  "accessToken": "eyJhbGciOi...",
  "tipo": "Bearer",
  "usuario": { "id": 1, "nombre": "Juan Pérez", "email": "juan@example.com", "rol": "USUARIO" }
}
```
`401 Unauthorized` si el email o password son incorrectos.

## Cómo probar en Postman

1. `POST http://localhost:8080/api/auth/registro` con el body de ejemplo arriba.
2. `POST http://localhost:8080/api/auth/login` con las mismas credenciales → copia el `accessToken`.
3. (A partir de la próxima etapa) usar ese token en el header `Authorization: Bearer <token>`
   para probar rutas protegidas.

## Notas técnicas

- Passwords cifradas con **BCrypt**.
- El access token incluye `email` (subject) y `rol` como claim, expira en 15 minutos
  (configurable en `app.jwt.expiration-ms`, `application.properties`).
- Filtro `JwtAuthenticationFilter` valida el token en cada petición y carga el usuario
  autenticado en el contexto de seguridad de Spring.
- Si el token falta/es inválido, `JwtAuthenticationEntryPoint` responde `401` en JSON
  (en vez de la página de login por defecto de Spring Security).
