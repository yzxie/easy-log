# 分布式日志分析系统

## 多模块结构
1. log-collector：日志收集模块，作为消费者，实时从kafka读取Flume传输到kafka上面的日志数据
2. log-engine：日志分析引擎，对从log-collector获取的数据，进行处理，根据配置文件定义的日志数据格式，进行处理分析；
3. log-store：日志存储模块，即在日志分析引擎分析后，将分析结果进行存储。为了提高日志分析引擎的性能，将结果先存储到Redis而不是MySQL。在log-store模块中，再定期将Redis数据刷到MySQL进行持久化存储；同时为web项目提供RPC服务。
4. log-conf：配置文件管理模块
5. log-common：公共类，公共组件，工具类定义模块
6. log-starter：整个服务启动模块

## 技术栈
1. Dubbo
2. Mybatis
3. Flume
4. Kafka
5. Netty
6. Redis
7. MySQL

## flume配置
### 日志读取方式
flume配置采用tail的方式读取日志文件，每个日志文件对应一个flume.properties文件，启动一个代理。在flume.properties文件中指定：
kafka的topic, key，如以下组合：
1. app1的stdout.log
topic: StdOut
key: app1
partition index: 0
2. app2的stdout.log
topic: StdOut
key: app2
partition index: 1

### 指定defaultPartitionId
1. app1 -> 0
2. app2 -> 1
3. ...


