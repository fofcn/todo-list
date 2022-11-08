# ubuntu 安装kubenetes
sudo ufw disable

# 安装selinux工具
sudo apt install selinux-utils

# 关闭selinux
sudo setenforce 0

sudo vim /etc/selinux/conifg
SELINUX=disabled
# 配置linux iptable 路由转发
sudo iptables -P FORWARD ACCEPT

sudo vim /etc/rc.local
添加下行到文件中然后保存
/usr/sbin/iptables -P FORWARD ACCEPT

sudo sed -i 's/.*swap.*/#&/' /etc/fstab

# 设置网络
sudo tee /etc/sysctl.d/k8s.conf <<-'EOF' 
net.bridge.bridge-nf-call-ip6tables = 1
net.bridge.bridge-nf-call-iptables = 1
net.ipv4.ip_forward = 1
EOF

sudo modprobe br_netfilter
查看 ipv4 与 v6 配置是否生效
sudo sysctl --system
# 安装工具
sudo apt-get install apt-transport-https ca-certificates curl software-properties-common

# 安装docker
sudo apt-get install docker.io

# docker daemon.json 可选
sudo cat /etc/docker/daemon.json
{
"exec-opts": ["native.cgroupdriver=systemd"],
"log-driver": "json-file",
"log-opts": {
"max-size": "100m"
},
"storage-driver": "overlay2",
"storage-opts": [
"overlay2.override_kernel_check=true"
],
"registry-mirrors": [
"https://mirror.ccs.tencentyun.com"
]
}
sudo systemctl restart docker

# 添加阿里云源
sudo curl -fsSL https://mirrors.aliyun.com/docker-ce/linux/ubuntu/gpg | apt-key add -
sudo add-apt-repository "deb [arch=amd64] https://mirrors.aliyun.com/docker-ce/linux/ubuntu $(lsb_release -cs) stable"

# 更新本地源
sudo apt update

# 安装docker
sudo apt install -y docker-ce

# 安装docker-compose(可选)
sudo apt install -y docker-compose
# 开机自启动docker
sudo systemctl enable docker

sudo curl https://mirrors.aliyun.com/kubernetes/apt/doc/apt-key.gpg | apt-key add -

# 添加APT源
sudo su -
sudo cat <<EOF >/etc/apt/sources.list.d/kubernetes.list
deb https://mirrors.aliyun.com/kubernetes/apt/ kubernetes-xenial main
EOF

sudo apt-get update

# 查看k8s版本
sudo apt-cache madison kubelet

# 使用 apt-cache 命令查看支持的 Kubernetes 版本
sudo apt-cache madison kubectl | grep 1.18.4-00

# 安装指定版本k8s
sudo apt-get install -y kubelet=1.18.4-00 kubeadm=1.18.4-00 kubectl=1.18.4-00

# 设置开机启动
sudo systemctl enable kubelet && sudo systemctl start kubelet

# 使用kubeadm 初始化k8s集群
sudo kubeadm init --apiserver-advertise-address=10.17.17.147 --pod-network-cidr=10.244.0.0/16 --image-repository registry.cn-hangzhou.aliyuncs.com/google_containers

# 初始化完成后
mkdir -p $HOME/.kube
sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
sudo chown $(id -u):$(id -g) $HOME/.kube/config

# 安装pod网络
kubectl apply -f https://raw.githubusercontent.com/flannel-io/flannel/master/Documentation/kube-flannel.yml

# k8s集群加入命令
kubeadm join 10.0.8.3:6443 --token ej8t5z.dbcua5lbwtjobiqp \
--discovery-token-ca-cert-hash sha256:7001245096b3408bc09cd55a6fc2fd488d92fe621e8046c5c85bdb2fd7cc02fa

kubeadm join 10.17.17.147:6443 --token 1c9y04.er0nhzumvgljg83o \
--discovery-token-ca-cert-hash sha256:0eddea15319ad6e693c76bf5e4770ca667a0cf14be92592bb38c63de5084b9a2

# k8s  初始化失败问题
1. wait-control-plane] Waiting for the kubelet to boot up the control plane as stati Pods from directory "/etc/kubernetes/manifests". This can take up to 4m0s
   [kubelet-check] Initial timeout of 40s passed.
   [kubelet-check] It seems like the kubelet isn't running or healthy.
   [kubelet-check] The HTTP call equal to 'curl -sSL http://localhost:10248/healthz' ailed with error: Get http://localhost:10248/healthz: dial tcp 127.0.0.1:10248: conect: connection refused.
   [kubelet-check] It seems like the kubelet isn't running or healthy.
   [kubelet-check] The HTTP call equal to 'curl -sSL http://localhost:10248/healthz' ailed with error: Get http://localhost:10248/healthz: dial tcp 127.0.0.1:10248: conect: connection refused.
   [kubelet-check] It seems like the kubelet isn't running or healthy.
   [kubelet-check] The HTTP call equal to 'curl -sSL http://localhost:10248/healthz' ailed with error: Get http://localhost:10248/healthz: dial tcp 127.0.0.1:10248: conect: connection refused.
   [kubelet-check] It seems like the kubelet isn't running or healthy.
   [kubelet-check] The HTTP call equal to 'curl -sSL http://localhost:10248/healthz' ailed with error: Get http://localhost:10248/healthz: dial tcp 127.0.0.1:10248: conect: connection refused.
   [kubelet-check] It seems like the kubelet isn't running or healthy.
   [kubelet-check] The HTTP call equal to 'curl -sSL http://localhost:10248/healthz' ailed with error: Get http://localhost:10248/healthz: dial tcp 127.0.0.1:10248: conect: connection refused.

Unfortunately, an error has occurred:
timed out waiting for the condition

This error is likely caused by:
- The kubelet is not running
- The kubelet is unhealthy due to a misconfiguration of the node in some wy (required cgroups disabled)

If you are on a systemd-powered system, you can try to troubleshoot the error withthe following commands:
- 'systemctl status kubelet'
- 'journalctl -xeu kubelet'

Additionally, a control plane component may have crashed or exited when started bythe container runtime.
To troubleshoot, list all containers using your preferred container runtimes CLI, .g. docker.
Here is one example how you may list all Kubernetes containers running in docker:
- 'docker ps -a | grep kube | grep -v pause'
Once you have found the failing container, you can inspect its logs with:
- 'docker logs CONTAINERID'
error execution phase wait-control-plane: couldn't initialize a Kubernetes cluster
## 问题原因
docker配置问题

## 解决方案
重启kubelet
sudo systemctl daemon-reload
sudo systemctl restart docker
sudo systemctl start kubelet

2. The Service "nginx" is invalid: spec.ports[0].nodePort: Invalid value: 80: provided port is not in the valid range. The range of valid ports is 30000-32767
## 现象
kubectl apply -f nginx.yml
deployment.apps/nginx-deployment unchanged
The Service "nginx" is invalid: spec.ports[0].nodePort: Invalid value: 80: provided port is not in the valid range. The range of valid ports is 30000-32767

## 原因
kubernetes默认service端口号范围为30000-32767，可以增加apiserver的配置增加端口号范围来解决。
## 解决方案
1. 编辑 kube-apiserver.yaml文件
## 参考
1. https://blog.csdn.net/oopxiajun2011/article/details/106301877
```shell
vim /etc/kubernetes/manifests/kube-apiserver.yaml
```
2. 找到 --service-cluster-ip-range 这一行，在这一行的下一行增加 如下内容
```shell
- --service-node-port-range=1-65535
```
3. 重启 kubelet
```shell
sudo systemctl daemon-reload
sudo systemctl restart kubelet
```

3. Node一直处于NotReady状态
现象：
   kubectl get nodes
   NAME           STATUS     ROLES    AGE    VERSION
   ecsd00301496   NotReady   master   4m3s   v1.18.4

## 问题原因
未安装pod网络
## 解决方案
安装Pod网络
```shell
kubectl apply -f https://raw.githubusercontent.com/flannel-io/flannel/master/Documentation/kube-flannel.yml
```

4. Unable to connect to the server: x509: certificate signed by unknown authority (possibly because of “crypto/rsa: verification error” while trying to verify candidate authority certificate “kubernetes”)
## 问题原因
root用户安装并初始化了k8s集群，使用普通用户执行Kubectl命令报错
## 解决方案
使用root或者sudo执行kubectl命令问题解决。

5.  Warning  FailedScheduling  <unknown>            default-scheduler  0/1 nodes are available: 1 node(s) had taint {node-role.kubernetes.io/master: }, that the pod didn't tolerate.

## 解决方案
```shell
kubectl taint nodes --all node-role.kubernetes.io/master-
```
## 参考
1. https://stackoverflow.com/questions/59484509/node-had-taints-that-the-pod-didnt-tolerate-error-when-deploying-to-kubernetes

# 参考
1. [Ubuntu k8s安装](https://blog.csdn.net/professorman/article/details/118150688)
2. [k8s nginx yml](https://github.com/fofcn/todo-list/blob/v1.1.0-istio/docs/3.%20devops/k8s/in-one/nginx/nginx.yml)