# 注册中心k8s配置
## deployment 配置
```
apiVersion: apps/v1
kind: Deployment
metadata:
  name: auth
  namespace: todo
spec:
  selector:
    matchLabels:
      app: auth
  replicas: 2
  template:
    metadata:
      labels:
        app: auth
    spec:
      containers:
      - name: auth
        image: auth:${tag}
        ports:
        - containerPort: 40002

```