# task SQL scripts
```sql
create database `todo_task` default charset utf8mb4;
use `todo_task`;
create table `task` (
  id bigint not null primary key,
  user_id bigint not null comment 'who create the task',
  title varchar(50) not null default '' comment 'task title',
  sub_title varchar(200) not null default '' comment 'task sub-title',
  create_time datetime not null comment 'first create time of task',
  last_modified_time datetime not null comment 'last modified time of task'
)engine=innodb default charset ut8mb4;
```