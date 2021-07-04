insert into roles(name)
values ('ROLE_USER'),
       ('ROLE_ADMIN');


insert into users(id, username, first_name, last_name, email, password)
values(1, 'u', 'test', 'user', 'a@a.ru', '$2a$08$9kskP6AygP2gWUmMRYBYvecfcvT73FhWdnBFPPDHlMLI4P3RJ55cu');

insert into user_roles(user_id, role_id)
values (1, 1),
       (1, 2);

insert into notes(id, title, watched, estimate, changed, user_id)
values (1, 'first note', 'true', 'GOOD', now(), 1),
       (2, 'second note', 'false', 'NOT_ESTIMATE', now(), 1),
       (3, 'third note', 'true', 'GOOD', now(), 1);