
create database if not exists gobang;
-- alter database character set "utf-8";
use gobang;
drop table if exists gb_user;
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
insert into gb_user
values (1, 'xrc', 'xrc', '123', 1, now(), now()),
       (2, 'xrc1', 'xrc1', '123', 1, now(), now());

drop table if exists gb_go;
-- 创建棋盘记录表
create table gb_go (
    go_id int primary key auto_increment,
    create_time timestamp not null default now(),
    modify_time timestamp,
    black_user_id int not null  comment '执黑者',
    white_user_Id int not null  comment '执白',
    go_context varchar(100) not null comment '棋盘json',
    room_id int not null  comment '房间id',
    is_end int comment '对局是否结束',
    end_time timestamp  comment '对局结束时间',
    last_user_id int  comment '最后落子玩家',
    go_result varchar(100) comment '结果json'
);
create index query_go_by_white_user_index on gb_go(white_user_Id);
create index query_go_by_black_user_index on gb_go(black_user_id);