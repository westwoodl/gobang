
create database if not exists gobang;
-- alter database character set "utf-8";
use gobang;
drop table if exists gb_user;
create table gb_user (
    user_id int primary key auto_increment,
    user_name varchar(20) not null ,
    user_account varchar(20) unique not null ,
    user_password varchar(20) not null ,
    user_power int default 0,
    create_time timestamp not null ,
    modify_time timestamp,
    img varchar(100),
    autograph varchar(50),
    friend varchar(50),
    status int default 0
);
-- 创建用户昵称index
create index user_name_index on gb_user(user_name);
insert into gb_user
values (1, 'xrc', 'xrc', '123', 1, now(), now(), null, null, null, null),
       (2, 'xrc1', 'xrc1', '123', 1, now(), now(), null, null, null, null);

drop table if exists gb_go;
-- 创建棋盘记录表
create table gb_go (
    go_id int primary key auto_increment,
    create_time timestamp not null default now(),
    modify_time timestamp,
    black_user_id int not null  comment '执黑者',
    white_user_Id int not null  comment '执白',
    go_context text not null comment '棋盘json',
    is_end int comment '对局是否结束',
    end_time timestamp  comment '对局结束时间',
    last_user_id int  comment '最后落子玩家',
    go_result varchar(100) comment '结果json',
    go_status int not null comment '对局状态'
);
create index query_go_by_white_user_index on gb_go(white_user_Id);
create index query_go_by_black_user_index on gb_go(black_user_id);
alter table gb_go add column go_type int not null comment ' 围棋1 五子棋2';

-- 创建房间表
drop table if exists gb_room;
create table gb_room (
    room_id int primary key auto_increment,
    create_time timestamp not null default now(),
    modify_time timestamp,
    room_number int comment '房间当前人数',
    create_user int not null comment '房间创造者',
    opponents int comment '对手',
    watch_user varchar(100) comment '观战者',
    go_id int comment '对局id',
    room_status int not null comment '房间状态',
    room_name varchar(20) not null comment '房间名',
    room_password varchar(20) comment '房间密码'
);
create index query_room_by_create_user_index on gb_room(create_user);
-- insert into gb_room values (1, null,now(),1,1,2,null,null,1,'fuckYouRoom','123');


-- 好友聊天表
drop table if exists gb_chat;
create table gb_chat(
    chat_id int primary key auto_increment,
    create_time timestamp not null default now(),
    modify_time timestamp,
    user_user_date

)