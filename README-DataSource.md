# Pomelo 动态数据源脚手架使用说明

## 功能特性

1. **本地SQLite数据库**：项目启动时自动创建本地SQLite数据库作为默认数据源
2. **动态数据源切换**：支持运行时动态添加、切换数据源，无需重启应用
3. **在线配置管理**：提供REST API管理数据源配置
4. **MyBatis + Common Mapper**：完整的ORM框架支持
5. **连接池管理**：使用HikariCP连接池，支持多数据源连接池管理

## 快速开始

### 1. 启动项目

```bash
cd pomelo-demo
mvn spring-boot:run
```

项目启动后会自动：
- 创建本地SQLite数据库文件 `pomelo-datasource.db`
- 初始化数据源配置表和示例用户表
- 插入示例数据

### 2. 验证基本功能

访问以下接口验证功能：

```bash
# 查看当前数据源
curl http://localhost:8080/public/datasource/info

# 查看示例用户数据
curl http://localhost:8080/public/users

# 创建新用户
curl -X POST http://localhost:8080/public/users \
  -H "Content-Type: application/json" \
  -d '{"username":"test","email":"test@example.com"}'
```

## 数据源管理API

### 查看所有数据源配置
```bash
curl http://localhost:8080/api/datasource/list
```

### 添加新数据源
```bash
curl -X POST http://localhost:8080/api/datasource/add \
  -H "Content-Type: application/json" \
  -d '{
    "name": "mysql-prod",
    "url": "jdbc:mysql://localhost:3306/pomelo_prod",
    "username": "root",
    "password": "password",
    "driverClassName": "com.mysql.cj.jdbc.Driver",
    "isDefault": false
  }'
```

### 切换数据源
```bash
curl -X POST http://localhost:8080/api/datasource/switch/mysql-prod
```

### 启用数据源
```bash
curl -X POST http://localhost:8080/api/datasource/enable/mysql-prod
```

### 设置默认数据源
```bash
curl -X POST http://localhost:8080/api/datasource/default/mysql-prod
```

## 支持的数据库类型

- **SQLite**（默认）：`org.sqlite.JDBC`
- **MySQL**：`com.mysql.cj.jdbc.Driver`
- **PostgreSQL**：`org.postgresql.Driver`
- **Oracle**：`oracle.jdbc.OracleDriver`
- **SQL Server**：`com.microsoft.sqlserver.jdbc.SQLServerDriver`

## 配置说明

### application.yml 配置

```yaml
# MyBatis配置
mybatis:
  config-location: classpath:mybatis-config.xml
  mapper-locations: classpath:mapper/**/*.xml
  type-aliases-package: com.cuckoo.pomelo.demo.entity

# 通用Mapper配置
mapper:
  mappers:
    - io.mybatis.mapper.Mapper
  not-empty: true
  identity: SQLITE  # 根据当前数据源类型调整

# 分页插件配置
pagehelper:
  helper-dialect: sqlite  # 根据当前数据源类型调整
  reasonable: true
  support-methods-arguments: true
  params: count=countSql
```

## 使用示例

### 1. 创建实体类

```java
@Data
@Entity.Table("your_table")
public class YourEntity {
    @Entity.Column(id = true, updatable = false)
    private Long id;
    
    @Entity.Column
    private String name;
    
    // 其他字段...
}
```

### 2. 创建Mapper接口

```java
public interface YourMapper extends Mapper<YourEntity, Long> {
    // 自定义查询方法
    @Select("SELECT * FROM your_table WHERE name = #{name}")
    YourEntity findByName(String name);
}
```

### 3. 创建Service

```java
@Service
public class YourService {
    @Autowired
    private YourMapper yourMapper;
    
    public YourEntity findById(Long id) {
        return yourMapper.selectByPrimaryKey(id).orElse(null);
    }
    
    // 其他业务方法...
}
```

## 注意事项

1. **数据源切换**：切换数据源后，新的数据库操作将使用新数据源，但不影响已有的事务
2. **连接池管理**：每个数据源都有独立的连接池，切换数据源时会自动管理连接池
3. **事务管理**：支持分布式事务，但建议在同一个事务中使用同一个数据源
4. **性能考虑**：频繁切换数据源可能影响性能，建议根据业务场景合理设计

## 故障排除

### 1. 数据源连接失败
- 检查数据库连接参数是否正确
- 确认数据库服务是否启动
- 检查网络连接和防火墙设置

### 2. SQLite文件权限问题
- 确保应用有读写当前目录的权限
- 检查SQLite文件是否被其他进程占用

### 3. MyBatis配置问题
- 检查Mapper接口是否正确扫描
- 确认实体类注解配置是否正确
- 查看MyBatis日志输出

## 扩展开发

如需扩展功能，可以：

1. **自定义数据源类型**：在`DynamicDataSourceManager`中添加新的数据源类型支持
2. **添加数据源监控**：集成监控组件，监控数据源连接状态和性能
3. **配置加密**：对数据源密码进行加密存储
4. **集群支持**：支持多实例间的数据源配置同步