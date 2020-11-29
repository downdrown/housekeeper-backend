-- Insert mock users
insert into users (username, firstname, lastname, role) values ('admin', 'Admin', 'Admin', 'ADMIN');
insert into users (username, firstname, lastname, role) values ('user', 'User', 'User', 'User');
insert into users (username, firstname, lastname, role) values ('guest', 'Guest', 'Guest', 'GUEST');

-- Insert mock credentials
insert into credentials (users_fk, password, salt) values ('admin', 'a-password', 'a-salt');
insert into credentials (users_fk, password, salt) values ('user', 'a-password', 'a-salt');
insert into credentials (users_fk, password, salt) values ('guest', 'a-password', 'a-salt');
