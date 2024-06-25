CREATE TABLE chamado (
  cliente_id int DEFAULT NULL,
  data_abertura date DEFAULT NULL,
  data_fechamento date DEFAULT NULL,
  id int NOT NULL AUTO_INCREMENT,
  prioridade tinyint DEFAULT NULL,
  status tinyint DEFAULT NULL,
  tecnico_id int DEFAULT NULL,
  observacoes varchar(255) DEFAULT NULL,
  titulo varchar(255) DEFAULT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (cliente_id) REFERENCES pessoa (id),
  FOREIGN KEY (tecnico_id) REFERENCES pessoa (id),
  CHECK (prioridade BETWEEN 0 AND 2),
  CHECK (status BETWEEN 0 AND 2)
);