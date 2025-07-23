CREATE TABLE ciudad (
  id IDENTITY PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  latitud DOUBLE NOT NULL,
  longitud DOUBLE NOT NULL
);

CREATE TABLE deposito (
  id IDENTITY PRIMARY KEY,
  ciudad_id BIGINT NOT NULL,
  direccion VARCHAR(255) NOT NULL,
  latitud DOUBLE NOT NULL,
  longitud DOUBLE NOT NULL,
  FOREIGN KEY (ciudad_id) REFERENCES ciudad(id)
);

CREATE TABLE cliente (
  id IDENTITY PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  email VARCHAR(100) UNIQUE NOT NULL,
  password VARCHAR(100) NOT NULL
);

CREATE TABLE camion (
  id IDENTITY PRIMARY KEY,
  capacidad_peso DOUBLE NOT NULL,
  capacidad_volumen DOUBLE NOT NULL,
  disponible BOOLEAN NOT NULL
);

CREATE TABLE contenedor (
  id IDENTITY PRIMARY KEY,
  peso DOUBLE NOT NULL,
  volumen DOUBLE NOT NULL,
  estado VARCHAR(50) NOT NULL,
  cliente_id BIGINT NOT NULL,
  FOREIGN KEY (cliente_id) REFERENCES cliente(id)
);

CREATE TABLE tarifa (
  id IDENTITY PRIMARY KEY,
  monto_base DOUBLE NOT NULL,
  costo_km DOUBLE NOT NULL,
  costo_dia_deposito DOUBLE NOT NULL
);

CREATE TABLE solicitud (
  id IDENTITY PRIMARY KEY,
  contenedor_id BIGINT NOT NULL,
  ciudad_origen_id BIGINT NOT NULL,
  ciudad_destino_id BIGINT NOT NULL,
  deposito_id BIGINT NOT NULL,
  camion_id BIGINT NOT NULL,
  costo_estimado DOUBLE,
  tiempo_estimado_horas DOUBLE,
  FOREIGN KEY (contenedor_id) REFERENCES contenedor(id),
  FOREIGN KEY (ciudad_origen_id) REFERENCES ciudad(id),
  FOREIGN KEY (ciudad_destino_id) REFERENCES ciudad(id),
  FOREIGN KEY (deposito_id) REFERENCES deposito(id),
  FOREIGN KEY (camion_id) REFERENCES camion(id)
);

CREATE TABLE tramo_ruta (
  id IDENTITY PRIMARY KEY,
  solicitud_id BIGINT NOT NULL,
  orden INT NOT NULL,
  tipo_tramo VARCHAR(30) NOT NULL, -- 'ORIGEN_DEP', 'DEP_DESTINO'
  ciudad_origen_id BIGINT NOT NULL,
  ciudad_destino_id BIGINT NOT NULL,
  fecha_estimada_salida TIMESTAMP,
  fecha_estimada_llegada TIMESTAMP,
  fecha_real_salida TIMESTAMP,
  fecha_real_llegada TIMESTAMP,
  FOREIGN KEY (solicitud_id) REFERENCES solicitud(id),
  FOREIGN KEY (ciudad_origen_id) REFERENCES ciudad(id),
  FOREIGN KEY (ciudad_destino_id) REFERENCES ciudad(id)
);