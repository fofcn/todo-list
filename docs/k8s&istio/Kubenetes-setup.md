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
/usr/sbin/iptables -P FORWARD ACCEPT

sudo sed -i 's/.*swap.*/#&/' /etc/fstab

# 设置网络
sudo tee /etc/sysctl.d/k8s.conf <<-'EOF'
net.bridge.bridge-nf-call-ip6tables = 1
net.bridge.bridge-nf-call-iptables = 1
net.ipv4.ip_forward = 1
EOF

modprobe br_netfilter
查看 ipv4 与 v6 配置是否生效
sysctl --system
# 安装工具
sudo apt-get install apt-transport-https ca-certificates curl software-properties-common

# 安装docker
sudo apt-get install docker.io

# docker daemon.json
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


# 添加阿里云源
curl -fsSL https://mirrors.aliyun.com/docker-ce/linux/ubuntu/gpg | apt-key add -
add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" apt-get update

# 更新本地源
sudo apt update

# 使用 apt-cache 命令查看支持的 Kubernetes 版本
apt-cache madison kubectl | grep 1.16

# 安装 k8s
sudo apt-get install -y kubelet=1.23.8-00 kubeadm=1.23.8-00 kubectl=1.23.8-00
sudo apt-mark hold kubelet=1.23.8-00 kubeadm=1.23.8-00 kubectl=1.23.8-00

# 使用kubeadm 初始化k8s集群
sudo kubeadm init --apiserver-advertise-address=10.0.8.3 --pod-network-cidr=10.244.0.0/16 --image-repository registry.cn-hangzhou.aliyuncs.com/google_containers

# 初始化完成后
mkdir -p $HOME/.kube
sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
sudo chown $(id -u):$(id -g) $HOME/.kube/config

# 腾讯云k8s集群加入命令
kubeadm join 10.0.8.3:6443 --token ej8t5z.dbcua5lbwtjobiqp \
--discovery-token-ca-cert-hash sha256:7001245096b3408bc09cd55a6fc2fd488d92fe621e8046c5c85bdb2fd7cc02fa


# 安装pod网络
kubectl apply -f https://raw.githubusercontent.com/flannel-io/flannel/master/Documentation/kube-flannel.yml


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
