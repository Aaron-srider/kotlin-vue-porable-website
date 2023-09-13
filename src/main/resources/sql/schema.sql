create table if not exists `user` (
    id int auto_increment primary key,
    username varchar(50) null,
    password varchar(50) null
);

create table if not exists `record` (
    id int auto_increment primary key,
    user_id int(11) null,
    amount decimal(11, 4) null,
    ctime varchar(50) null,
    utime varchar(50) null
);