-- Insert mock credentials
insert into credentials (id, users_fk, password, salt) values (10, 10, 'a-password', 'a-salt'); -- admin
insert into credentials (id, users_fk, password, salt) values (20, 20, 'a-password', 'a-salt'); -- user
insert into credentials (id, users_fk, password, salt) values (30, 30, 'a-password', 'a-salt'); -- guest
