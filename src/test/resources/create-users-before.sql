delete from user_roles;
delete from users;

alter sequence users_id_seq restart with 1;

insert into users(username, first_name, last_name, email, password)
values('u', 'test', 'user', 'a@a.ru', '$2a$08$9kskP6AygP2gWUmMRYBYvecfcvT73FhWdnBFPPDHlMLI4P3RJ55cu');

insert into user_roles(user_id, role_id)
values(1, 1);




