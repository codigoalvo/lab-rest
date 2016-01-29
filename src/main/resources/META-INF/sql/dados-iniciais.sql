DROP SEQUENCE usuario_id_seq IF EXISTS;
CREATE SEQUENCE usuario_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1; /*COMPATIBILIDADE COM H2 MODO POSTGREE*/
ALTER TABLE usuario ALTER COLUMN id SET DEFAULT NEXTVAL('usuario_id_seq'); /*COMPATIBILIDADE COM H2 MODO POSTGREE*/
insert into usuario (email, senha, nome, apelido, tipo, tentativas_login_invalido) values ('admin@email.com','c6zZpZchMLdQZsgllaH64w==', 'Administrador', 'admin', 'ADMIN', 0);
insert into usuario (email, senha, nome, apelido, tipo, tentativas_login_invalido) values ('usuario@email.com','QiVgyySWSwSDtwOkK811AA==', 'Usuario', 'usuario', 'USER', 0);
insert into usuario (email, senha, nome, apelido, tipo, tentativas_login_invalido) values ('codigoalvo@gmail.com','hdBNkyS7WX79G4AK2Kdulw==', 'codigoalvo', 'codigoalvo', 'ADMIN', 0);
insert into usuario (email, senha, nome, apelido, tipo, tentativas_login_invalido) values ('teste@email.com','maKdyBBf0vo52M3ARzOTjQ==', 'Teste', 'teste', 'USER', 0);
/*alter sequence usuario_id_seq restart with 5;*/
DROP SEQUENCE categoria_id_seq IF EXISTS;
CREATE SEQUENCE categoria_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1; /*COMPATIBILIDADE COM H2 MODO POSTGREE*/
ALTER TABLE categoria ALTER COLUMN id SET DEFAULT NEXTVAL('categoria_id_seq'); /*COMPATIBILIDADE COM H2 MODO POSTGREE*/
insert into categoria(nome, usuario_id) values ('Remuneração', (select id from usuario where email = 'admin@email.com'));
insert into categoria(nome, usuario_id) values ('Diversos', (select id from usuario where email = 'admin@email.com'));
insert into categoria(nome, usuario_id) values ('Moradia', (select id from usuario where email = 'admin@email.com'));
insert into categoria(nome, usuario_id) values ('Mercado', (select id from usuario where email = 'admin@email.com'));
insert into categoria(nome, usuario_id) values ('Refeição', (select id from usuario where email = 'admin@email.com'));
insert into categoria(nome, usuario_id) values ('Saúde', (select id from usuario where email = 'admin@email.com'));
insert into categoria(nome, usuario_id) values ('Educação', (select id from usuario where email = 'admin@email.com'));
insert into categoria(nome, usuario_id) values ('Transporte', (select id from usuario where email = 'admin@email.com'));
insert into categoria(nome, usuario_id) values ('Games', (select id from usuario where email = 'admin@email.com'));
insert into categoria(nome, usuario_id) values ('Investimentos', (select id from usuario where email = 'teste@email.com'));
insert into categoria(nome, usuario_id) values ('Hobbies', (select id from usuario where email = 'teste@email.com'));
insert into categoria(nome, usuario_id) values ('Lazer', (select id from usuario where email = 'teste@email.com'));
/*alter sequence categoria_id_seq restart with 11;*/
DROP SEQUENCE conta_id_seq IF EXISTS;
CREATE SEQUENCE conta_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1; /*COMPATIBILIDADE COM H2 MODO POSTGREE*/
ALTER TABLE conta ALTER COLUMN id SET DEFAULT NEXTVAL('conta_id_seq'); /*COMPATIBILIDADE COM H2 MODO POSTGREE*/
insert into conta(nome, tipo, usuario_id) values ('Dinheiro', 'D', (select id from usuario where email = 'admin@email.com'));
insert into conta(nome, tipo, usuario_id) values ('Débito Banco', 'B', (select id from usuario where email = 'admin@email.com'));
insert into conta(nome, tipo, dia_fechamento, dia_pagamento, usuario_id) values ('Cartão Master Banco', 'C', 26, 5, (select id from usuario where email = 'admin@email.com'));
insert into conta(nome, tipo, dia_fechamento, dia_pagamento, usuario_id) values ('Cartão Visa Loja', 'C', 24, 3, (select id from usuario where email = 'teste@email.com'));
/*alter sequence conta_id_seq restart with 5;*/
/*insert into validador_email(id, email, data, origem, tipo) values ('3789a92c818241a7833a26ba0a29f232', 'fulano@email.com', '2015-12-20 16:00:00.000', '127.0.0.1', 'R');*/ /*3789a92c-8182-41a7-833a-26ba0a29f232*/
/*insert into validador_email(id, email, data, origem, tipo, usuario_id) values ('323df6a5a3df4052b43b0111e4c69e6d', 'teste@email.com', '2015-12-20 16:00:00.000', '127.0.0.1', 'S', (select id from usuario where email = 'teste@email.com'));*/ /*323df6a5-a3df-4052-b43b-0111e4c69e6d*/
DROP SEQUENCE planejamento_id_seq IF EXISTS;
CREATE SEQUENCE planejamento_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1; /*COMPATIBILIDADE COM H2 MODO POSTGREE*/
ALTER TABLE planejamento ALTER COLUMN id SET DEFAULT NEXTVAL('planejamento_id_seq'); /*COMPATIBILIDADE COM H2 MODO POSTGREE*/
insert into planejamento(valor, periodo, usuario_id, categoria_id) values (650, '2015-11-01', (select id from usuario where email = 'admin@email.com'), (select id from categoria where nome = 'Diversos'))
insert into planejamento(valor, periodo, usuario_id, categoria_id) values (350, '2015-11-01', (select id from usuario where email = 'admin@email.com'), (select id from categoria where nome = 'Mercado'))
insert into planejamento(valor, periodo, usuario_id, categoria_id) values (500, '2015-12-01', (select id from usuario where email = 'admin@email.com'), (select id from categoria where nome = 'Diversos'));
insert into planejamento(valor, periodo, usuario_id, categoria_id) values (500, '2015-12-01', (select id from usuario where email = 'admin@email.com'), (select id from categoria where nome = 'Mercado'))
insert into planejamento(valor, periodo, usuario_id, categoria_id) values (450, '2015-12-01', (select id from usuario where email = 'admin@email.com'), (select id from categoria where nome = 'Moradia'));
insert into planejamento(valor, periodo, usuario_id, categoria_id) values (450, '2015-12-01', (select id from usuario where email = 'admin@email.com'), (select id from categoria where nome = 'Refeição'));
insert into planejamento(valor, periodo, usuario_id, categoria_id) values (350, '2016-01-01', (select id from usuario where email = 'admin@email.com'), (select id from categoria where nome = 'Moradia'));
insert into planejamento(valor, periodo, usuario_id, categoria_id) values (350, '2016-01-01', (select id from usuario where email = 'admin@email.com'), (select id from categoria where nome = 'Refeição'));
/*alter sequence planejamento_id_seq restart with 9;*/
DROP SEQUENCE transacao_id_seq IF EXISTS;
CREATE SEQUENCE transacao_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1; /*COMPATIBILIDADE COM H2 MODO POSTGREE*/
ALTER TABLE transacao ALTER COLUMN id SET DEFAULT NEXTVAL('transacao_id_seq'); /*COMPATIBILIDADE COM H2 MODO POSTGREE*/
insert into transacao(valor, descricao, tipo, data_transacao, data_pagamento, usuario_id, categoria_id, conta_id) values (1500, 'Salário','R','2015-12-02', '2015-12-06', (select id from usuario where email = 'admin@email.com'), (select id from categoria where nome = 'Remuneração'), (select id from conta where nome = 'Dinheiro'));
insert into transacao(valor, descricao, tipo, data_transacao, data_pagamento, usuario_id, categoria_id, conta_id) values (1200, '13º Salário','R','2015-12-19', '2015-12-19', (select id from usuario where email = 'admin@email.com'), (select id from categoria where nome = 'Remuneração'), (select id from conta where nome = 'Dinheiro'));
insert into transacao(valor, descricao, tipo, data_transacao, data_pagamento, usuario_id, categoria_id, conta_id) values (250, 'Água, Luz, Telefone','D','2015-12-06', '2015-12-06', (select id from usuario where email = 'admin@email.com'), (select id from categoria where nome = 'Moradia'), (select id from conta where nome = 'Dinheiro'));
insert into transacao(valor, descricao, tipo, data_transacao, data_pagamento, usuario_id, categoria_id, conta_id) values (250, 'Crédito Steam (PagSeguro)','D','2015-12-05', '2016-01-05', (select id from usuario where email = 'admin@email.com'), (select id from categoria where nome = 'Games'), (select id from conta where nome = 'Cartão Master Banco'));
insert into transacao(valor, descricao, tipo, data_transacao, data_pagamento, usuario_id, categoria_id, conta_id) values (20, 'eBook - Guia do Mochileiro das Galáxias (Amazon)','D','2015-12-09', '2016-01-05', (select id from usuario where email = 'admin@email.com'), (select id from categoria where nome = 'Educação'), (select id from conta where nome = 'Cartão Master Banco'));
insert into transacao(valor, descricao, tipo, data_transacao, data_pagamento, usuario_id, categoria_id, conta_id) values (45, 'Combustível - Moto','D','2015-12-09', '2015-12-09', (select id from usuario where email = 'admin@email.com'), (select id from categoria where nome = 'Transporte'), (select id from conta where nome = 'Débito Banco'));
insert into transacao(valor, descricao, tipo, data_transacao, data_pagamento, usuario_id, categoria_id, conta_id) values (50, 'Assinatura Devmedia','D','2015-12-18', '2016-01-05', (select id from usuario where email = 'admin@email.com'), (select id from categoria where nome = 'Educação'), (select id from conta where nome = 'Cartão Master Banco'));
insert into transacao(valor, descricao, tipo, data_transacao, data_pagamento, usuario_id, categoria_id, conta_id) values (180, 'Russi','D','2015-12-05', '2016-01-05', (select id from usuario where email = 'admin@email.com'), (select id from categoria where nome = 'Mercado'), (select id from conta where nome = 'Cartão Master Banco'));
insert into transacao(valor, descricao, tipo, data_transacao, data_pagamento, usuario_id, categoria_id, conta_id) values (30, 'Almoço no Shopping (Vivenda do Camarão)','D','2015-12-18', '2015-12-18', (select id from usuario where email = 'admin@email.com'), (select id from categoria where nome = 'Refeição'), (select id from conta where nome = 'Débito Banco'));
insert into transacao(valor, descricao, tipo, data_transacao, data_pagamento, usuario_id, categoria_id, conta_id) values (1500, 'Salário','R','2016-01-02', '2016-01-06', (select id from usuario where email = 'admin@email.com'), (select id from categoria where nome = 'Remuneração'), (select id from conta where nome = 'Dinheiro'));
insert into transacao(valor, descricao, tipo, data_transacao, data_pagamento, usuario_id, categoria_id, conta_id) values (1500, 'Auxilio Refeição','R','2016-01-02', '2016-01-02', (select id from usuario where email = 'admin@email.com'), (select id from categoria where nome = 'Remuneração'), (select id from conta where nome = 'Dinheiro'));
insert into transacao(valor, descricao, tipo, data_transacao, data_pagamento, usuario_id, categoria_id, conta_id) values (250, 'Água, Luz, Telefone','D','2016-01-06', '2016-01-06', (select id from usuario where email = 'admin@email.com'), (select id from categoria where nome = 'Moradia'), (select id from conta where nome = 'Dinheiro'));
insert into transacao(valor, descricao, tipo, data_transacao, data_pagamento, usuario_id, categoria_id, conta_id) values (140, 'Russi','D','2016-01-05', '2016-02-05', (select id from usuario where email = 'admin@email.com'), (select id from categoria where nome = 'Mercado'), (select id from conta where nome = 'Cartão Master Banco'));
insert into transacao(valor, descricao, tipo, data_transacao, data_pagamento, usuario_id, categoria_id, conta_id) values (250, 'Jogos Destiny [PS4] e Rivals [XB1] (GTA Games)','D','2016-02-09', '2016-03-05', (select id from usuario where email = 'admin@email.com'), (select id from categoria where nome = 'Games'), (select id from conta where nome = 'Cartão Master Banco'));
insert into transacao(valor, descricao, tipo, data_transacao, data_pagamento, usuario_id, categoria_id, conta_id) values (190, 'Russi','D','2016-01-20', '2016-02-05', (select id from usuario where email = 'admin@email.com'), (select id from categoria where nome = 'Mercado'), (select id from conta where nome = 'Cartão Master Banco'));
insert into transacao(valor, descricao, tipo, data_transacao, data_pagamento, usuario_id, categoria_id, conta_id) values (1500, 'Salário','R','2016-02-02', '2016-02-06', (select id from usuario where email = 'admin@email.com'), (select id from categoria where nome = 'Remuneração'), (select id from conta where nome = 'Dinheiro'));
insert into transacao(valor, descricao, tipo, data_transacao, data_pagamento, usuario_id, categoria_id, conta_id) values (250, 'Água, Luz, Telefone','D','2016-02-05', '2016-02-05', (select id from usuario where email = 'admin@email.com'), (select id from categoria where nome = 'Moradia'), (select id from conta where nome = 'Dinheiro'));
insert into transacao(valor, descricao, tipo, data_transacao, data_pagamento, usuario_id, categoria_id, conta_id) values (45, 'Farmácia','D','2016-02-09', '2016-03-05', (select id from usuario where email = 'admin@email.com'), (select id from categoria where nome = 'Saúde'), (select id from conta where nome = 'Cartão Master Banco'));
insert into transacao(valor, descricao, tipo, data_transacao, data_pagamento, usuario_id, categoria_id, conta_id) values (250, 'Crédito Steam (PagSeguro)','D','2016-02-09', '2016-03-05', (select id from usuario where email = 'admin@email.com'), (select id from categoria where nome = 'Games'), (select id from conta where nome = 'Cartão Master Banco'));
/*alter sequence transacao_id_seq restart with 9;*/