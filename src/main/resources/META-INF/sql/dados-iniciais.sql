insert into usuario (id, login, senha, nome, email, tipo, tentativas_login_invalido) values (1, 'admin','c6zZpZchMLdQZsgllaH64w==', 'Administrador', 'admin@email.com', 'ADMIN', 0);
alter sequence SEQ_USUARIO_ID restart with 2;
insert into categoria(id, nome) values (1, 'Moradia');
insert into categoria(id, nome) values (2, 'Alimentação');
insert into categoria(id, nome) values (3, 'Transporte');
insert into categoria(id, nome) values (4, 'Saúde');
insert into categoria(id, nome) values (5, 'Investimentos');
insert into categoria(id, nome) values (6, 'Lazer');
alter sequence SEQ_CATEGORIA_ID restart with 7;