# 注册中心k8s配置
## deployment 配置
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: eureka
  namespace: todo
spec:
  selector:
    matchLabels:
      app: eureka
  replicas: 1 
  template:
    metadata:
      labels:
        app: eureka
    spec:
      containers:
      - name: eureka
        image: eureka:latest
        imagePullPolicy: Never
        ports:
        - containerPort: 40000
        
apiVersion: v1
kind: Service
metadata:
  name: eureka-service
  namespace: todo
  labels:
    app: eureka
  name: eureka-clusterip
spec:
  type: ClusterIP
  ports:
  - name: service0
    port: 40000               
    protocol: TCP            
    targetPort: 40000           
  selector:                   
    app: eureka
            
```