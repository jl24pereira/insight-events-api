CREATE TABLE categoria (
    id          UUID PRIMARY KEY DEFAULT uuidv7(),
    nombre      VARCHAR(100) NOT NULL,
    descripcion VARCHAR(500),
    CONSTRAINT uk_categoria_nombre UNIQUE (nombre)
);

CREATE TABLE analista (
    id     UUID PRIMARY KEY DEFAULT uuidv7(),
    nombre VARCHAR(150) NOT NULL,
    correo VARCHAR(180) NOT NULL,
    CONSTRAINT uk_analista_correo UNIQUE (correo)
);

CREATE TABLE evento (
    id           UUID PRIMARY KEY DEFAULT uuidv7(),
    codigo       VARCHAR(30)  NOT NULL,
    titulo       VARCHAR(200) NOT NULL,
    descripcion  TEXT,
    fecha        TIMESTAMPTZ  NOT NULL,
    prioridad    VARCHAR(20)  NOT NULL,
    estado       VARCHAR(20)  NOT NULL,
    fuente       VARCHAR(120),
    categoria_id UUID         NOT NULL,
    CONSTRAINT uk_evento_codigo UNIQUE (codigo),
    CONSTRAINT fk_evento_categoria FOREIGN KEY (categoria_id)
        REFERENCES categoria (id) ON DELETE RESTRICT,
    CONSTRAINT ck_evento_prioridad CHECK (prioridad IN ('BAJA','MEDIA','ALTA','CRITICA')),
    CONSTRAINT ck_evento_estado    CHECK (estado    IN ('NUEVO','ASIGNADO','EN_PROCESO','RESUELTO','CERRADO'))
);

CREATE TABLE asignacion (
    id               UUID PRIMARY KEY DEFAULT uuidv7(),
    evento_id        UUID        NOT NULL,
    analista_id      UUID        NOT NULL,
    fecha_asignacion TIMESTAMPTZ NOT NULL DEFAULT now(),
    estado           VARCHAR(20) NOT NULL DEFAULT 'ACTIVA',
    CONSTRAINT fk_asignacion_evento FOREIGN KEY (evento_id)
        REFERENCES evento (id) ON DELETE CASCADE,
    CONSTRAINT fk_asignacion_analista FOREIGN KEY (analista_id)
        REFERENCES analista (id) ON DELETE RESTRICT,
    CONSTRAINT uk_asignacion_evento_analista UNIQUE (evento_id, analista_id),
    CONSTRAINT ck_asignacion_estado CHECK (estado IN ('ACTIVA','FINALIZADA','CANCELADA'))
);

CREATE TABLE historial_evento (
    id         UUID PRIMARY KEY DEFAULT uuidv7(),
    evento_id  UUID         NOT NULL,
    usuario    VARCHAR(150) NOT NULL,
    fecha      TIMESTAMPTZ  NOT NULL DEFAULT now(),
    accion     VARCHAR(30)  NOT NULL,
    comentario TEXT,
    CONSTRAINT fk_historial_evento FOREIGN KEY (evento_id)
        REFERENCES evento (id) ON DELETE CASCADE,
    CONSTRAINT ck_historial_accion CHECK (accion IN
        ('CREACION','ACTUALIZACION','ELIMINACION','ASIGNACION','CAMBIO_ESTADO'))
);

CREATE INDEX idx_evento_categoria       ON evento (categoria_id);
CREATE INDEX idx_evento_estado          ON evento (estado);
CREATE INDEX idx_evento_prioridad       ON evento (prioridad);
CREATE INDEX idx_evento_fecha           ON evento (fecha DESC);
CREATE INDEX idx_asignacion_analista    ON asignacion (analista_id);
CREATE INDEX idx_historial_evento_fecha ON historial_evento (evento_id, fecha DESC);
