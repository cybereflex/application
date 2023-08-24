### 简介
基础建设模块，禁用了一些中间件的 autoconfigure，手动对中间件进行配置以及定义了一些中间件的抽象类和工具类

动态配置包含数据库配置，缓存配置，消息中间件配置，定时任务配置，位于 autoconfigure 包下，支持对各种中间件配置的扩展

抽象类和工具类包含

- AbstractAppInitial 服务启动初始化抽象类
- AbstractRabbitListener RabbitMQ 监听器抽象类
- AbstractScheduleJob Quartz 定时任务抽象类
- QuartzScheduleManager Quartz 定时任务管理工具类
- AbstractMqttHandler MQTT 监听器抽象类
- MqttTemplate MQTT 消息发送抽象类
- RedisCache Redis 缓存操作工具类
- SpringContextHolder Spring Context 上下文持有者，可用于操作 bean
- JsonTypeHandler Mybatis JSON 与 Map<String,Object> 类型转换