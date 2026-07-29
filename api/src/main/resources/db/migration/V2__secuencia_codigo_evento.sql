CREATE SEQUENCE seq_evento_codigo START 1;

ALTER TABLE evento ALTER COLUMN codigo SET DEFAULT
    'EVT-' || to_char(now(), 'YYYY') || '-' ||
    lpad(nextval('seq_evento_codigo')::text, 5, '0');
