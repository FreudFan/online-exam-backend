# online-exam-backend

#### 项目简介
online-exam-backend 是一个在线考试系统的后端模块。
基于 Jersey + SpringBoot 实现的的 restful 服务，主要包括用户管理、在线考试，自动批卷、成绩管理、错题管理、留言板、试卷管理、题库管理、试题科目维护等功能。

#### 项目源码

|     |   github  |
|---  |--- |
|  后端源码   |  https://github.com/FreudFan/online-exam-backend   |
|  安卓源码   |  https://github.com/FreudFan/online-exam-android   |

### 架构概览
基于 SpringBoot 生态，接口层使用 Jersey 提供 restful 服务  
数据持久层使用 Spring JdbcTemplate, 数据库采用 mysql8, 数据库连接池使用 druid  
支持负载均衡、session 由 redis 集中管理  
cache 管理使用 spring cache Redis 集中管理  
采用基于 token 的身份认证机制  
日志系统使用 slf4j + log4j2  
接口文档使用 swagger 自动生成，访问项目端口可进到文档页面  
*数据库表结构及测试数据和文件上传测试数据在 resources/data 目录下  

#### 功能介绍
组织结构管理、用户信息管理、在线考试、实时打分、统计错题、成绩管理、题库管理、自动生成试卷、文件上传导入题库