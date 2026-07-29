INSERT INTO analista (id, nombre, correo) VALUES
    ('01920000-0000-7000-8001-000000000001', 'María Fernández', 'maria.fernandez@insight.test'),
    ('01920000-0000-7000-8001-000000000002', 'Carlos Rivas',    'carlos.rivas@insight.test'),
    ('01920000-0000-7000-8001-000000000003', 'Ana Molina',      'ana.molina@insight.test'),
    ('01920000-0000-7000-8001-000000000004', 'Diego Ortega',    'diego.ortega@insight.test')
ON CONFLICT (id) DO NOTHING;
