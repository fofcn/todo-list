# todo list config map

```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: todo-list
data:
  spring.profiles.active: prod
  eureka.client.serviceurl.defaultzone: http://172.19.143.30:40000/eureka/
  spring.shardingsphere.datasource.master.username: jdbc:mysql://172.19.143.30:60000/todo_auth?useUnicode=true&characterEncoding=utf8&characterSetResults=utf8
  spring.shardingsphere.datasource.slave0.username: jdbc:mysql://172.19.143.30:60000/todo_auth?useUnicode=true&characterEncoding=utf8&characterSetResults=utf8
  spring.shardingsphere.datasource.master.url: jdbc:mysql://172.19.143.30:60000/todo.auth?useunicode:true&characterencoding:utf8&charactersetresults:utf8
  spring.shardingsphere.datasource.slave0.url: jdbc:mysql://172.19.143.30:60000/todo.auth?useunicode:true&characterencoding:utf8&charactersetresults:utf8
  spring.cloud.client.ipaddress: 172.19.143.30
```