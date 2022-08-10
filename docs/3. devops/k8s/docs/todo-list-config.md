# todo list config map

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: todo-list
  namespace: todo
data:
  SPRING_PROFILES_ACTIVE: prod
  EUREKA_CLIENT_SERVICEURL_DEFAULTZONE: http://eureka-clusterIp:40000/eureka/
  SPRING_SHARDINGSPHERE_DATASOURCE_MASTER_USERNAME: jdbc:mysql://172.19.143.30:60000/todo_auth?useUnicode=true&characterEncoding=utf8&characterSetResults=utf8
  SPRING_SHARDINGSPHERE_DATASOURCE_SLAVE0_USERNAME: jdbc:mysql://172.19.143.30:60000/todo_auth?useUnicode=true&characterEncoding=utf8&characterSetResults=utf8
  SPRING_SHARDINGSPHERE_DATASOURCE_MASTER_URL: jdbc:mysql://172.19.143.30:60000/todo.auth?useunicode:true&characterencoding:utf8&charactersetresults:utf8
  SPRING_SHARDINGSPHERE_DATASOURCE_SLAVE0_URL: jdbc:mysql://172.19.143.30:60000/todo.auth?useunicode:true&characterencoding:utf8&charactersetresults:utf8
```