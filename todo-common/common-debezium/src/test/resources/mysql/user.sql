create table `sys_user`(
    `id` int not null primary key auto_increment,
    `username` varchar(32) not null default '' comment 'username',
    `password` varchar(64) not null default '' comment 'password'
) engine=innodb default charset utf8mb4;

begin;
insert into `sys_user` (`id`, `username`, `password`) values (1, 'test_user1', 'test_pwd1');
insert into `sys_user` (`id`,`username`, `password`) values (2, 'test_user2', 'test_pwd2');
insert into `sys_user` (`id`,`username`, `password`) values (3, 'test_user3', 'test_pwd3');
insert into `sys_user` (`id`,`username`, `password`) values (4, 'test_user4', 'test_pwd4');
commit;