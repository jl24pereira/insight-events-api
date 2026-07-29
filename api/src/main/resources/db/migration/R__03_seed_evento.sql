INSERT INTO evento (id, codigo, titulo, descripcion, fecha, prioridad, estado, fuente, categoria_id) VALUES
    ('01920000-0000-7000-8002-000000000001', 'EVT-2026-90001', 'Colisión múltiple en bulevar norte', 'Tres vehículos involucrados', now() - interval '5 days',  'ALTA',    'NUEVO',      'Central 911',   '01920000-0000-7000-8000-000000000001'),
    ('01920000-0000-7000-8002-000000000002', 'EVT-2026-90002', 'Alerta por lluvias intensas',        'Acumulados sobre 80mm',     now() - interval '4 days',  'CRITICA', 'EN_PROCESO', 'Servicio meteo','01920000-0000-7000-8000-000000000002'),
    ('01920000-0000-7000-8002-000000000003', 'EVT-2026-90003', 'Robo en comercio del centro',        'Sin lesionados',            now() - interval '3 days',  'MEDIA',   'ASIGNADO',   'Denuncia',      '01920000-0000-7000-8000-000000000003'),
    ('01920000-0000-7000-8002-000000000004', 'EVT-2026-90004', 'Corte de energía sector sur',        'Afecta 400 usuarios',       now() - interval '2 days',  'ALTA',    'RESUELTO',   'Distribuidora', '01920000-0000-7000-8000-000000000004'),
    ('01920000-0000-7000-8002-000000000005', 'EVT-2026-90005', 'Traslado de emergencia',             'Paciente estabilizado',     now() - interval '1 days',  'BAJA',    'CERRADO',    'Cruz Roja',     '01920000-0000-7000-8000-000000000005')
ON CONFLICT (id) DO NOTHING;
