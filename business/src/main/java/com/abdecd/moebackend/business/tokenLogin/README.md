### 依赖

```xml
<dependency>
    <groupId>com.auth0</groupId>
    <artifactId>java-jwt</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

### 配置

common/TokenLoginProp

```java
private Integer jwtTtlSeconds;
/**
 不拦截的请求，如可以这样配置：
 - /error
 - /doc.html/**
 - /swagger-ui.html/**
 - /v3/api-docs/**
 - /webjars/**
 */
private String[] excludePatterns = new String[0];
/**
 * 测试模式; true 时启用虚拟用户允许所有请求 详见 interceptor/LoginInterceptor.inTest()
 */
private Boolean test = false;
```

例子

```yml
token-login:
  jwtTtlSeconds: 86400
  exclude-patterns:
    - /error
    - /doc.html/**
    - /swagger-ui.html/**
    - /v3/api-docs/**
    - /webjars/**
    - /login
    - /register
```

### 常量

common/TokenLoginConstant

```java
String K_USER_ID = "id";  
String K_PERMISSION = "permission";  
String JWT_TOKEN_NAME = "token";  
Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*?&]{6,20}$");  
String LOGIN_TOKEN_BLACKLIST = "xxx:loginTokenBlacklist:";  
```
