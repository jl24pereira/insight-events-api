# Insight Events AP# Insight Events AP# Insight Events API

API REST para gestión de eventos e investigación (accidentes de tránsito, reportes de clima, entre otros). Permite clasificar eventos, asignarlos a analistas y mantener un historial completo de las acciones realizadas.

> **Nota:** este README documenta honestamente lo que está implementado y lo que quedó pendiente por el plazo de entrega. Ver la sección [Mejoras futuras](#mejoras-futuras).

---

## Tabla de Contenidos

- [Tecnologías](#tecnologías)
- [Arquitectura](#arquitectura)
- [Prerrequisitos](#prerrequisitos)
- [Configuración y Ejecución](#configuración-y-ejecución)
- [Cómo Funciona](#cómo-funciona)
- [Integración Continua](#integración-continua)
- [Migraciones (Flyway)](#migraciones-flyway)
- [Mejoras futuras](#mejoras-futuras)

---

## Tecnologías

- Java 21
- Spring Boot 4.1
- Spring Data JPA / Hibernate 7
- PostgreSQL 18
- Flyway
- Gradle
- Docker & Docker Compose

---

## Arquitectura

El proyecto sigue una arquitectura en capas organizada por feature: cada entidad de dominio tiene su propio paquete con `domain`, `repository`, `service`, `web` y `dto`.

```
com.jlpereira.api
├── shared/       errores comunes, paginación
├── categoria/    CRUD
├── analista/     CRUD
├── evento/       CRUD + código autogenerado
├── historial/    consulta JPQL + registro automático
└── asignacion/   stored procedure
```

Reglas fijas en los cinco features:

- El controlador solo habla con DTOs, nunca con entidades.
- La transacción vive en el servicio (`@Transactional`), nunca en el controlador.
- La comunicación entre features es siempre servicio → servicio.
- `open-in-view` desactivado: obliga a resolver relaciones lazy dentro del servicio.

---

## Prerrequisitos

- [Docker](https://docs.docker.com/get-docker/)
- [Docker Compose](https://docs.docker.com/compose/install/)

No hace falta tener Java ni PostgreSQL instalados localmente — todo corre en contenedores.

---

## Configuración y Ejecución

1. **Clona el repositorio**:

   ```bash
   git clone https://github.com/tuusuario/insight-events-api.git
   cd insight-events-api
   ```

2. **Levanta la base de datos y la API con Docker Compose**:

   ```bash
   docker compose up -d --build
   ```

   Esto levanta:
   - `insight-db`: PostgreSQL 18, con el esquema y los datos de prueba aplicados automáticamente vía Flyway.
   - `insight-api`: la API, en `http://localhost:8080`.

3. **Detener los contenedores**:

   ```bash
   docker compose down
   ```

4. **Reiniciar desde una base limpia** (borra los datos):

   ```bash
   docker compose down -v
   docker compose up -d --build
   ```

5. **Modo desarrollo** (solo la base en Docker, la API desde el IDE):

   ```bash
   docker compose up -d db
   cd api && ./gradlew bootRun
   ```

---

## Cómo Funciona

### Crear una categoría

```bash
curl -X POST http://localhost:8080/api/v1/categorias \
  -H "Content-Type: application/json" \
  -d '{"nombre": "Incendio estructural", "descripcion": "Siniestros en edificaciones"}'
```

### Crear un evento

```bash
curl -X POST http://localhost:8080/api/v1/eventos \
  -H "Content-Type: application/json" \
  -H "X-Usuario: jlpereira" \
  -d '{
    "titulo": "Colisión múltiple en bulevar norte",
    "descripcion": "Tres vehículos involucrados",
    "fecha": "2026-07-28T14:30:00-06:00",
    "prioridad": "ALTA",
    "fuente": "Central 911",
    "categoriaId": "<uuid-de-la-categoria>"
  }'
```

La respuesta incluye un `codigo` autogenerado (`EVT-2026-00001`), sin que se envíe en la solicitud.

### Asignar el evento a un analista

```bash
curl -X POST http://localhost:8080/api/v1/eventos/<eventoId>/asignacion \
  -H "Content-Type: application/json" \
  -H "X-Usuario: jlpereira" \
  -d '{"analistaId": "<uuid-del-analista>"}'
```

Esto ejecuta el stored procedure `sp_asignar_evento`, que valida existencia de ambos recursos, evita duplicados, cambia el estado del evento a `ASIGNADO` y registra la acción en el historial — todo en una sola transacción.

### Consultar el historial de un evento

```bash
curl http://localhost:8080/api/v1/eventos/<eventoId>/historial
```

Devuelve las acciones ordenadas de más reciente a más antigua, con la categoría del evento ya resuelta (sin problema N+1).

Endpoints completos:

| Método             | Ruta                              |
| ------------------ | --------------------------------- |
| GET / POST         | `/api/v1/categorias`              |
| GET / PUT / DELETE | `/api/v1/categorias/{id}`         |
| GET / POST         | `/api/v1/analistas`               |
| GET / PUT / DELETE | `/api/v1/analistas/{id}`          |
| GET / POST         | `/api/v1/eventos`                 |
| GET / PUT / DELETE | `/api/v1/eventos/{id}`            |
| PATCH              | `/api/v1/eventos/{id}/estado`     |
| GET                | `/api/v1/eventos/{id}/historial`  |
| POST               | `/api/v1/eventos/{id}/asignacion` |

Colección de Postman: `insight-events-historial.postman_collection.json`.

---

## Integración Continua

El repo tiene un workflow de GitHub Actions (`.github/workflows/ci.yml`) que corre en cada push y cada PR. Levanta un Postgres de prueba, compila con Gradle y ejecuta el build completo. Si algo se rompe, se ve en el check del commit o del PR antes de mergear.

---

## Migraciones (Flyway)

El esquema de la base no se crea a mano ni con `ddl-auto`, vive versionado en archivos SQL dentro de `api/src/main/resources/db/migration/`. Flyway los aplica en orden automáticamente cada vez que arranca la API.

| Archivo                           | Contenido                                                 |
| --------------------------------- | --------------------------------------------------------- |
| `V1__schema.sql`                  | Las 5 tablas, constraints, índices y `CHECK` de los enums |
| `V2__secuencia_codigo_evento.sql` | Secuencia para el código autogenerado del evento          |
| `V3__sp_asignar_evento.sql`       | Stored procedure de asignación                            |
| `R__01_seed_categoria.sql`        | Categorías de prueba                                      |
| `R__02_seed_analista.sql`         | Analistas de prueba                                       |
| `R__03_seed_evento.sql`           | Eventos de prueba                                         |

Los `V__` corren una sola vez, en orden, y no se pueden modificar después de aplicados. Los `R__` (repetibles) son los datos de prueba: se vuelven a ejecutar cada vez que cambia el archivo, así que se pueden editar libremente sin romper nada.

---

## Mejoras futuras

- Búsqueda de eventos con filtros dinámicos (estado, prioridad, categoría, rango de fechas) + paginación — segunda JPQL pendiente.
- Consulta analítica de resumen por categoría y estadísticas con SQL nativo — tercera JPQL y la nativa pendientes.
- Borrado lógico de eventos (campo `activo`), para resolver la tensión entre "eliminar eventos" y "mantener historial confiable" sin depender de `CASCADE`.
- Endpoint para cancelar/finalizar una asignación, que dispare el cierre del evento.
- Autenticación real (JWT), en lugar del header `X-Usuario` usado como sustituto para trazabilidad.
- Pruebas Unitarias, y configuracion para Code Coverage, para mantener una calidad de codigo correcta.
- Documentación OpenAPI/Swagger.
- Despliegue público en un proveedor administrado (Render, Railway).

