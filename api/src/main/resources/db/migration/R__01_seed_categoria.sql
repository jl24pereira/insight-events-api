INSERT INTO categoria (id, nombre, descripcion) VALUES
    ('01920000-0000-7000-8000-000000000001', 'Accidente de tránsito',  'Colisiones y siniestros viales'),
    ('01920000-0000-7000-8000-000000000002', 'Reporte de clima',       'Eventos meteorológicos'),
    ('01920000-0000-7000-8000-000000000003', 'Incidente de seguridad', 'Robos, hurtos y alteraciones'),
    ('01920000-0000-7000-8000-000000000004', 'Falla de servicio',      'Interrupciones de servicios públicos'),
    ('01920000-0000-7000-8000-000000000005', 'Emergencia médica',      'Atenciones y traslados')
ON CONFLICT (id) DO NOTHING;
