DROP SEQUENCE usuario_id_seq IF EXISTS;
CREATE SEQUENCE usuario_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1; /*COMPATIBILIDADE COM H2 MODO POSTGREE*/
ALTER TABLE usuario ALTER COLUMN id SET DEFAULT NEXTVAL('usuario_id_seq'); /*COMPATIBILIDADE COM H2 MODO POSTGREE*/
insert into usuario (login, senha, nome, email, tipo, tentativas_login_invalido) values ('admin','c6zZpZchMLdQZsgllaH64w==', 'Administrador', 'admin@email.com', 'ADMIN', 0);
insert into usuario (login, senha, nome, email, tipo, tentativas_login_invalido) values ('usuario','QiVgyySWSwSDtwOkK811AA==', 'Usuario', 'usuario@email.com', 'USER', 0);
insert into usuario (login, senha, nome, email, tipo, tentativas_login_invalido) values ('codigoalvo','hdBNkyS7WX79G4AK2Kdulw==', 'codigoalvo', 'codigoalvo@gmail.com', 'ADMIN', 0);
insert into usuario (login, senha, nome, email, tipo, tentativas_login_invalido) values ('teste','maKdyBBf0vo52M3ARzOTjQ==', 'Teste', 'teste@email.com', 'USER', 0);
/*alter sequence USUARIO_ID_SEQ restart with 5;*/
DROP SEQUENCE categoria_id_seq IF EXISTS;
CREATE SEQUENCE categoria_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1; /*COMPATIBILIDADE COM H2 MODO POSTGREE*/
ALTER TABLE categoria ALTER COLUMN id SET DEFAULT NEXTVAL('categoria_id_seq'); /*COMPATIBILIDADE COM H2 MODO POSTGREE*/
insert into categoria(nome, usuario_id) values ('Diversos', (select id from usuario where login = 'admin'));
insert into categoria(nome, usuario_id) values ('Moradia', (select id from usuario where login = 'admin'));
insert into categoria(nome, usuario_id) values ('Alimentação', (select id from usuario where login = 'admin'));
insert into categoria(nome, usuario_id) values ('Transporte', (select id from usuario where login = 'admin'));
insert into categoria(nome, usuario_id) values ('Saúde', (select id from usuario where login = 'teste'));
insert into categoria(nome, usuario_id) values ('Investimentos', (select id from usuario where login = 'teste'));
insert into categoria(nome, usuario_id) values ('Hobbies', (select id from usuario where login = 'teste'));
insert into categoria(nome, usuario_id) values ('Lazer', (select id from usuario where login = 'teste'));
/*alter sequence categoria_id_seq restart with 10;*/
DROP SEQUENCE conta_id_seq IF EXISTS;
CREATE SEQUENCE conta_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1; /*COMPATIBILIDADE COM H2 MODO POSTGREE*/
ALTER TABLE conta ALTER COLUMN id SET DEFAULT NEXTVAL('conta_id_seq'); /*COMPATIBILIDADE COM H2 MODO POSTGREE*/
insert into conta(nome, tipo, usuario_id) values ('Dinheiro', 'D', (select id from usuario where login = 'admin'));
insert into conta(nome, tipo, usuario_id) values ('Débito Banco', 'B', (select id from usuario where login = 'teste'));
insert into conta(nome, tipo, dia_fechamento, dia_pagamento, usuario_id) values ('Cartão Master Banco', 'C', 26, 5, (select id from usuario where login = 'admin'));
insert into conta(nome, tipo, dia_fechamento, dia_pagamento, usuario_id) values ('Cartão Visa Loja', 'C', 24, 3, (select id from usuario where login = 'teste'));
/*alter sequence CONTA_ID_SEQ restart with 5;*/
insert into validador_email(id, email, data, origem) values ('3789a92c818241a7833a26ba0a29f232', 'fulano@email.com', '2015-12-20 16:00:00.000', '127.0.0.1'); /*3789a92c-8182-41a7-833a-26ba0a29f232*/