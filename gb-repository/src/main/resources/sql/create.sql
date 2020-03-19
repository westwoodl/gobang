
create database if not exists gobang;
-- alter database character set "utf-8";
use gobang;
create table gb_user (
    user_id int primary key auto_increment,
    user_name varchar(20) not null ,
    user_account varchar(20) unique not null ,
    user_password varchar(20) not null ,
    user_power int not null default 0,
    create_time timestamp not null ,
    modify_time timestamp
);
-- 创建用户昵称index
create index user_name_index on gb_user(user_name);