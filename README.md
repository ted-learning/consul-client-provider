# consul-client-provider

A demo for spring cloud consul

High Level Architecture



Consul 分布式集群


服务器都是单个 Raft 对等体集的一部分。这意味着他们一起工作来选举一个领导者。非leader服务器收到一个RPC请求时，它会转发给集群leader。当服务器收到对不同数据中心的请求时，它会将其转发到正确数据中心中的随机服务器。然后该服务器可以转发到本地领导者。

Consul 与应用程序集成:


Anti-Entropy 反熵
熵是系统变得越来越无序的趋势。Consul 的反熵机制旨在对抗这种趋势，即使在其组件发生故障时也能保持集群状态有序。
* Periodic Synchronization
    * 除了在Agent发生更改时运行，反熵也是一个长期运行的过程，周期性反熵运行之间的时间量将根据集群大小而变化，每个Consul Agent都会在间隔窗口内随机选择一个错开的开始时间
* Best-effort sync
    * 反熵可能会失败，Agent会尝试以尽力而为的方式进行同步，如果在反熵运行期间遇到错误，则会记录该错误并继续运行代理。反熵机制会定期运行以自动从这些类型的瞬态故障中恢复。

Agent
Each Consul agent maintains its own set of service and check registrations as well as health information. The agents are responsible for executing their own health checks and updating their local state.

Catalog
Consul's service discovery is backed by a service catalog. This catalog is formed by aggregating information submitted by the agents. The catalog maintains the high-level view of the cluster, including which services are available, which nodes run those services, health information, and more. The catalog is used to expose this information via the various interfaces Consul provides, including DNS and HTTP。

Consensus
Consul 使用 Consensus 来提供一致性（由 CAP 定义）。共识协议基于 “Raft：寻找可理解的共识算法”。有关 Raft 的直观解释，请参阅The Secret Lives of Data。

Raft 节点始终处于以下三种状态之一：
* 跟随者：初始状态
* 候选者：一段时间内没有收到条目，跟随者会自我提升到候选者
* 领导者：候选者投票决定，可以追加一个新的日志条目并向跟随者复制

Modes
* Default: 均衡模式，Raft 利用领导者租赁，提供领导者假设其角色稳定的时间窗口。
* Consistent: 此模式非常一致，没有任何警告。权衡始终是一致的读取，但由于额外的往返行程而增加了延迟。
* Stale: 这意味着读取可以任意陈旧，但通常在领导者的 50 毫秒内。




Consul Agent Install
https://learn.hashicorp.com/tutorials/consul/get-started-install?in=consul/getting-started








        Dashboard: http://localhost:8500/

Gossip协议
https://www.consul.io/docs/architecture/gossip




使用 Consul 进行服务注册与发现

注册：
当客户端向 Consul 注册时，它会提供有关自身的元数据，例如主机和端口、id、名称和标签、weight等。默认情况下会创建一个 HTTP 检查，Consul默认每10秒 /actuator/health 访问一次端点。

Registering Management as a Separate Service







Health Check: http://localhost:{management.server.port}/actuator/health



${spring.application.name}:${vcap.application.instance_id:${spring.application.instance_id:${random.value}}}


发现：


Client Side Load balancer, 会有本地缓存 （与Consul不同步，会有服务下线延迟同步的情况）



DiscoveryClient是实时查询，与Consul服务状态同步

Spring 使用Consul HTTP API进行服务发现，Consul还提供DNS的API

➜  ~ dig @127.0.0.1 -p 8600 consul-client-provider.service.consul SRV


; <<>> DiG 9.10.6 <<>> @127.0.0.1 -p 8600 consul-client-provider.service.consul SRV
; (1 server found)
;; global options: +cmd
;; Got answer:
;; ->>HEADER<<- opcode: QUERY, status: NOERROR, id: 3408
;; flags: qr aa rd; QUERY: 1, ANSWER: 2, AUTHORITY: 0, ADDITIONAL: 5
;; WARNING: recursion requested but not available


;; OPT PSEUDOSECTION:
; EDNS: version: 0, flags:; udp: 4096
;; QUESTION SECTION:
;consul-client-provider.service.consul. IN SRV


;; ANSWER SECTION:
consul-client-provider.service.consul. 0 IN SRV    1 1 2703 0ad11538.addr.dc1.consul.
consul-client-provider.service.consul. 0 IN SRV    1 1 3302 0ad11538.addr.dc1.consul.


;; ADDITIONAL SECTION:
0ad11538.addr.dc1.consul. 0    IN    A    10.209.21.56
Zhangs-MacBook-Pro.local.node.dc1.consul. 0 IN TXT "consul-network-segment="
0ad11538.addr.dc1.consul. 0    IN    A    10.209.21.56
Zhangs-MacBook-Pro.local.node.dc1.consul. 0 IN TXT "consul-network-segment="


;; Query time: 0 msec
;; SERVER: 127.0.0.1#8600(127.0.0.1)
;; WHEN: Fri Apr 01 17:52:43 CST 2022
;; MSG SIZE  rcvd: 288

curl --location --request GET 'http://consul-client-provider.service.consul:3302/hello’


https://learn.hashicorp.com/tutorials/consul/dns-forwarding?in=consul/networking


集成Spring cloud config





