# todo list 
这是一个Java后端练习项目。

# 模块设计
| 编码              | 模块名     | 功能            |
|-----------------|---------|---------------|
| todo-common     | 通用      | 通用组件简单封装      |
| todo-guid       | 全局唯一ID  | 支撑业务ID        |
| todo-eureka     | 注册中心    | 服务注册与发现（废弃）   |
| todo-gateway    | 网关      | 认证与路由    （废弃） |
| todo-auth       | 认证与授权领域 | 认证与授权         |
| todo-usercenter | 用户领域    | 用户领域          |
| todo-task       | 任务领域    | 任务领域          |

# todo-common 模块
todo-common模块是一些通用模块，通过对现有技术的一些封装来支持快速开发应用的目的。

## 子模块介绍
| #   | 名称 |  用途   |
|-----|--|-----|
|     | common-core	 |   核心通用模块  |
|     | common-crawler	 |  爬虫   |
|     | common-debezium	 |  数据同步   |
|     | common-domain	 |  DDD领域封装   |
|     | common-encryption	 |  对称加密、非对称加密和哈希   |
|     |common-jpa  |  JPA模块   |
|     | common-kafka |  KAFKA模块    |
|     | common-log |  日志统一   |
|     | common-rabbitmq |  RabbitMQ封装   |
|     | common-redis |  Redis封装   |
|     | common-socket |  Socket封装，目前只封装了gRPC，TODO：UDP封装（使用Netty）    |
|     | common-web |  基于Spring boot的Web封装   |

### DDD 实践
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