# task SQL scripts
```sql
create database `todo_task` default charset utf8mb4;
use `todo_task`;
create table `task` (
  id bigint not null primary key auto_increment,
  user_id bigint not null comment 'who create the task',
  title varchar(50) not null default '' comment 'task title',
  sub_title varchar(200) not null default '' comment 'task sub-title',
  deleted tinyint not null default 0 comment 'delete tag, 0 for normal, 1 for deleted',
  status tinyint not null default 0 comment 'status tag, 1 for TODO, 2 for DONE',
  create_time datetime not null comment 'first create time of task',
  last_modified_time datetime not null comment 'last modified time of task'
)engine=innodb default charset utf8mb4;
```