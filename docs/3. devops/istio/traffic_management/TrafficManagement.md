# 流量管理 Traffic Management
Istio路由规则可以控制服务间流量和API调用。Istio简化了服务级别的一些属性，比如：断路器（Circuit Breaker)、超时（Timeout)、重试（Retries)，
同时也可以简化一些重要的任务，比如：A/B测试（A/B testing)、基于流量百分比的金丝雀部署（Canary rollouts percentage-based traffic split).

Istio的流量管理基于Envoy Proxies,网格中的服务发送和接受流量都会由Envoy代理。

Istio流量管理主要由以下API完成：
* Virtual Service
* Destination Rule
* Gateways
* Service Entries
* Sidecars

# Virtual Service
Virtual Service和Destination Rule是构建Istio流量路由功能的关键拼图。Virtual Service用来配置Istio服务网格中如何将请求路由到
一个服务。每个Virtual Service都有一套有序的路由规则组成。

Virtual Service主要是配置如何将流量匹配到一个Service。

# Destination Rule
Destination Rule主要用来在Virtual Service匹配到一个Service后，根据什么样的规则将流量转发给这个Service。
常用的Destination Rule主要是负载均衡、多版本、断路器等等。

# Gateways

# Service Entries

# Sidecars