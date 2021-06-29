create table note (
    id bigserial not null,
    changed timestamp,
    estimate varchar(255),
    title varchar(50) not null,
    watched boolean not null,
    film_id int8,
    user_id int8,
    primary key (id)
);

alter table if exists note
    add constraint note_user_fk
    foreign key (user_id) references users;