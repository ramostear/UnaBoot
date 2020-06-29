![](./src/main/resources/static/ub-admin/img/una-logo-max.png)
<center>UnaBoot | 尤娜</center>
<center>一个基于Spring Boot 2.0 构建的Java博客系统</center>

----

[![GitHub license](https://img.shields.io/github/license/ramostear/UnaBoot)](https://github.com/ramostear/UnaBoot/blob/master/LICENSE) ![](https://img.shields.io/badge/author-ramostear-brightgreen) ![](https://img.shields.io/badge/jdk-1.8-brightgreen) ![](https://img.shields.io/badge/maven-3.3.0-brightgreen) ![](https://img.shields.io/badge/version-1.3.0-brightgreen) [![](https://img.shields.io/badge/download-github-brightgreen)](https://github.com/ramostear/UnaBoot.git) [![](https://img.shields.io/badge/download-gitee-green)](https://gitee.com/ramostear/UnaBoot.git)


----
## 简介		

​		UnaBoot是一款基于Spring Boot 2.0构建的国产Java博客系统，在此之前，我不断的尝试使用过不同的博客系统，如基于PHP的WordPress、基于Node.js的静态博客系统HEXO、基于Java的CMS系统JEECMS和MCMS等，这些系统都有着不错的表现，能够满足绝大多数的需求场景。那问题来了，我为什么还要重复性的造一个“轮子”呢？一个简单的比喻，前面提到的系统都很强大，有的像“卡车轮子”，有的像“坦克履带”，有的像“跑车轮子”,它们的性能，功能都很强大。然而，作为一个经常写博客文章的我，我现在需要的是小巧的“电摩轮子”。基于这样的一个需求，也参考了上述诸多优秀CMS系统的设计，站在“巨人”的肩膀上，完成了UnaBoot系统的设计和开发。

​		UnaBoot的初衷是提供一个极简的内容创作平台，给热爱技术，热爱写作的小伙伴一个简洁，易用的写作软件。因此，UnaBoot系统在设计之初就只保留了一个博客网站最核心的几个功能模块，它们分别是栏目、文章、主题、友链、标签、归档、存储和评论，共计八个主要核心功能。为了尽可能的降低UnaBoot的使用门槛，UnaBoot基于Freemarker模板引擎开发了一套内置的内容标签，通过使用这些标签，对于不能熟练使用Java编程语言的编程爱好者，也能快速的构建出一套漂亮的系统主题。UnaBoot会自动根据各类标签加载对应的数据，完成主题的渲染。
        
        
   经过不断的迭代，UnaBoot现已更新到1.3.0版本。在新版本中，做了大量的优化，例如：增加了栏目授权机制，可以根据需要，将栏目分配给不同的文章编辑员，同时新增了Word风格的文档编辑器，对于习惯了Office办公软件的创作者，又多了一种写作方式的选择（系统默认使用Markdown编辑器）。

​
	


## 版本
| 时间             | 版本号                           | 说明                                                         |
| ---------------- | ------------------------------ | ------------------------------------------------------------ |
|2020-06-29|UnaBoot 1.3.0|提供两种风格编辑器（Markdown和Office Word）,提供栏目分配功能，优化主题管理功能（类似云盘操作）|
|2020-04-07|Una-Boot-1.2.2-RELEASE|项目地址：[Gitee](https://gitee.com/ramostear/UnaBoot) [Github](https://github.com/ramostear/UnaBoot)|
|2020-04-04|Una-Boot-1.2.2|提供默认主题:卡特琳娜,修改文章字段校验规则|
|2020-03-20|Una-Boot-1.2.0|新版尤娜开源，改进主题解析方式，取消主题刷新；新增在线安装向导|
|2019-11-28|Una-Boot-1.0.0|尤娜博客基础版上线|


## 项目地址

​		你可以使用git工具从[Github](https://github.com)或者[Gitee](https://gitee.com)上获取尤娜博客最新的源代码：

```
git clone https://github.com/ramostear/UnaBoot.git
```

```
git clone https://gitee.com/ramostear/UnaBoot.git
```

## 主要特点

- 完全开源：基于AGPL-3.0协议开源
- 快速初始化：通过安装向导，快速完成站点初始化工作
- 标签化建站：尤娜内置了内容标签和内容函数，可以快速的完成模板的制作
- 多主题：支持多个主题自由切换，快速改变站点风格，而不需重新编译后台代码
- Markdown支持: 内置markdown编辑器
- Office Word支持: 新增Office Word风格编辑器
- 分级授权：支持栏目授权操作（基于Shiro实现）
- 定时任务：支持定时发文操作
- 动态验证码：基于[Happy-Captcha](https://github.com/ramostear/Happy-Captcha) 快速验证码框架实现
- 文件存储：支持本地存储和CDN存储
- 评论支持：内置了Gitalk评论函数，只需设置相关的Gitalk参数即可拥有评论功能
- IP黑名单：提供IP黑名单管理功能（付费版）
- 一键导入：提供一件导入历史数据功能（付费版）
- 评论系统：内置评论管理模块（付费版）
- 前后端分离：提供前后端分离功能（付费版）




## 开发环境

​	建议您使用下面推荐的环境与尤娜玩耍，以避免版本不一致所带来的困扰

- OS: Windows 7/10,Linux
- IDE: Eclipse，IntelliJ IDEA(推荐)
- DB：MySQL 5.6+
- JDK: JDK8+
- Web Server: Apache Tomcat 8+
- Maven: Maven 3.0+


## 运行环境

- OS: Windows Server/Linux Server
- JRE: JRE 8+
- MySQL: MySQL 5.6+
- Web Container: Apache Tomcat 8+
- Nginx: Nginx 2.0+(非必须)

## 技术框架

尤娜所使用的开发框架明细：

| 框架             | 说明                           | 官网                                                         |
| ---------------- | ------------------------------ | ------------------------------------------------------------ |
| Spring Framework | 轻量级(相对而言)的Java开发框架 | https://spring.io/projects/spring-framework                  |
| Spring Boot      | Java Web开发脚手架             | https://spring.io/projects/spring-boot                       |
| Apache Shiro     | 安全控制框架                   | [https://shiro.apache.org](https://shiro.apache.org/)        |
| Hibernate        | 对象关系映射框架               | [http://hibernate.org](http://hibernate.org/)                |
| Freemarker       | 视图模板引擎                   | [https://freemarker.apache.org](https://freemarker.apache.org/) |
| Log4J            | 日志记录组件                   | [https://logging.apache.org](https://logging.apache.org/)    |
| Druid            | 数据库链接池                   | [https://druid.apache.org](https://druid.apache.org/)        |
| FastJSON         | JSON解析库                     | [FastJson](https://github.com/alibaba/fastjson/wiki)         |
| EhCache          | 基于Java的进程内缓存框架       | [http://www.ehcache.org](http://www.ehcache.org/)            |
| pinyin4j         | 中文转拼音的Java库             | https://sourceforge.net/projects/pinyin4j/                   |
| Maven            | 项目构建                       | [https://maven.apache.org](https://maven.apache.org/)        |
| lombok           | 代码生成器                     | [https://projectlombok.org](https://projectlombok.org/)      |
| HappyCaptcha     | 验证码生成框架                  | [Happy-Captcha](https://github.com/ramostear/Happy-Captcha)|
| Shiro-Freemarker-Tags|Freemarker&Shiro权限标签    | [Shiro-Freemarker-Tags](https://github.com/ramostear/shiro-freemarker-tags)|


## 更多

### 部分功能预览


### 更多详情

你可以访问[https://www.ramostear.com](https://www.ramostear.com) 获取更多详细信息！
