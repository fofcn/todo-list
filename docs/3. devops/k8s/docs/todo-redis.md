```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: redis
  namespace: todo
  labels:
    app: redis
spec:
  selector:
    matchLabels:
      app: redis
  strategy:
    type: Recreate
  template:
    metadata:
      labels:
        app: redis
    spec:
      containers:
        - name: redis
          image: docker.io/bitnami/redis:6.2
          ports:
            - containerPort: 3306
          env:
            - name: ALLOW_EMPTY_PASSWORD
              value: "yes"
```
```yaml
apiVersion: v1
kind: Service
metadata:
  labels:
    app: redis
    namespace: todo
  name: redis-clusterip
spec:
  ports:
  - name: service0
    port: 6379
    protocol: TCP
    targetPort: 6379
  selector:
    app: redis
  type: ClusterIP
```