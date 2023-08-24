### 简介
通用模块，一些工具类的封装，组件的定义，常量以及枚举的定义

工具类：

- CommonHttpClient 对于 JDK HttpClient 的封装
- Json 对 Jackson 的封装
- XmlParser 对 SAXParser 的封装
- JwtUtil 对 JWT 的封装
- DateTimeUtil LocalDateTime 等对 Date 的转换 

组件：

- cc.cybereflex.common.component.executor 包下，执行器模式的定义，对任务进行拆分，适用于较为复杂的逻辑处理
- cc.cybereflex.common.component.state_machine 包下，状态机的定义，适用于有状态流转的场景