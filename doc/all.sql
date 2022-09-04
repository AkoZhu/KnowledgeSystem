# Create DATABASE wiki;
# Use wiki;

# 电子书表
drop table if exists `ebook`;
create table `ebook` (
  `id` bigint not null comment 'Id',
  `name` varchar(50) comment 'Name',
  `category1_id` bigint comment 'Category1',
  `category2_id` bigint comment 'Category2',
  `description` varchar(200) comment 'Description',
  `cover` varchar(200) comment 'Cover',
  `doc_count` int not null default 0 comment 'Document Count',
  `view_count` int not null default 0 comment 'View Count',
  `vote_count` int not null default 0 comment 'Vote Count',
  primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='e-Book';

insert into `ebook` (id, name, description) values (1, 'Spring Boot Introduction', 'Java development for beginner, the best preferred framework for enterprise-level application development');
insert into `ebook` (id, name, description) values (2, 'Vue Introduction', 'Vue development for beginner, the best preferred framework for enterprise-level application development');
insert into `ebook` (id, name, description) values (3, 'Python Introduction', 'Python development for beginner, the best preferred framework for enterprise-level application development');
insert into `ebook` (id, name, description) values (4, 'Mysql Introduction', 'MySQL development for beginner, the best preferred framework for enterprise-level application development');
insert into `ebook` (id, name, description) values (5, 'Oracle Introduction', 'Oracle development for beginner, the best preferred framework for enterprise-level application development');

select * from `ebook`;
update ebook set cover = '/image/cover2.png' where id = 1;
update ebook set cover = '/image/cover1.png' where id = 2;



drop table if exists `test`;
create table `test` (
    `id` bigint not null comment 'id',
    `name` varchar(50) comment 'name',
    `password` varchar(50) comment 'Password',
    primary key (`id`)
) engine = innodb default charset = utf8mb4 comment = 'Test';

insert into `test` (id, name, password) values(1, 'Test', 'password');

drop table if exists `demo`;
create table `demo`(
    `id` bigint not null comment 'id',
    `name` varchar(50) comment 'name',
    primary key(`id`)
) engine = innodb default charset= utf8mb4 comment = 'Test';

insert into `demo` (id, name) values (1, 'Test');


-- Create Category table
drop table if exists `category`;
create table `category` (
  `id` bigint not null comment 'id',
  `parent` bigint not null default 0 comment 'parentId',
  `name` varchar(50) not null comment 'Name',
  `sort` int comment 'order',
  primary key (`id`)
) engine=innodb default charset=utf8mb4 comment='Category';

insert into `category` (id, parent, name, sort) values (100, 000, 'Frontend Development', 100);
insert into `category` (id, parent, name, sort) values (101, 100, 'Vue', 101);
insert into `category` (id, parent, name, sort) values (102, 100, 'HTML & CSS', 102);
insert into `category` (id, parent, name, sort) values (200, 000, 'Java', 200);
insert into `category` (id, parent, name, sort) values (201, 200, 'Basic application', 201);
insert into `category` (id, parent, name, sort) values (202, 200, 'Web application framework', 202);
insert into `category` (id, parent, name, sort) values (300, 000, 'Python', 300);
insert into `category` (id, parent, name, sort) values (301, 300, 'Basic application', 301);
insert into `category` (id, parent, name, sort) values (302, 300, 'Advanced application', 302);
insert into `category` (id, parent, name, sort) values (400, 000, 'Database', 400);
insert into `category` (id, parent, name, sort) values (401, 400, 'MySQL', 401);
insert into `category` (id, parent, name, sort) values (500, 000, 'Others', 500);
insert into `category` (id, parent, name, sort) values (501, 500, 'Server', 501);
insert into `category` (id, parent, name, sort) values (502, 500, 'Development Tool', 502);
insert into `category` (id, parent, name, sort) values (503, 500, 'Server-side Language', 503);

select * from `category`;


-- Create Document table
drop table if exists `doc`;
create table `doc` (
    `id` bigint not null comment 'id',
    `ebook_id` bigint not null default 0 comment 'Ebook id',
    `parent` bigint not null default 0 comment 'Parent id',
    `name` varchar(50) not null comment 'Name',
    `sort` int comment 'Order',
    `view_count` int default 0 comment 'View count',
    `vote_count` int default 0 comment 'Like count',
    primary key(`id`)
) engine=innodb default charset= utf8mb4 comment='document';

-- Test
insert into `doc` (id, ebook_id, parent, name, sort, view_count, vote_count) values (1, 1, 0, 'Document1', 1, 0, 0)
insert into `doc` (id, ebook_id, parent, name, sort, view_count, vote_count) values (2, 1, 1, 'Document1.1', 1, 0, 0);
insert into `doc` (id, ebook_id, parent, name, sort, view_count, vote_count) values (3, 1, 0, 'Document2', 2, 0, 0);
insert into `doc` (id, ebook_id, parent, name, sort, view_count, vote_count) values (4, 1, 3, 'Document2.1', 1, 0, 0);
insert into `doc` (id, ebook_id, parent, name, sort, view_count, vote_count) values (5, 1, 3, 'Document2.2', 2, 0, 0);
insert into `doc` (id, ebook_id, parent, name, sort, view_count, vote_count) values (6, 1, 5, 'Document2.2.1', 1, 0, 0);

update `doc` set parent = 1 where `id`= 2;
select * from `doc`;


-- Create content table
drop table if exists `content`;
create table `content`(
  `id` bigint not null comment 'Document Id',
  `content` mediumtext not null comment 'content',
  primary key(`id`)
) engine=innodb default charset=utf8mb4 comment='Document content';

select * from `content`;
select * from `doc`;


-- Create User Table
drop table if exists `user`;
create table `user` (
    `id` bigint not null comment 'ID',
    `login_name` varchar(50) not null comment 'Login Name',
    `name` varchar(50) comment 'Name',
    `password` char(32) not null comment 'password',
    primary key(`id`),
    unique key `login_name_unique` (`login_name`)
) engine=innodb default charset=utf8mb4 comment='User';

# username:test, password:test123
insert into `user`(id, `login_name`, `name`, `password`) value(1, 'test', 'test',
'7354a1d413535a6c0dc5c209e198d799');
select * from `user`;




