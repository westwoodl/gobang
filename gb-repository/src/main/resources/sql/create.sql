
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

-- 创建棋盘记录表
create table gb_go (
    go_id int primary key auto_increment,
    create_time timestamp not null default now(),
    modify_time timestamp,
    black_user_id int not null,
    white_user_Id int not null,
    go_context varchar(100) not null,
    roomId int not null,
    is_end boolean not null ,
    end_time timestamp,
    last_user_id int,
    go_result varchar(100)
);
create index query_go_by_user_index on gb_go(black_user_id, white_user_Id);