insert into usuario (id, login, senha, nome, email, tipo, tentativas_login_invalido) values (1, 'admin','c6zZpZchMLdQZsgllaH64w==', 'Administrador', 'admin@email.com', 'ADMIN', 0);
insert into usuario (id, login, senha, nome, email, tipo, tentativas_login_invalido) values (2, 'usuario','QiVgyySWSwSDtwOkK811AA==', 'Usuario', 'usuario@email.com', 'USER', 0);
insert into usuario (id, login, senha, nome, email, tipo, tentativas_login_invalido) values (3, 'codigoalvo','hdBNkyS7WX79G4AK2Kdulw==', 'codigoalvo', 'codigoalvo@gmail.com', 'ADMIN', 0);
insert into usuario (id, login, senha, nome, email, tipo, tentativas_login_invalido) values (4, 'teste','maKdyBBf0vo52M3ARzOTjQ==', 'Teste', 'teste@email.com', 'USER', 0);
/*alter sequence SEQ_USUARIO_ID restart with 5;*/
insert into categoria(id, nome, usuario_id) values (1, 'Diversos', 1);
insert into categoria(id, nome, usuario_id) values (2, 'Moradia', 1);
insert into categoria(id, nome, usuario_id) values (3, 'Alimentação', 1);
insert into categoria(id, nome, usuario_id) values (4, 'Transporte', 1);
insert into categoria(id, nome, usuario_id) values (5, 'Saúde', 1);
insert into categoria(id, nome, usuario_id) values (6, 'Investimentos', 1);
insert into categoria(id, nome, usuario_id) values (7, 'Hobbies', 1);
insert into categoria(id, nome, usuario_id) values (8, 'Lazer', 1);
/*alter sequence SEQ_CATEGORIA_ID restart with 9;*/
insert into pagamento(id, codigo, nome, tipo, usuario_id) values (1, 'DINHEIRO', 'Dinheiro', 'D', 1);
insert into pagamento(id, codigo, nome, tipo, usuario_id) values (2, 'DEBITO', 'Débito Banco', 'B', 1);
insert into pagamento(id, codigo, nome, tipo, dia_fechamento, dia_pagamento, usuario_id) values (3, 'CCMCBCO', 'Cartão Master Banco', 'C', 26, 5, 1);
insert into pagamento(id, codigo, nome, tipo, dia_fechamento, dia_pagamento, usuario_id) values (4, 'CCVILJ', 'Cartão Visa Loja', 'C', 24, 3, 1);