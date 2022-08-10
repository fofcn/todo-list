# 1. todo list 
这是一个Java后端练习项目。

# 2. 模块设计
| 编码              | 模块名     | 功能            |
|-----------------|---------|---------------|
| todo-common     | 通用      | 通用组件简单封装      |
| todo-guid       | 全局唯一ID  | 支撑业务ID        |
| todo-eureka     | 注册中心    | 服务注册与发现（废弃）   |
| todo-gateway    | 网关      | 认证与路由    （废弃） |
| todo-auth       | 认证与授权领域 | 认证与授权         |
| todo-usercenter | 用户领域    | 用户领域          |
| todo-task       | 任务领域    | 任务领域          |

# 3. todo-common 模块
todo-common模块是一些通用模块，通过对现有技术的一些封装来支持快速开发应用的目的。

## 3.1 子模块全览
| #   | 名称 |  用途   |
|-----|--|-----|
|  1   | common-core	 |   核心通用模块  |
|  2  | common-crawler	 |  爬虫   |
|  3   | common-debezium	 |  数据同步   |
|  4   | common-domain	 |  DDD领域封装   |
|  5   | common-encryption	 |  对称加密、非对称加密和哈希   |
|  6   | common-jpa  |  JPA模块   |
|  7   | common-kafka |  KAFKA模块    |
|  8   | common-log |  日志统一   |
|  9   | common-rabbitmq |  RabbitMQ封装   |
|  10   | common-redis |  Redis封装   |
|  11   | common-socket |  Socket封装，目前只封装了gRPC，TODO：UDP封装（使用Netty）    |
|  12   | common-web |  基于Spring boot的Web封装   |

## 3.2 common-core

## 3.3 common-crawler

## 3.4 common-debezium

## 3.5 common-domain

## 3.6 common-encryption

## 3.7 common-jpa

## 3.8 common-kafka

## 3.9 common-log

## 3.10 common-rabbitmq

## 3.11 common-redis

## 3.12 common-socket

## 3.13 common-socket

## 3.14 common-web

# 4. 微服务与DDD 实践
## 4.1. 模式列表
1. 六边形架构/端口适配器
2. CQRS(命令与查询职责分离)
3. DDD 子域划分
4. API Gateway
5. Database per Service
6. Saga
7. API Composition
8. Domain Event
9. Event Soucing
10. BFF
11. Service Instance per Container
12. Service Deployment
13. Event Sourcing

## 4.2. DDD 分包设计
```
src
├───main
│   ├───java
│   │   └───com
│   │       └───epam
│   │           └───todo
│   │               └───task
│   │                   │   TaskApplication.java
│   │                   │   
│   │                   ├───adapter
│   │                   │   ├───im
│   │                   │   ├───mobile
│   │                   │   │   ├───android
│   │                   │   │   └───ios
│   │                   │   ├───wap
│   │                   │   └───web
│   │                   │           TaskController.java
│   │                   │           
│   │                   ├───app
│   │                   │   │   TaskServiceImpl.java
│   │                   │   │   
│   │                   │   ├───assembler
│   │                   │   │       TaskDTOAssembler.java
│   │                   │   │       
│   │                   │   └───executor
│   │                   │       │   TaskCreateCmdExe.java
│   │                   │       │   TaskDelCmdExe.java
│   │                   │       │   TaskUpdateCmdExe.java
│   │                   │       │   TaskUpdateStatusCmdExe
│   │                   │       │   
│   │                   │       └───query
│   │                   │               TaskListQueryCmdExe,java
│   │                   │               TaskQueryCmdExec.java
│   │                   │               
│   │                   ├───client
│   │                   │   ├───api
│   │                   │   │       TaskService.java
│   │                   │   │       
│   │                   │   └───dto
│   │                   │       ├───cmd
│   │                   │       │       TaskCreateCmd.java
│   │                   │       │       TaskDelCmd.java
│   │                   │       │       TaskUpdateCmd.java
│   │                   │       │       TaskUpdateStatusCm
│   │                   │       │       
│   │                   │       ├───data
│   │                   │       │       TaskCreateDTO.java
│   │                   │       │       TaskDTO.java
│   │                   │       │       TaskListDTO.java
│   │                   │       │       
│   │                   │       └───query
│   │                   │               TaskListQuery.java
│   │                   │               TaskQuery.java
│   │                   │               
│   │                   ├───domain
│   │                   │   ├───entity
│   │                   │   │       TaskEntity.java
│   │                   │   │       
│   │                   │   ├───gateway
│   │                   │   │       TaskGatewayService.java
│   │                   │   │       
│   │                   │   ├───service
│   │                   │   │       TaskDomainService.java
│   │                   │   │       
│   │                   │   └───valueobject
│   │                   │           DeleteEnum.java
│   │                   │           TaskStatus.java
│   │                   │           TaskTitle.java
│   │                   │           
│   │                   └───infrastructure
│   │                       │   TaskGatewayServiceImpl.java
│   │                       │   
│   │                       ├───model
│   │                       │   │   StateEnum.java
│   │                       │   │   Task.java
│   │                       │   │   
│   │                       │   └───converter
│   │                       │           TaskEntityConverter.java
│   │                       │           
│   │                       └───repository
│   │                               TaskRepository.java
│   │                               
│   └───resources
│           application-dev.yml
│           application-prod.yml
│           application-test.yml
│           application.yml
│           
└───test
    ├───java
    │   └───com
    │       └───epam
    └───resources
```


# 部署
## 1. Jar包
```shell
git clone https://github.com/fofcn/todo-list.git
cd todo-list
mvn clean package
```
## 2. Docker
```shell
git clone https://github.com/fofcn/todo-list.git
cd todo-list
mvn clean package -Ddockerfile.skip=false

# Startup services using docs/3. devops/docker/docker-compose.yml 
```
## 3. kubernetes
```shell
git clone https://github.com/fofcn/todo-list.git
cd todo-list
mvn clean package -Ddockerfile.skip=false

# Startup services using docs/3. devops/k8s/yaml/in-one yaml files
```
## 4. kubernetes + Istio
```shell
git clone https://github.com/fofcn/todo-list.git
cd todo-list
mvn clean package -Ddockerfile.skip=false

# Startup services using docs/3. devops/k8s&istio/yaml/in-one yaml files
```
