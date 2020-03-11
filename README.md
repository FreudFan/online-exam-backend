# online-exam-backend

#### 项目简介
online-exam-backend 是一个在线考试系统的后端模块。
基于 Jersey+Spring 实现的的 restful 服务，主要包括用户管理、在线考试，自动批卷、成绩管理、错题管理、留言板、试卷管理、题库管理、试题科目维护等功能。

### 架构概览
使用内嵌式 tomcat 9  
接口层使用 Jersey2.29 提供restful服务  
整合了 Spring5.2 进行对象管理  
数据持久层使用 Spring JdbcTemplate, 数据库采用 mysql8, 数据库连接池使用 druid  
支持负载均衡、session 由 redis 集中管理  
采用基于 token 的身份认证机制  
日志系统使用 slf4j + log4j2  
接口文档使用 swagger 自动生成，访问项目端口可进到文档页面  

#### 项目源码

|     |   github  |
|---  |--- |
|  后端源码   |  https://github.com/FreudFan/online-exam-backend   |
|  安卓源码   |  https://github.com/FreudFan/online-exam-android   |
