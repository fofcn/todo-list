# nginx k8s configuration
## nginx deployment 配置
```
apiVersion: apps/v1
kind: Deployment
metadata:
  name: nginx-deployment
  namespace: todo
spec:
  selector:
    matchLabels:
      app: nginx
  replicas: 2 
  template:
    metadata:
      labels:
        app: nginx
    spec:
      containers:
      - name: nginx
        image: nginx:1.14.2
        ports:
        - containerPort: 80
        
```

## nginx service配置
```
apiVersion: v1
kind: Service
metadata:
  name: nginx
  labels:
    app: nginx
spec:
  type: LoadBalancer
  ports:
  - name: http
    port: 80
    targetPort: 80
    protocol: TCP

  selector:
    app: nginx
```