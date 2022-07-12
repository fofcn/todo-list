# delete eureka client dependency
## delete spring cloud eureka client
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

## delete @EnableEurekaClient annotation
```java
@EnableEurekaClient
```

## delete eureka configure from application.yaml
```yaml
eureka:
  instance:
    preferIpAddress: true
    instanceId: ${spring.cloud.client.ip-address}:${server.port}
    hostname: ${spring.cloud.client.ip-address}
    leaseRenewalIntervalInSeconds: 15
  client:
    serviceUrl:
      defaultZone: http://127.0.0.1:40000/eureka/
    registryFetchIntervalSeconds: 1
    refresh:
      enable: true
```