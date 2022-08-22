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