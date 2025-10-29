-- Agregar columna mocked_data a price_snapshot
ALTER TABLE price_snapshot ADD COLUMN mocked_data BOOLEAN NOT NULL DEFAULT FALSE;

COMMENT ON COLUMN price_snapshot.mocked_data IS 'Indica si los datos fueron obtenidos de AlphaVantage (false) o son datos mock de respaldo (true)';

