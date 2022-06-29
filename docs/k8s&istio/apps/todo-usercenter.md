# 注册中心k8s配置
## deployment 配置
```
apiVersion: apps/v1
kind: Deployment
metadata:
  name: usercenter
  namespace: todo
spec:
  selector:
    matchLabels:
      app: usercenter
  replicas: 2
  template:
    metadata:
      labels:
        app: usercenter
    spec:
      containers:
      - name: usercenter
        image: usercenter:${tag}
        ports:
        - containerPort: 40002

```