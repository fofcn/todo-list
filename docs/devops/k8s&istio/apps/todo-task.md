# 注册中心k8s配置
## deployment 配置
```
apiVersion: apps/v1
kind: Deployment
metadata:
  name: task
  namespace: todo
spec:
  selector:
    matchLabels:
      app: task
  replicas: 2
  template:
    metadata:
      labels:
        app: task
    spec:
      containers:
      - name: task
        image: task:${tag}
        imagePullPolicy: Never
        ports:
        - containerPort: 40002

```