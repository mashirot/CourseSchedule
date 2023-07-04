## 使用

clone 到本地之后，`mvn package`，到 target 文件夹下找到 jar 文件

复制到你要的文件夹，在同级目录创建一个 `application.yml`，填写如下内容
```yml
server:
  port: 8080
spring:
  datasource:
    druid:
      driver-class-name: com.mysql.cj.jdbc.Driver
#      下面改成你自己的
      url: jdbc:mysql://url
      username: username
      password: passwd
  jackson:
    time-zone: GMT+8
mybatis:
  mapper-locations: classpath://ski/mashiro/dao/*.xml
  configuration:
    map-underscore-to-camel-case: true
cors:
  configuration:
    allowedOriginPatterns: https://schedule.mashiro.ski
    allowCredentials: true
    allowedMethods: "*"
    allowedHeaders: "*"
    exposedHeaders: "*"
```

然后打开shell，`java -jar ./CourseSchedule-2.0.0.jar`