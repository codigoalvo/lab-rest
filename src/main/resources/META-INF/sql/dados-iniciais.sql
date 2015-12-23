DROP SEQUENCE usuario_id_seq IF EXISTS;
CREATE SEQUENCE usuario_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1; /*COMPATIBILIDADE COM H2 MODO POSTGREE*/
ALTER TABLE usuario ALTER COLUMN id SET DEFAULT NEXTVAL('usuario_id_seq'); /*COMPATIBILIDADE COM H2 MODO POSTGREE*/
insert into usuario (email, senha, nome, apelido, tipo, tentativas_login_invalido) values ('admin@email.com','c6zZpZchMLdQZsgllaH64w==', 'Administrador', 'admin', 'ADMIN', 0);
insert into usuario (email, senha, nome, apelido, tipo, tentativas_login_invalido) values ('usuario@email.com','QiVgyySWSwSDtwOkK811AA==', 'Usuario', 'usuario', 'USER', 0);
insert into usuario (email, senha, nome, apelido, tipo, tentativas_login_invalido) values ('codigoalvo@gmail.com','hdBNkyS7WX79G4AK2Kdulw==', 'codigoalvo', 'codigoalvo', 'ADMIN', 0);
insert into usuario (email, senha, nome, apelido, tipo, tentativas_login_invalido) values ('teste@email.com','maKdyBBf0vo52M3ARzOTjQ==', 'Teste', 'teste', 'USER', 0);
/*alter sequence USUARIO_ID_SEQ restart with 5;*/
DROP SEQUENCE categoria_id_seq IF EXISTS;
CREATE SEQUENCE categoria_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1; /*COMPATIBILIDADE COM H2 MODO POSTGREE*/
ALTER TABLE categoria ALTER COLUMN id SET DEFAULT NEXTVAL('categoria_id_seq'); /*COMPATIBILIDADE COM H2 MODO POSTGREE*/
insert into categoria(nome, usuario_id) values ('Diversos', (select id from usuario where email = 'admin@email.com'));
insert into categoria(nome, usuario_id) values ('Moradia', (select id from usuario where email = 'admin@email.com'));
insert into categoria(nome, usuario_id) values ('Alimentação', (select id from usuario where email = 'admin@email.com'));
insert into categoria(nome, usuario_id) values ('Transporte', (select id from usuario where email = 'admin@email.com'));
insert into categoria(nome, usuario_id) values ('Saúde', (select id from usuario where email = 'teste@email.com'));
insert into categoria(nome, usuario_id) values ('Investimentos', (select id from usuario where email = 'teste@email.com'));
insert into categoria(nome, usuario_id) values ('Hobbies', (select id from usuario where email = 'teste@email.com'));
insert into categoria(nome, usuario_id) values ('Lazer', (select id from usuario where email = 'teste@email.com'));
/*alter sequence categoria_id_seq restart with 10;*/
DROP SEQUENCE conta_id_seq IF EXISTS;
CREATE SEQUENCE conta_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1; /*COMPATIBILIDADE COM H2 MODO POSTGREE*/
ALTER TABLE conta ALTER COLUMN id SET DEFAULT NEXTVAL('conta_id_seq'); /*COMPATIBILIDADE COM H2 MODO POSTGREE*/
insert into conta(nome, tipo, usuario_id) values ('Dinheiro', 'D', (select id from usuario where email = 'admin@email.com'));
insert into conta(nome, tipo, usuario_id) values ('Débito Banco', 'B', (select id from usuario where email = 'teste@email.com'));
insert into conta(nome, tipo, dia_fechamento, dia_pagamento, usuario_id) values ('Cartão Master Banco', 'C', 26, 5, (select id from usuario where email = 'admin@email.com'));
insert into conta(nome, tipo, dia_fechamento, dia_pagamento, usuario_id) values ('Cartão Visa Loja', 'C', 24, 3, (select id from usuario where email = 'teste@email.com'));
/*alter sequence CONTA_ID_SEQ restart with 5;*/
insert into validador_email(id, email, data, origem, tipo) values ('3789a92c818241a7833a26ba0a29f232', 'fulano@email.com', '2015-12-20 16:00:00.000', '127.0.0.1', 'R'); /*3789a92c-8182-41a7-833a-26ba0a29f232*/
insert into validador_email(id, email, data, origem, tipo, usuario_id) values ('323df6a5a3df4052b43b0111e4c69e6d', 'teste@email.com', '2015-12-20 16:00:00.000', '127.0.0.1', 'S', (select id from usuario where email = 'teste@email.com')); /*323df6a5-a3df-4052-b43b-0111e4c69e6d*/
