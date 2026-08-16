
# 01springboot-hello 项目开发规范指南

为保证项目代码质量、可维护性、安全性与可扩展性，所有开发工作应遵循以下规则。

## 一、项目基本信息

| 项目项 | 说明 |
|---|---|
| 项目名称 | `01springboot-hello` |
| 项目作者 | `15486` |
| 操作系统 | Windows 11 |
| 工作目录 | `D:\kaifa\xiangmu\01springboot-hello\01springboot-hello` |
| 构建工具 | Maven |
| Java 版本 | JDK 17.0.11 |
| Spring Boot 版本 | `2.3.4.RELEASE` |
| 项目坐标 | `com.bjsxt:01springboot-hello:0.0.1-SNAPSHOT` |
| 主包路径 | `com.bjsxt` |

> 项目当前使用 Spring Boot 2.3.4.RELEASE，不应直接套用 Spring Boot 3.x 的依赖包名或 API。  
> 例如，Spring Boot 2.x 默认使用 `javax.*` 命名空间；升级到 Spring Boot 3.x 后才使用 `jakarta.*`。

## 二、目录结构

```text
01springboot-hello
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── bjsxt
    │   │           ├── controller
    │   │           ├── domain
    │   │           ├── dto
    │   │           ├── mapper
    │   │           ├── service
    │   │           │   └── impl
    │   │           └── util
    │   └── resources
    │       ├── mapper
    │       ├── static
    │       └── templates
    └── test
        └── java
            └── com
                └── bjsxt
```

### 目录职责

| 目录 | 职责 |
|---|---|
| `controller` | 接收 HTTP 请求、参数校验并返回响应，不直接操作数据库 |
| `domain` | 存放领域对象、数据库实体或业务数据对象 |
| `dto` | 存放请求参数和响应数据传输对象 |
| `mapper` | 存放 MyBatis Mapper 接口 |
| `service` | 定义业务接口，编排业务逻辑 |
| `service.impl` | 存放 Service 接口实现类 |
| `util` | 存放通用工具类，不应存放核心业务逻辑 |
| `resources/mapper` | 存放 MyBatis XML 映射文件 |
| `resources/static` | 存放静态资源 |
| `resources/templates` | 存放服务端模板文件 |
| `src/test/java` | 存放单元测试和集成测试代码 |

## 三、技术栈与版本要求

### 核心技术

- Java：JDK 17.0.11
- Spring Boot：`2.3.4.RELEASE`
- 构建工具：Maven
- Web 框架：`spring-boot-starter-web`
- 持久层框架：MyBatis
- MyBatis Spring Boot Starter：`2.1.4`
- 数据库：MySQL
- 缓存：Spring Data Redis
- 服务注册与发现：Nacos Discovery
- 配置中心：Nacos Config
- 消息队列：RocketMQ
- 代码生成与简化：Lombok `1.18.30`
- 测试框架：`spring-boot-starter-test`
- 热部署：Spring Boot DevTools
- 配置元数据处理：Spring Boot Configuration Processor

### 依赖使用规则

- 本项目使用 **MyBatis**，不使用 JPA、Hibernate 或 `JpaRepository`。
- 数据库访问必须通过 Mapper 层完成，禁止在 Controller 中直接执行数据库操作。
- MyBatis XML 文件统一放置在 `src/main/resources/mapper` 目录。
- Nacos 相关配置应集中维护在项目配置文件或配置中心中，禁止在代码中硬编码服务地址、账号和密码。
- Redis 仅用于缓存、分布式数据或临时数据存储，不应替代核心业务数据库。
- RocketMQ 消费者和生产者应明确消息主题、标签、消费模式及异常处理策略。
- `spring-boot-devtools` 仅用于开发环境，不得影响生产环境部署。
- Lombok 仅用于减少样板代码，不得掩盖复杂业务逻辑或不合理的对象设计。
- 新增依赖前应确认是否已有功能相同的依赖，避免重复引入和版本冲突。
- 不得随意升级 Spring Boot、Spring Cloud Alibaba、MyBatis 或 RocketMQ 版本；升级前必须评估兼容性。

## 四、Maven 构建规范

常用命令如下：

```bash
# 编译项目
mvn clean compile

# 执行测试
mvn test

# 打包项目
mvn clean package

# 跳过测试打包，仅在确认必要时使用
mvn clean package -DskipTests

# 启动 Spring Boot 项目
mvn spring-boot:run
```

规范要求：

- 修改 `pom.xml` 后应执行 Maven 依赖解析和编译验证。
- 提交代码前至少执行 `mvn test` 或 `mvn clean package`。
- 不得提交 `target`、IDE 配置缓存或本地构建产物。
- 依赖版本优先由 Spring Boot Parent 或 BOM 统一管理。
- 需要显式指定版本时，应说明原因并避免与现有版本体系冲突。

## 五、分层架构规范

### Controller 层

- 只负责 HTTP 请求接收、参数校验、调用 Service 和构造响应。
- 禁止直接调用 Mapper。
- 禁止在 Controller 中编写复杂业务逻辑。
- 请求和响应优先使用 DTO，避免直接暴露数据库对象。
- 接口路径、请求方式、参数含义和响应结构应保持清晰一致。

### Service 层

- 所有业务逻辑必须通过 Service 层组织。
- Service 接口放置在 `service` 包中，实现类放置在 `service.impl` 包中。
- 涉及多个数据库操作的业务流程，应根据实际情况使用事务。
- 事务注解优先放在 Service 层。
- Service 层应进行必要的业务校验和异常处理。
- 不应将 Controller、HTTP 或页面相关逻辑放入 Service。

### Mapper 层

- Mapper 接口统一放置在 `com.bjsxt.mapper` 包中。
- SQL 优先写在 `resources/mapper` 对应的 XML 文件中；简单查询可根据项目约定使用注解。
- 禁止手动拼接用户输入形成 SQL，防止 SQL 注入。
- SQL 参数必须使用 MyBatis 参数绑定机制。
- 查询字段应按需选择，避免无必要的 `SELECT *`。
- 注意分页、索引和批量操作性能，避免循环中频繁执行单条 SQL。
- Mapper XML 的命名空间必须与 Mapper 接口全限定名一致。

### Domain、DTO 层

- `domain` 用于表达领域对象或持久化对象。
- `dto` 用于接口输入和输出的数据传输。
- 不应将包含敏感字段的 Domain 对象直接返回给前端。
- Domain 与 DTO 之间应在 Service 层或专用转换方法中完成转换。
- 对象字段命名应与业务含义保持一致，避免使用无意义的缩写。

## 六、安全与数据校验规范

- 对外接口必须进行输入校验。
- Spring Boot 2.x 项目使用 `javax.validation.*` 下的校验注解，例如：
  - `@NotNull`
  - `@NotBlank`
  - `@Size`
  - `@Min`
  - `@Max`
- Controller 参数根据实际情况使用 `@Valid` 或 `@Validated`。
- 禁止信任客户端传入的身份、权限、金额、状态等关键字段。
- 密码、密钥、数据库密码、Nacos 凭证和 RocketMQ 凭证不得写入源代码。
- 敏感配置应通过环境变量、配置文件外置或 Nacos 配置中心管理。
- 日志中禁止输出密码、令牌、完整身份证号、银行卡号等敏感信息。
- 对异常响应进行统一处理，避免将堆栈信息、SQL 或内部路径直接返回给客户端。
- 防范 SQL 注入、越权访问、XSS、反序列化攻击和敏感信息泄露等常见安全问题。

## 七、事务、缓存与消息规范

### 事务

- `@Transactional` 优先用于 Service 层。
- 事务范围应尽可能小，避免在事务中执行远程调用、长时间阻塞操作或复杂循环。
- 明确事务回滚条件，避免异常被吞掉导致事务无法回滚。
- 不应在同一个类内部通过 `this` 调用事务方法来依赖 Spring 代理。

### Redis

- Redis Key 应统一命名，建议包含业务模块和数据类型，例如：

```text
项目名:模块:业务类型:业务标识
```

- 设置合理的过期时间，避免无期限缓存导致内存持续增长。
- 缓存读取失败时应设计降级策略。
- 更新数据库和删除缓存的顺序应结合业务一致性要求设计。
- 禁止将未经脱敏的敏感信息长期写入 Redis。

### RocketMQ

- 消息生产和消费应具备幂等性。
- 消费失败必须有明确的重试、告警或补偿策略。
- 消费者处理消息前应校验消息内容和业务状态。
- 消息体应使用稳定、可扩展的数据结构，避免随意变更字段含义。
- 禁止在消息消费者中执行无法控制时长的阻塞操作。

## 八、代码风格与命名规范

### 命名

| 类型 | 命名规范 | 示例 |
|---|---|---|
| 类名 | UpperCamelCase | `UserController` |
| 接口名 | UpperCamelCase | `UserService` |
| 实现类 | 接口名加 `Impl` | `UserServiceImpl` |
| 方法和变量 | lowerCamelCase | `findUserById` |
| 常量 | UPPER_SNAKE_CASE | `MAX_RETRY_COUNT` |
| 包名 | 全小写 | `com.bjsxt.service.impl` |
| DTO | 以 `DTO` 结尾 | `UserCreateDTO` |
| VO | 以 `VO` 结尾 | `UserVO` |
| Query | 以 `Query` 结尾 | `UserQuery` |
| Mapper XML | 与 Mapper 接口对应 | `UserMapper.xml` |

### Lombok

可根据对象职责合理使用：

- `@Getter`
- `@Setter`
- `@Data`
- `@NoArgsConstructor`
- `@AllArgsConstructor`
- `@Builder`
- `@Slf4j`

规范要求：

- 持久化对象应谨慎使用 `@Data`，避免 `equals`、`hashCode` 或 `toString` 引发性能和循环引用问题。
- 不应在实体或领域对象中无条件生成包含敏感字段的 `toString`。
- 构造方法、字段访问级别应符合对象封装要求。

## 九、注释与文档规范

- 所有新增类、公共方法和复杂业务逻辑应添加 Javadoc 或必要注释。
- 注释必须使用用户第一语言：**中文**。
- 注释应说明“为什么这样做”，而不仅是重复代码行为。
- SQL、缓存、消息、事务和复杂算法应补充关键设计说明。
- 禁止保留过时、错误或与代码实际行为不一致的注释。
- 临时代码必须使用 `TODO` 标记并说明后续处理事项，例如：

```java
// TODO: 中文说明后续需要完成的功能或优化内容
```

## 十、日志与异常处理规范

- 使用 Lombok 的 `@Slf4j` 或项目统一日志框架记录日志。
- 禁止使用 `System.out.println` 或 `System.err.println` 代替正式日志。
- 日志级别应合理使用：
  - `ERROR`：系统错误、无法恢复的异常
  - `WARN`：潜在问题或可降级异常
  - `INFO`：关键业务流程
  - `DEBUG`：开发调试信息
- 日志中不得输出密码、Token、密钥和完整敏感个人信息。
- 禁止捕获异常后不处理或静默忽略。
- 异常信息应包含必要的业务上下文，但不能泄露系统内部实现细节。
- 统一使用项目约定的异常类型和响应结构。

## 十一、测试规范

- 测试代码统一放在 `src/test/java` 下，并遵循与主代码一致的包结构。
- Service、Mapper 和关键 Controller 应根据业务重要程度编写测试。
- 测试方法应验证正常流程、边界条件和异常流程。
- 测试不应依赖开发者本机的固定路径、个人账号或不可控外部服务。
- 涉及 MySQL、Redis、Nacos 或 RocketMQ 的测试，应明确使用测试环境、Mock 或独立测试配置。
- 提交代码前应确保测试通过。

## 十二、编码原则

| 原则 | 要求 |
|---|---|
| SOLID | 保持职责单一、依赖抽象、降低模块耦合 |
| DRY | 抽取重复逻辑，避免复制粘贴 |
| KISS | 优先使用简单、清晰、容易维护的实现 |
| YAGNI | 不实现当前没有明确需求的功能 |
| OWASP | 遵循常见 Web 安全防护要求 |
| 可维护性 | 优先保证代码可读性、可测试性和可扩展性 |
| 兼容性 | 遵循当前 Spring Boot 2.3.4.RELEASE 的 API 和依赖体系 |

## 十三、提交前检查清单

- [ ] 代码位于正确的分层目录中。
- [ ] Controller 未直接调用 Mapper。
- [ ] Service 接口与实现类分离，实现在 `impl` 包中。
- [ ] SQL 使用参数绑定，未拼接用户输入。
- [ ] 输入参数已进行校验。
- [ ] 敏感配置未硬编码。
- [ ] 日志未泄露敏感信息。
- [ ] 新增类、公共方法和复杂逻辑已添加中文注释。
- [ ] 已执行 `mvn test` 或 `mvn clean package`。
- [ ] 未提交 `target`、本地配置和无关临时文件。
- [ ] 未引入与当前 Spring Boot 2.x 不兼容的 Spring Boot 3.x API 或依赖。
