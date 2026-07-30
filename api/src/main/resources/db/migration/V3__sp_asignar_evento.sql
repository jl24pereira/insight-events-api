CREATE OR REPLACE PROCEDURE sp_asignar_evento(
    IN  p_evento_id     UUID,
    IN  p_analista_id   UUID,
    IN  p_usuario       VARCHAR,
    OUT p_asignacion_id UUID
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_codigo_evento VARCHAR;
BEGIN
    SELECT codigo INTO v_codigo_evento
    FROM evento WHERE id = p_evento_id;

    IF NOT FOUND THEN
        RAISE EXCEPTION 'EVENTO_NO_ENCONTRADO: %', p_evento_id
            USING ERRCODE = 'P0001';
    END IF;

    IF NOT EXISTS (SELECT 1 FROM analista WHERE id = p_analista_id) THEN
        RAISE EXCEPTION 'ANALISTA_NO_ENCONTRADO: %', p_analista_id
            USING ERRCODE = 'P0002';
    END IF;

    IF EXISTS (
        SELECT 1 FROM asignacion
        WHERE evento_id = p_evento_id
          AND analista_id = p_analista_id
    ) THEN
        RAISE EXCEPTION 'ASIGNACION_DUPLICADA: evento % ya fue asignado al analista %',
            p_evento_id, p_analista_id
            USING ERRCODE = 'P0003';
    END IF;

    INSERT INTO asignacion (evento_id, analista_id, fecha_asignacion, estado)
    VALUES (p_evento_id, p_analista_id, now(), 'ACTIVA')
    RETURNING id INTO p_asignacion_id;

    UPDATE evento SET estado = 'ASIGNADO' WHERE id = p_evento_id;

    INSERT INTO historial_evento (evento_id, usuario, fecha, accion, comentario)
    VALUES (
        p_evento_id, p_usuario, now(), 'ASIGNACION',
        'Evento ' || v_codigo_evento || ' asignado al analista ' || p_analista_id
    );
END;
$$;
