# 1. 参考链接
1. [istio 安装](https://istio.io/latest/docs/setup/getting-started/#download)

# 2. 下载istio
curl -L https://istio.io/downloadIstio | ISTIO_VERSION=1.12.8 TARGET_ARCH=x86_64 sh -

# 3. 解压
tar zxvf istio-XXXX-linux-amd64.tar.gz

# 4. 安装istio
cd istio-1.14.1
export PATH=$PWD/bin:$PATH
istioctl install --set profile=demo -y

# 4. k8s 添加命名空间以让istio自动注入Envoy Sidecar代理
kubectl label namespace default istio-injection=enabled

# 5. 部署示例应用
kubectl apply -f samples/bookinfo/platform/kube/bookinfo.yaml

# 6. k8s 查看service
kubectl get services

# 7. k8s 查看pod
kubectl get pods

# 8. k8s 启动代理以让外部访问服务
kubectl apply -f samples/bookinfo/networking/bookinfo-gateway.yaml

# 9. k8s 分析以确保配置正确
istioctl analyze


# 10. istio安装问题
1. Events:
   Type     Reason            Age   From               Message
  ----     ------            ----  ----               -------
Warning  FailedScheduling  10m   default-scheduler  0/1 nodes are available: 1 node(s) had taint {node-role.kubernetes.io/master: }, that the pod didn't tolerate
## 问题原因
1. k8s master节点无可调度node;
2. pilot镜像未拉取成功。
## 解决方案
1. 启用master节点node
```shell
kubectl taint nodes --all node-role.kubernetes.io/master-
```
2. 手动拉取pilot镜像
```shell
sudo docker pull docker.io/istio/pilot:1.14.1
```

# 11. 安装完成后,BookInfo应用测试（详细可以参考）
1. [Istio Book info示例测试](https://istio.io/latest/docs/examples/)