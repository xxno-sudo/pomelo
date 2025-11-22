# 数据源切换测试指南

## 测试步骤

### 1. 启动项目并验证默认SQLite数据源

```bash
# 启动项目
cd pomelo-demo
mvn spring-boot:run

# 验证当前数据源
curl http://localhost:8080/public/datasource/info

# 测试数据源连接
curl http://localhost:8080/public/datasource/test

# 查看用户数据（应该显示SQLite中的示例数据）
curl http://localhost:8080/public/users
```

### 2. 添加新的数据源（以MySQL为例）

```bash
# 添加MySQL数据源
curl -X POST http://localhost:8080/api/datasource/add \
  -H "Content-Type: application/json" \
  -d '{
    "name": "mysql-test",
    "url": "jdbc:mysql://localhost:3306/test_db",
    "username": "root",
    "password": "password",
    "driverClassName": "com.mysql.cj.jdbc.Driver",
    "isDefault": false
  }'
```

### 3. 查看所有数据源配置

```bash
# 查看所有数据源
curl http://localhost:8080/api/datasource/list

# 查看数据源状态
curl http://localhost:8080/api/datasource/status
```

### 4. 启用并切换到新数据源

```bash
# 启用MySQL数据源
curl -X POST http://localhost:8080/api/datasource/enable/mysql-test

# 切换到MySQL数据源
curl -X POST http://localhost:8080/api/datasource/switch/mysql-test

# 验证切换结果
curl http://localhost:8080/public/datasource/info

# 测试新数据源连接
curl http://localhost:8080/public/datasource/test
```

### 5. 验证数据源切换效果

```bash
# 查看当前数据源的用户数据（应该是MySQL中的数据）
curl http://localhost:8080/public/users

# 在新数据源中创建用户
curl -X POST http://localhost:8080/public/users \
  -H "Content-Type: application/json" \
  -d '{"username":"mysql-user","email":"mysql@example.com"}'

# 切换回SQLite数据源
curl -X POST http://localhost:8080/api/datasource/switch/sqlite-default

# 验证切换回SQLite
curl http://localhost:8080/public/datasource/info

# 查看SQLite中的用户数据（应该不包含刚才在MySQL中创建的用户）
curl http://localhost:8080/public/users
```

### 6. 设置默认数据源

```bash
# 将MySQL设置为默认数据源
curl -X POST http://localhost:8080/api/datasource/default/mysql-test

# 重启应用后验证默认数据源
curl http://localhost:8080/public/datasource/info
```

## 故障排除

### 1. 数据源切换不生效

**症状**：调用切换API返回成功，但查询数据时仍然使用旧数据源

**解决方案**：
- 检查日志中是否有数据源刷新的信息
- 调用测试接口验证连接：`curl http://localhost:8080/public/datasource/test`
- 查看数据源状态：`curl http://localhost:8080/api/datasource/status`

### 2. 新数据源连接失败

**症状**：添加数据源时验证失败

**解决方案**：
- 确认数据库服务已启动
- 检查连接参数（URL、用户名、密码）
- 确认数据库驱动已正确配置
- 检查网络连接和防火墙设置

### 3. MyBatis映射问题

**症状**：切换数据源后查询失败

**解决方案**：
- 确保目标数据库中存在相应的表结构
- 检查MyBatis的SQL语句是否兼容目标数据库
- 查看应用日志中的SQL执行信息

## 数据库准备

### MySQL示例

```sql
-- 创建测试数据库
CREATE DATABASE test_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE test_db;

-- 创建用户表
CREATE TABLE demo_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 插入测试数据
INSERT INTO demo_user (username, email) VALUES 
('mysql-admin', 'mysql-admin@example.com'),
('mysql-user1', 'mysql-user1@example.com');
```

### PostgreSQL示例

```sql
-- 创建用户表
CREATE TABLE demo_user (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(100),
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 插入测试数据
INSERT INTO demo_user (username, email) VALUES 
('pg-admin', 'pg-admin@example.com'),
('pg-user1', 'pg-user1@example.com');
```

## 监控和日志

### 关键日志信息

- `数据源管理器初始化完成`：数据源管理器启动成功
- `已从数据源 X 切换到数据源: Y`：数据源切换成功
- `动态数据源配置已刷新`：数据源映射更新成功
- `当前使用数据源: X`：显示当前正在使用的数据源

### 调试技巧

1. 启用MyBatis SQL日志，在`application.yml`中添加：
```yaml
logging:
  level:
    com.cuckoo.pomelo.demo.mapper: DEBUG
```

2. 启用数据源调试日志：
```yaml
logging:
  level:
    com.cuckoo.pomelo.common.dao.datasource: DEBUG
```

3. 监控数据源连接池状态（如果使用HikariCP）：
```yaml
logging:
  level:
    com.zaxxer.hikari: DEBUG
```