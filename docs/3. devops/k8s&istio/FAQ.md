# FAQ
## k8s NodePort无法使用80端口
修改k8s apiserver.yml配置，设置端口范围
sudo vim kube-apiserver.yaml
在配置块：
```yaml

spec:
  containers:
  - command:

```
下添加：
```yaml
# 80-65534这个是端口号范围，这个可以根据自己的场景进行配置
- --service-node-port-range=80-65534
```

# mysql:  The MySQL server is running with the --skip-grant-tables option so it cannot execute this statement
```shell
FLUSH PRIVILEGES;

create user 'username'@'%' identified by '';
grant all privileges on *.* to 'username'@'%' with grant option;  
FLUSH PRIVILEGES;
```

# 测试工具
```shell
kubectl run -i --tty --image=mysql:8.0.25 --restart=Never test --rm /bin/sh -n todo

mysql -u username -p -h mysql8 -P 33060
kubectl run -i --tty --image=nginx:alpine --restart=Never test --rm /bin/sh -n todo
```

# 集群内访问（ClusterIP)
1. [follow this link](https://support.huaweicloud.com/usermanual-cce/cce_01_0011.html)

