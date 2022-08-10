# 注册中心k8s配置
## deployment 配置
```
apiVersion: v1
kind: ConfigMap
metadata:
  name: gateway-configmap
  namespace: todo
data:
  SPRING_PROFILES_ACTIVE: prod
  EUREKA_CLIENT_SERVICEURL_DEFAULTZONE: http://eureka-clusterip:40000/eureka/
  
apiVersion: apps/v1
kind: Deployment
metadata:
  name: gateway
  namespace: todo
spec:
  selector:
    matchLabels:
      app: gateway
  replicas: 1
  template:
    metadata:
      labels:
        app: gateway
    spec:
      containers:
      - name: gateway
        image: gateway:latest
        imagePullPolicy: Never
        ports:
        - containerPort: 40001
        env:
        - name: SPRING_PROFILES_ACTIVE
          valueFrom:
            configMapKeyRef:
              name: gateway-configmap
              key: SPRING_PROFILES_ACTIVE
        - name: EUREKA_CLIENT_SERVICEURL_DEFAULTZONE
          valueFrom:
            configMapKeyRef:
              name: gateway-configmap
              key: EUREKA_CLIENT_SERVICEURL_DEFAULTZONE   
              
apiVersion: v1
kind: Service
metadata:
  labels:
    app: gateway
    namespace: todo
  name: gateway-clusterip
spec:
  ports:
  - name: service0
    port: 40001
    protocol: TCP
    targetPort: 40001
  selector:
    app: gateway
  type: ClusterIP
     
```