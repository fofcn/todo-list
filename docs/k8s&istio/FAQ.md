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