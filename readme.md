头条资讯项目

#一. 是什么
##1. 概述
这个项目源于牛客网中级项目, 目标是建立一个功能比较丰富的热点资讯网站;
toutiao项目展示了一个SpringBoot复杂项目的方方面面的要素, 非常适合用来练习, 分析, 作为下一步开发的模板.

##2. 项目特色
- 整体: 采用Spring Boot快速构建服务
- 前端: Velocity引擎, 后期将考虑用FreeMaker重构一次. 毕竟Velocity好像很久不更新了.
- ORM: mybatis + mysql 
- 内存数据库: redis
- 事件的处理: async部分的事件机制, 类似订阅者模式

#二. 怎么用
##0. 软件环境
Linux或者MacOS最好, Windows也可以;
Java1.9;
MySQL 5.5以上, Redis3.0以后, SequelPro2016年以后;
Intellij Idea 2016以后;

##1. MySQL数据库初始化
本机安装好MySQL;
把MySQL数据库的编码统一设置为utf-8或者utf-8(很重要);

下面是初始化数据库的SQL语句, 这部分语句也保存在了DatabaseInitSQL.spf这个文件中, 可以使用SequelPro打开.

create table toutiao_news (
    nid int PRIMARY KEY AUTO_INCREMENT, 
    title varchar(64), 
    link varchar(128), 
    image varchar(128), 
    like_count int, 
    comment_count int, 
    created_date datetime, 
    uid int
);

create table toutiao_user(
    uid int primary key auto_increment, 
    username varchar(32), 
    password varchar(32), 
    salt char(16), 
    head_url varchar(128)
);

create table toutiao_loginTicket(
    tid int primary key auto_increment, 
    uid int, 
    ticket varchar(64), 
    expired datetime, 
    status int
);

create table toutiao_message(
	msgid int primary key auto_increment,
    from_uid int, 
    to_uid int, 
    content varchar(256), 
    has_read int, 
    created_date datetime, 
    conversation_id varchar(64)
);

create table toutiao_comment(
    cid int primary key auto_increment,
    uid int, 
    entity_id int, 
    entity_type int, 
    content varchar(256), 
    created_date datetime, 
    status int
);

接下来记得在localhost:8080/index页面, 点击右上角"登陆", 然后输入密码注册一个账户叫systemAdmin, 密码可以设置成123456, 它代表的是我们的后台系统管理员. 它的uid在数据库中应该会被默认设置为1, 它专门用来给用户发提示信息.

##2. 配置信息
在resources/application.properties文件中, 配置好datasource.url, username, password信息, 特别注意URL的书写
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/myDatabase?useUnicode=true&characterEncoding=utf-8&useSSL=false
spring.datasource.username=root
spring.datasource.password=root



##3. Redis配置
本机安装Redis, MacOS可以使用brew install redis;
安装好, 请运行/usr/local/Cellar/redis/4.0.2/bin/redis-server, 它会启动一个redis-server后台服务, 让我们可以通过端口localhost:6379对redis数据库进行操作.
配置好ToutiaoUtil中的REDIS_SERVER部分

##4. 使用Intellij Idea试运行
可能出现的问题:
###1 字符编码问题
Error updating database.  
Cause: java.sql.SQLException: Incorrect string value: '\xE5\x8C\x97\xE4\xBA\xAC...' 
for column 'title' at row 1

这是因为编码问题, 需要把MySQL数据库的编码统一设置为utf-8, 因此, 需要重新创建一下所有的表格;
同时, 检查application.properties的第一行是不是这样:
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/myDatabase?useUnicode=true&characterEncoding=utf-8&useSSL=false

记得做好这些配置了要重启服务器, 重启SequelPro;

###2 站内信部分的问题
站内信发送者的头像显示不出来
util/ToutiaoUtil中SYSTEM_UID默认设置为1, 注意如果没有对这个账户进行正常的注册初始化的话, 会对其他用户的站内信列表造成问题;

#三. 为什么(技术架构)
TODO: 完善这个部分的技术架构介绍
##1. SpringBoot框架
##2. MyBatis
##3. Redis
##4. 事件
##5. 设计模式
##6. 前端

#四. 后续改进建议
- 点击链接会出问题: 我
- 清理Test数据, 修改好test脚本
- 增加logout按钮
- 增加用户站内信互相发送的功能
- 增加用户资料设置页面, form表单主要是
- 增加用户修改密码, 设置用户名, 等等用户信息管理
- 增加新闻的修改, 删除等功能
- 七牛云上传图片失败的话要返回一个提示.
- 使用Freemaker/Thymeleaf整体重构该项目前端
- 使用较为新版的SpringBoot进行项目后端整体重构

#参考资料
牛客网中级项目系列视频和相关代码资源等.