# 分布式日志分析系统

一. 多模块结构
1. log-collector：日志收集模块，作为消费者，实时从kafka读取Flume传输到kafka上面的日志数据
2. log-engine：日志分析引擎，对从log-collector获取的数据，进行处理，根据配置文件定义的日志数据格式，进行处理分析；
3. log-store：日志存储模块，即在日志分析引擎分析后，将分析结果进行存储。为了提高日志分析引擎的性能，将结果先存储到Redis，
然后日志分析引擎线程进行日志分析。在该模块中，再定期将Redis数据刷到MySQL进行持久化存储；
4. log-conf：配置文件管理模块；
5. log-common：公共类，公共组件，工具类定义模块；
6. log-starter：整个服务启动模块；
7. log-web：web项目，对日志分析结果实时可视化展示。

二. 技术栈
1. SpringBoot
2. Mybatis
3. Flume
4. Kafka
5. Netty
6. Redis
7. MySQL


