CREATE TABLE perfis (
  perfis int DEFAULT NULL,
  pessoa_id int NOT NULL,
  CONSTRAINT FK_pessoa FOREIGN KEY (pessoa_id) REFERENCES pessoa (id)
);