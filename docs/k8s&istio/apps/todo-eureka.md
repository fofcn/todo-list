# 注册中心k8s配置
## deployment 配置
```
apiVersion: apps/v1
kind: Deployment
metadata:
  name: eureka
  namespace: default
spec:
  selector:
    matchLabels:
      app: eureka
  replicas: 2 
  template:
    metadata:
      labels:
        app: eureka
    spec:
      containers:
      - name: eureka
        image: eureka:${tag}
        ports:
        - containerPort: 40000
        
```