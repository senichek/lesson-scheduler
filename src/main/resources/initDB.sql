DROP TABLE IF EXISTS users cascade;

create table users(
ID SERIAL PRIMARY KEY,
name varchar(25) NOT NULL,
email varchar(25) NOT NULL UNIQUE,
password varchar(250) NOT NULL,
role varchar(5) NOT NULL
);

/* The un-crypted password for all users is pass111 */
insert into users(name, email, password, role) values('Professor', 'prof@gmail.com', '$2a$04$GGTsxpDhsbZS9gDKUUpqPOxXuc/9PU1h56ueiFHk68PhY0flyJJmy', 'ADMIN');