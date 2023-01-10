DROP TABLE IF EXISTS users cascade;
DROP TABLE IF EXISTS lessons cascade;

create table users(
ID SERIAL PRIMARY KEY,
name varchar(25) NOT NULL,
email varchar(25) NOT NULL UNIQUE,
password varchar(250) NOT NULL,
role varchar(5) NOT NULL
);

create table lessons(
ID SERIAL PRIMARY KEY,
student_id integer,
student_name varchar(80),
date_time_from timestamp NOT NULL,
date_time_to timestamp NOT NULL,
description varchar(250),
price float,
paid boolean,
reserved boolean,
canceled boolean,

FOREIGN KEY (student_id)
REFERENCES users(ID) ON DELETE CASCADE
);

/* The un-crypted password for all users is pass111 */
insert into users(name, email, password, role) values('Professor', 'prof@gmail.com', '$2a$04$GGTsxpDhsbZS9gDKUUpqPOxXuc/9PU1h56ueiFHk68PhY0flyJJmy', 'ADMIN');
insert into users(name, email, password, role) values('Student_one', 'std_one@gmail.com', '$2a$04$GGTsxpDhsbZS9gDKUUpqPOxXuc/9PU1h56ueiFHk68PhY0flyJJmy', 'USER');
insert into users(name, email, password, role) values('Student_two', 'std_two@gmail.com', '$2a$04$GGTsxpDhsbZS9gDKUUpqPOxXuc/9PU1h56ueiFHk68PhY0flyJJmy', 'USER');