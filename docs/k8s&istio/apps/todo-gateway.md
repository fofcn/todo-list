# 注册中心k8s配置
## deployment 配置
```
apiVersion: apps/v1
kind: Deployment
metadata:
  name: gateway
  namespace: todo
spec:
  selector:
    matchLabels:
      app: gateway
  replicas: 2
  template:
    metadata:
      labels:
        app: gateway
    spec:
      containers:
      - name: gateway
        image: gateway:${tag}
        ports:
        - containerPort: 40001

```