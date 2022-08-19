# Destination Rule
DestinationRule defines policies that apply to traffic intended for a service after routing has occurred. 
These rules specify configuration for load balancing, connection pool size from the sidecar, 
and outlier detection settings to detect and evict unhealthy hosts from the load balancing pool.
Destination Rule 定义了流量转发到服务后的策略。这些规则配置指定了Sidecar的负载均衡、连接池的大小和要从负载均衡池中要移除的不健康的主机。
示例：
```yaml
// api 
apiVersion: networking.istio.io/v1alpha3
// 资源类型DestinationRule
kind: DestinationRule
metadata:
  name: bookinfo-ratings
spec:
  host: ratings.prod.svc.cluster.local
  trafficPolicy:
    loadBalancer:
      simple: LEAST_REQUEST

```

# 自我总结
VirtualService是路由规则，那么DestinationRule则是转发规则。

