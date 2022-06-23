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

11. 配置nginx
nginx.conf配置：
```

worker_processes  1;
events {
    worker_connections  1024;
}


http {
    include       mime.types;
    default_type  application/octet-stream;

    log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
                      '$status $body_bytes_sent "$http_referer" '
                      '"$http_user_agent" "$http_x_forwarded_for"';
    log_format  combinedio  '$remote_addr - $remote_user [$time_local] '
                            '"$request" $status  '
                            '"$http_referer" "$http_user_agent" $request_length $request_time $upstream_response_time $server_addr';
     sendfile        on;
    tcp_nodelay     on;
    server_tokens   off;
    keepalive_timeout  65;
    gzip             on;
    gzip_min_length  1000;
    gzip_proxied     expired no-cache no-store private auth;
    gzip_buffers     4 8k;
    gzip_types       text/plain application/x-javascript text/css application/xml text/javascript;
    gzip_disable     "MSIE [1-6]\.";


    fastcgi_send_timeout      600s;
    proxy_connect_timeout     600s;
    proxy_read_timeout        600s;
    proxy_send_timeout        600s;
    client_body_buffer_size   20M;
    client_max_body_size      1024M;      #设置允许客户端请求的最大的单个文件字节数
    client_header_buffer_size 20M;      #指定来自客户端请求头的headebuffer大小
    include  conf.d/*.conf;
}

```

todo list配置：todo.epam.conf
```
upstream todo.epam.com {
    ip_hash;
    server 172.19.143.30:40001 weight=5 max_fails=3 fail_timeout=30;
}

server {
    listen 80;
    server_name 47.108.62.173;
    #root         /app/webroot;
    index        index.html index.php index.htm;
    access_log   /app/applog/nginx/logs/todo.epam.access.log  main;
    error_log    /app/applog/nginx/logs/todo.epam.error.log;
    # added by Chris Yang for frontend
    location /chris_yang {
        alias /app/webroot/chris_yang;
        try_files $uri $uri/ /chris_yang/index.html;
        autoindex on;
    }
    # added by Chris Yang for frontend
    location /vicky_he {
        alias /app/webroot/vicky_he;
        try_files $uri $uri/ /vicky_he/index.html;
        autoindex on;
        index index.html;
    }
    location /api {
        proxy_pass       http://todo.epam.com;
        proxy_set_header Host  $host:$server_port;
        proxy_set_header X-Real-IP   $remote_addr;
        proxy_set_header REMOTE-HOST $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Scheme $scheme;
        proxy_set_header X-Forwarded-Proto $scheme;
        proxy_set_header X-NginX-Proxy true;
        proxy_hide_header X-Powered-By;
        proxy_hide_header Vary;
        proxy_redirect off;
    }
}

```