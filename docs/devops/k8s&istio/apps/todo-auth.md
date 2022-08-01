# 注册中心k8s配置
## deployment 配置
```
apiVersion: v1
kind: ConfigMap
metadata:
  name: auth-configmap
  namespace: todo
data:
  SPRING_PROFILES_ACTIVE: prod
  EUREKA_CLIENT_SERVICEURL_DEFAULTZONE: http://eureka-clusterip:40000/eureka/
  SPRING_SHARDINGSPHERE_DATASOURCE_MASTER_USERNAME: todo_user
  SPRING_SHARDINGSPHERE_DATASOURCE_SLAVE0_USERNAME: todo_user
  SPRING_SHARDINGSPHERE_DATASOURCE_MASTER_URL: jdbc:mysql://mysql8:3306/todo_auth?useUnicode=true&characterEncoding=utf8&characterSetResults=utf8
  SPRING_SHARDINGSPHERE_DATASOURCE_SLAVE0_URL: jdbc:mysql://mysql8:3306/todo_auth?useUnicode=true&characterEncoding=utf8&characterSetResults=utf8
  
  
apiVersion: apps/v1
kind: Deployment
metadata:
  name: auth
  namespace: todo
spec:
  selector:
    matchLabels:
      app: auth
  replicas: 1
  template:
    metadata:
      labels:
        app: auth
    spec:
      containers:
      - name: auth
        image: auth:latest
        imagePullPolicy: Never
        ports:
        - containerPort: 40002
        env:
        - name: SPRING_PROFILES_ACTIVE
          valueFrom:
            configMapKeyRef:
              name: auth-configmap
              key: SPRING_PROFILES_ACTIVE
        - name: EUREKA_CLIENT_SERVICEURL_DEFAULTZONE
          valueFrom:
            configMapKeyRef:
              name: auth-configmap
              key: EUREKA_CLIENT_SERVICEURL_DEFAULTZONE   
        - name: SPRING_SHARDINGSPHERE_DATASOURCE_MASTER_USERNAME
          valueFrom:
            configMapKeyRef:
              name: auth-configmap
              key: SPRING_SHARDINGSPHERE_DATASOURCE_MASTER_USERNAME   
              
        - name: SPRING_SHARDINGSPHERE_DATASOURCE_SLAVE0_USERNAME
          valueFrom:
            configMapKeyRef:
              name: auth-configmap
              key: SPRING_SHARDINGSPHERE_DATASOURCE_SLAVE0_USERNAME
              
        - name: SPRING_SHARDINGSPHERE_DATASOURCE_MASTER_URL
          valueFrom:
            configMapKeyRef:
              name: auth-configmap
              key: SPRING_SHARDINGSPHERE_DATASOURCE_MASTER_URL
              
        - name: SPRING_SHARDINGSPHERE_DATASOURCE_SLAVE0_URL
          valueFrom:
            configMapKeyRef:
              name: auth-configmap
              key: SPRING_SHARDINGSPHERE_DATASOURCE_SLAVE0_URL
              
              
apiVersion: v1
kind: Service
metadata:
  labels:
    app: auth
    namespace: todo
  name: auth-clusterip
spec:
  ports:
  - name: service0
    port: 40002
    protocol: TCP
    targetPort: 40002
  selector:
    app: auth
  type: ClusterIP
```