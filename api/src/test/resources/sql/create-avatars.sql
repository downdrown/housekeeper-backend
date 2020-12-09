-- Insert mock avatars
insert into avatars (id, users_fk, content, content_type, original_file_name, size) values (10, 10, file_read('classpath:img/placeholder.png'), 'image/png', 'placeholder.png', 255);
insert into avatars (id, users_fk, content, content_type, original_file_name, size) values (20, 20, file_read('classpath:img/placeholder.png'), 'image/png', 'placeholder.png', 255);
insert into avatars (id, users_fk, content, content_type, original_file_name, size) values (30, 30, file_read('classpath:img/placeholder.png'), 'image/png', 'placeholder.png', 255);
