# 发布
1. 安装docker
2. 安装docker-compose
3. 安装nginx
4. 安装redis
redis docker compose配置:redis.yml
```yaml
version: '2'

services:
  redis:
    image: docker.io/bitnami/redis:6.2
    environment:
      - ALLOW_EMPTY_PASSWORD=yes
      - REDIS_DISABLE_COMMANDS=FLUSHDB,FLUSHALL
    ports:
      - '6379:6379'
    volumes:
      - 'redis_data:/bitnami/redis/data'

volumes:
  redis_data:
    driver: local

```

启动redis
```shell
docker-compose -f redis.yml up -d
```
5. 安装mysql
mysql docker compose配置: mysql.yml
```yaml
version: '2'

services:
  mysql:
    image: mysql:8.0.25

    environment:
      - MYSQL_ROOT_PASSWORD=epam2022.

    ports:
      - "60000:3306"
    volumes:
      - /app/data/mysql:/var/lib/mysql
      - /app/compose/mysql/conf:/etcmysql/conf.d
```
mysql配置文件：conf-file.cnf,放置到/app/compose/mysql/conf目录下
```
[mysqld]
pid-file=/var/run/mysqld/mysqld.pid
socket=/var/run/mysqld/mysqld.sock
datadir=/var/lib/mysql
log-error=/var/log/mysql/error.log
log-bin=/var/lib/mysql/mysql-bin
symbolic-links=0
max_connections=3000
lower_case_table_names=1
binlog_format=ROW
character_set_server=utf8mb4
default_authentication_plugin=mysql_native_password
skip-grant-tables
[mysql]
default-character-set=utf8mb4
```
启动Mysql:
```shell
docker-compose -f mysql.yml up -d
```
6. 本地编译打包
```shell
cd todo-list
mvn clean package -Dmaven.test.skip=true
```

7. 手动传包
```scp
scp C:\Users\Ford_Ji\IdeaProjects\todo-list\todo-auth\target\todo-auth.jar root@47.108.62.173:/app/softs/todo-auth/target
scp C:\Users\Ford_Ji\IdeaProjects\todo-list\todo-gateway\target\todo-gateway.jar root@47.108.62.173:/app/softs/todo-gateway/target
scp C:\Users\Ford_Ji\IdeaProjects\todo-list\todo-auth\target\todo-auth.jar root@47.108.62.173:/app/softs/todo-auth/target
scp C:\Users\Ford_Ji\IdeaProjects\todo-list\todo-task\target\todo-task.jar root@47.108.62.173:/app/softs/todo-task/target
scp C:\Users\Ford_Ji\IdeaProjects\todo-list\todo-usercenter\target\todo-usercenter.jar root@47.108.62.173:/app/softs/todo-usercenter/target
```

8. 打包镜像
```shell
cd /app/softs/todo-gateway
docker build . -t todo-gateway

cd /app/softs/todo-auth
docker build . -t todo-auth

cd /app/softs/todo-usercenter
docker build . -t todo-usercenter

cd /app/softs/todo-task
docker build . -t todo-task
```

9. 服务docker-compose文件编写
```yaml
version: '3.3'
services:
  eureka:
    container_name: eureka
    restart: always
    image: todo-eureka:${tag}
    ports:
      - "40000:40000"
    #volumes:
    #  - /etc/localtime:/etc/localtime:ro
    #  - /etc/timezone:/etc/timezone:ro
    logging:
      driver: "json-file"
      options:
        max-size: "512m"
    environment:
      - SPRING_PROFILES_ACTIVE=prod

  gateway:
    container_name: gateway
    restart: always
    image: todo-gateway:${tag}
    ports:
      - "40001:40001"
    #volumes:
    #  - /etc/localtime:/etc/localtime:ro
    #  - /etc/timezone:/etc/timezone:ro
    logging:
      driver: "json-file"
      options:
        max-size: "512m"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://172.19.143.30:40000/eureka/
      - SPRING_CLOUD_CLIENT_IPADDRESS=172.19.143.30

  auth:
    container_name: auth
    restart: always
    image: todo-auth:${tag}
    ports:
      - "40002:40002"
    #volumes:
    #  - /etc/localtime:/etc/localtime:ro
    #  - /etc/timezone:/etc/timezone:ro
    logging:
      driver: "json-file"
      options:
        max-size: "512m"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://172.19.143.30:40000/eureka/
      - SPRING_SHARDINGSPHERE_DATASOURCE_MASTER_USERNAME=todo_user
      - SPRING_SHARDINGSPHERE_DATASOURCE_SLAVE0_USERNAME=todo_user
      - SPRING_SHARDINGSPHERE_DATASOURCE_MASTER_URL=jdbc:mysql://172.19.143.30:60000/todo_auth?useUnicode=true&characterEncoding=utf8&characterSetResults=utf8
      - SPRING_SHARDINGSPHERE_DATASOURCE_SLAVE0_URL=jdbc:mysql://172.19.143.30:60000/todo_auth?useUnicode=true&characterEncoding=utf8&characterSetResults=utf8
      - SPRING_CLOUD_CLIENT_IPADDRESS=172.19.143.30


  task:
    container_name: task
    restart: always
    image: todo-task:${tag}
    ports:
      - "40004:40004"
    #volumes:
    #  - /etc/localtime:/etc/localtime:ro
    #  - /etc/timezone:/etc/timezone:ro
    logging:
      driver: "json-file"
      options:
        max-size: "512m"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://172.19.143.30:40000/eureka/
      - SPRING_SHARDINGSPHERE_DATASOURCE_MASTER_USERNAME=todo_user
      - SPRING_SHARDINGSPHERE_DATASOURCE_SLAVE0_USERNAME=todo_user
      - SPRING_CLOUD_CLIENT_IPADDRESS=172.19.143.30

  usercenter:
    container_name: usercenter
    restart: always
    image: todo-usercenter:${tag}
    ports:
      - "40003:40003"
    #volumes:
    #  - /etc/localtime:/etc/localtime:ro
    #  - /etc/timezone:/etc/timezone:ro
    logging:
      driver: "json-file"
      options:
        max-size: "512m"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - EUREKA_CLIENT_SERVICEURL_DEFAULTZONE=http://172.19.143.30:40000/eureka/
      - SPRING_SHARDINGSPHERE_DATASOURCE_MASTER_USERNAME=todo_user
      - SPRING_SHARDINGSPHERE_DATASOURCE_SLAVE0_USERNAME=todo_user
      - SPRING_SHARDINGSPHERE_DATASOURCE_MASTER_URL=jdbc:mysql://172.19.143.30:60000/todo_auth?useUnicode=true&characterEncoding=utf8&characterSetResults=utf8
      - SPRING_SHARDINGSPHERE_DATASOURCE_SLAVE0_URL=jdbc:mysql://172.19.143.30:60000/todo_auth?useUnicode=true&characterEncoding=utf8&characterSetResults=utf8
      - SPRING_CLOUD_CLIENT_IPADDRESS=172.19.143.30
```

10. 服务容器启动
```shell
export tag=latest
docker-compose -f /app/compose/docker-compose.yml up -d eureka
docker-compose -f /app/compose/docker-compose.yml up -d gateway
docker-compose -f /app/compose/docker-compose.yml up -d auth
docker-compose -f /app/compose/docker-compose.yml up -d usercenter
docker-compose -f /app/compose/docker-compose.yml up -d task
```