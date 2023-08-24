### 简介
结合工作中以及个人学习遇到的问题以及对一些技术的沉淀，总结的一个项目。

### 模块简介
- app-infrastructure 基础模块，包含Redis, Database, RabbitMQ, MQTT, Quartz等中间件的动态配置，以及一些通用的抽象类
- app-common 通用模块，一些通用的组件，对 SAXParser 封装的 XML 工具类，对 ObjectMapper 进行封装的 JSON 工具类等，以及对 Executor 执行器模式的抽象，对 StateMachine 状态及的抽象等
- app-gateway 网关模块，统一入口，包含鉴权以及动态路由
- app-client FeignClient 定义，用于各个模块间接口的调用与实现
- app-biz 业务模块的父 POM
- app-biz/app-resources 资源管理模块，用于管理各种资源
- app-biz/app-dispatch 调度模块，负责对设备的调度与操作，主要通过 WebSocket 以及 MQTT 协议
- app-biz/app-process 流程控制模块，采用 Camunda 7 方案，实现 JSON 与 BPMN 的转换，实现会签，或签，依次审批，加签等操作
- app-biz/app-media 流媒体模块，实现了部分 OnvifClient 功能，Sip 信令网关功能，以及 GB28181 协议的部分实现，可结合开源 ZLMediaKit 实现流媒体网关功能 (部分功能 TODO)
