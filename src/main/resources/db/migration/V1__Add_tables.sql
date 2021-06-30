create sequence hibernate_sequence start 1 increment 1;

create table roles (
	id bigserial not null,
	name varchar(50) not null,
	primary key (id),
	CONSTRAINT UK_roles_name UNIQUE (name)
);

create table users (
	id bigserial not null,
	username varchar(255) not null,
	first_name varchar(255) not null,
    last_name varchar(255) not null,
    email varchar(255) not null,
    password varchar(255) not null,
	primary key (id),
	constraint UK_users_username unique (username),
	constraint UK_users_email unique (email)
);

create table user_roles (
	user_id int8 not null,
	role_id int8 not null
);

create table notes (
    id bigserial not null,
    changed timestamp,
    estimate varchar(255),
    title varchar(50) not null,
    watched boolean not null,
    user_id int8,
    primary key (id)
);

alter table if exists user_roles
	add constraint user_roles_roles_fk
	foreign key (role_id) references roles;

alter table if exists user_roles
	add constraint user_roles_users_fk
	foreign key (user_id) references users;

alter table if exists notes
    add constraint notes_user_fk
    foreign key (user_id) references users;