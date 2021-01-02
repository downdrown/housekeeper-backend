-- Insert mock users
insert into users (id, username, firstname, lastname, gender, role) values (10, 'admin', 'Admin', 'Admin', 'FEMALE', 'ADMIN');
insert into users (id, username, firstname, lastname, gender, role) values (20, 'user', 'User', 'User', 'MALE', 'USER');
insert into users (id, username, firstname, lastname, gender, role) values (30, 'guest', 'Guest', 'Guest', 'MALE', 'GUEST');

-- Insert mock credentials
insert into credentials (id, users_fk, password) values (10, 10, '$2a$10$umThkjstS0ZuSaUmvyxkfucpi2wHH3q6y/8U.Sit8SNjVOAPeMjgi'); -- admin
insert into credentials (id, users_fk, password) values (20, 20, '$2a$10$umThkjstS0ZuSaUmvyxkfucpi2wHH3q6y/8U.Sit8SNjVOAPeMjgi'); -- user
insert into credentials (id, users_fk, password) values (30, 30, '$2a$10$umThkjstS0ZuSaUmvyxkfucpi2wHH3q6y/8U.Sit8SNjVOAPeMjgi'); -- guest
