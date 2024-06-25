CREATE TABLE pessoa (
  data_criacao date DEFAULT NULL,
  id int NOT NULL AUTO_INCREMENT,
  dtype varchar(31) NOT NULL,
  cpf varchar(255) DEFAULT NULL,
  email varchar(255) DEFAULT NULL,
  nome varchar(255) DEFAULT NULL,
  senha varchar(255) DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE (cpf),
  UNIQUE (email)
);