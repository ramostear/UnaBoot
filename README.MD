![](https://camo.githubusercontent.com/6046932fae162f7487297a76d71c26e41ae14e23/68747470733a2f2f63646e2e72616d6f73746561722e636f6d2f756e61626f6f742e706e67)

[![GitHub license](https://img.shields.io/github/license/ramostear/UnaBoot-Pro)](https://github.com/ramostear/UnaBoot-Pro/blob/master/LICENSE) ![](https://img.shields.io/badge/author-ramostear-brightgreen) ![](https://img.shields.io/badge/jdk-1.8-brightgreen) ![](https://img.shields.io/badge/maven-3.3.0-brightgreen) ![](https://img.shields.io/badge/version-1.2.0-brightgreen) [![](https://img.shields.io/badge/download-github-brightgreen)](https://github.com/ramostear/UnaBoot-Pro.git) [![](https://img.shields.io/badge/download-gitee-green)](https://gitee.com/ramostear/UnaBoot-Pro.git)


----
>大家好，我是尤娜博客(una-boot)的作者树下魅狐，自2019年9月una-boot-1.0.0版本开源以来，陆续有热爱技术，钟爱技术写作的小伙伴为尤娜提出了宝贵的改进意见和建议，在此表示衷心的感谢，谢谢大家的支持！	

## 简介		

​		尤娜(una-boot)是一款基于Spring Boot 2.0构建的国产Java博客系统，在此之前，我不断的尝试使用过不同的博客系统，如基于PHP的WordPress、基于Node.js的静态博客系统Hexo、基于Java的CMS系统JEECMS和MCMS等，这些系统都有着不错的表现，能够满足绝大多数的需求场景。那问题来了，我为什么还要重复性的造一个“轮子”呢？一个简单的比喻，前面提到的系统都很强大，有的像“卡车轮子”，有的像“坦克履带”，有的像“跑车轮子”,它们的性能，功能都很强大。然而，作为一个经常写博客文章的我，我现在需要的是小巧的“电摩轮子”。基于这样的一个需求，也参考了上述诸多优秀CMS系统的设计，站在“巨人”的肩膀上，完成了尤娜博客系统的设计和开发。

​		尤娜的初衷是提供一个极简的内容创作平台，给热爱技术，热爱写作的小伙伴一个简洁，易用的写作软件。因此，尤娜博客系统在设计之初就只保留了一个博客网站最核心的几个功能模块，它们分别是栏目、文章、主题、友链、标签、归档、存储和评论，共计八个主要核心功能。为了尽可能的降低尤娜的使用门槛，尤娜基于Freemarker模板引擎开发了一套内置的博客标签，通过使用这些标签，对于不能熟练使用Java编程语言的小伙伴，也能快速的构建出一套漂亮的博客主题。尤娜会自动根据各类标签加载对应的博客数据，完成主题的渲染。

​		由于抗击疫情以及工作的原因，原定于2020年1月份上线una-boot-1.2.0版本的计划推迟到了3月份。在una-boot-1.2.0版本中，新增了在线安装并初始化博客的功能，免去了部署系统是来回修改配置文件的复杂过程。在新版本中，尤娜使用了动态数据源切换技术，系统安装完成后，不需要再重启Apache Tomcat，尤娜会自动切换到博主指定的数据源上。接下来，我将简要介绍una-boot-1.2.0版本的大致情况。



## 主要特点

- 完全开源：基于AGPL-3.0协议开源
- 快速初始化：通过安装向导，快速完成站点初始化工作
- 标签化建站：尤娜内置了内容标签和内容函数，可以快速的完成模板的制作
- 多主题：支持多个主题自由切换，快速改变站点风格，而不需重新编译后台代码
- Markdown支持: 内置markdown编辑器
- 文件存储：支持本地存储和CDN存储
- 评论支持：内置了Gitalk评论函数，只需设置相关的Gitalk参数即可拥有评论功能
- Spring Boot: 基于Spring Boot 2.0版本进行构建



## 开发环境

​	建议您使用下面推荐的环境与尤娜玩耍，以避免版本不一致所带来的困扰

- OS: Windows 7/10,Linux
- IDE: Eclipse，IntelliJ IDEA(推荐)
- DB：MySQL 5.6+
- JDK: JDK8+
- Web Server: Apache Tomcat 8+
- Maven: Maven 3.0+



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



## 快速开始

​		你可以按照下列的方式来获取并运行尤娜博客系统。

### 获取源代码

​		你可以使用git工具从[Github](https://github.com)或者[Gitee](https://gitee.com)上获取尤娜博客最新的源代码：

```
git clone https://github.com/ramostear/UnaBoot-Pro.git
```

```
git clone https://gitee.com/ramostear/UnaBoot-Pro.git
```

除此之外，我还提供了可在Tomcat中运行的war包，你可以访问https://gitee.com/ramostear/UnaBoot-Pro/releases/una-boot-v1.2.0或者https://github.com/ramostear/UnaBoot-Pro/releases/tag/una-boot-v1.2.0 下载最新的war到本地运行。

![](https://cdn.ramostear.com/20200322-1aa364ad59df433fb74e2f6bef1b8e9a.png)

<p style="text-align:center;">Gitee下载</p>

![](https://cdn.ramostear.com/20200322-24335ee7477d43c49523d4f3eb7ed9f4.png)

<p style="text-align:center;">Github下载</p>

### 编译源代码

​		如果你是直接下载项目war包，请跳过此步骤。代码克隆到本地后，你可以使用命令行工具或者IDEA对项目源码进行编译，命令如下：

```
mvn compile -Dmaven.test.skip=true
```

待项目编译完成后，便可执行打包操作。

> 注意：
>
> 如果使用IDE自带的Maven工具对项目进行编译时，请检查你的IDE是否安装了Lombok插件，如果缺少Lombok插件，项目编译将会失败。



### 项目打包

​		项目编译完成后，需要对项目进行打包才能运行，如果你使用的是IntelliJ IDEA或者STS等工具，可以直接运行UnaBootProApplication.java文件中的main()方法来启动项目。如果你想将项目放到外部的Tomcat中运行，请参照下面的打包命令：

```
mvn clean package -Dmaven.test.skip=true
```

打包成功后，你可以在项目的target目录中找到一份名为una-boot-pro-1.2.0.war的文件包，此文件就是运行项目的最终文件。

### 启动项目

​		将打包好的或者下载的una-boot-pro-1.2.0.war文件拷贝到Apache Tomcat安装目录下的webapps目录中，然后启动Apache Tomcat。

> 注意：
>
> 尤娜博客系统需要在Apache Tomcat 8及以上的版本中运行

![](https://cdn.ramostear.com/20200322-852f88694c86440a8e2dc1e7436fc8cf.png)

<p style="text-align:center;">拷贝war文件到tomcat</p>

![](https://cdn.ramostear.com/20200322-5981e25ec0dc4bc8abbea64eafd9f508.png)

<p style="text-align:center;">启动Apache Tomcat</p>

Apache Tomcat启动完成后，在浏览器中按照下列的格式输入访问地址并访问

```
http://[localhost|127.0.0.1]:[8080/你自己的tomcat端口号]/una-boot-pro-1.2.0/unaboot/install.html
```

> 提示
>
> 如果是第一次启动并访问尤娜博客系统，请在MySQL数据库管理系统中创建一个空的数据库，该数据库在的名称在初始化博客时需要使用。如本次演示所使用的db_una_boot_pro_demo.
>
![](https://cdn.ramostear.com/20200322-1247e27baa134951ae08a95ed7ec09fd.png)



## 安装并初始化尤娜

### 安装向导

​		以我在本地演示为例，浏览器中输入http://localhost:8080/una-boot-pro-1.2.0/unaboot/install.html ,访问成功后，你将看到入下的安装向导界面：

![](https://cdn.ramostear.com/20200322-f25b4931274140e19b13514ece45fb5f.png)

请阅读UnaBoot的许可协议，并勾选同意按钮后，点击“下一步”按钮，填写数据库相关的信息。

### 数据库信息

​		阅读完许可协议并同意后，你可进入数据库配置界面。在此界面中，你需要提供MySQL数据的主机地址（例如localhost或127.0.0.1），数据库的端口号(默认端口号为3306)，存储尤娜博客数据的数据库名称(例如在上一步中创建的db_una_boot_pro_demo数据库)，数据库的访问账号以及访问密码。

![](https://cdn.ramostear.com/20200322-5c79713daac24b21a1d73b7388689c88.png)



### 网站信息

​		在完成数据库配置后，你可以进入站点信息配置界面，配置站点的名称，站点域名，管理员账号以及管理员登录密码，界面如下：

![](https://cdn.ramostear.com/20200322-eaed87fa7b9c4bf080153c5576c978d1.png)

信息确认无误后，点击“确认”按钮，开始初始化博客系统。

> 提示
>
> 请牢记你的站点管理员账号和密码

系统初始化成功后，你将收到如下的系统提示信息：

![](https://cdn.ramostear.com/20200322-45218b1260904c67b4c35eea12f901a7.png)

点击“确定”按钮，系统将跳转到后台登录页面，输入此前配置的管理员账号和密码，登录系统后台。

![](https://cdn.ramostear.com/20200322-0981cbfab29d437ab6e2aee3f1a89045.png)

自此，整个博客的初始化工作完成。接下来，你可以使用自己的管理员账号和密码登录尤娜博客后台，对博客系统进行管理。

> 重要提示
>
> 如果你在安装初始化的过程中，没能成功初始化系统，请检查war包中的WEB-INF/lib/目录下有无ibatis-common-2.2.0.jar文件，如果没有，请将WEB-INF/lib-provided/目录下的ibatis-common-2.2.0.jar文件拷贝到WEB-INF/lib/目录中，然后重启Apache Tomcat。



## 尤娜博客后台管理系统一览

​	在此小结中，我将对尤娜博客后台管理系统做一个简要的介绍。

### 后台主页

​		博客后台主页不要包括了常用功能的快捷入口，如文档地址，接口地址，写作入口，栏目管理入口，网站设置入口，主题管理入口，全文检索设置按钮，缓存清理按钮等。界面如下：

![](https://cdn.ramostear.com/20200322-13bcae9c2fa0497b997b21e784e8f324.png)

尤娜后台管理系统将功能分为了三个板块，分别时内容管理，配置管理和系统管理，下面将分别介绍。

### 内容管理

​		内容管理板块包含了用户管理，栏目管理，博客管理，主题管理，友链管理和标签管理，其相应的界面如下：

![](https://cdn.ramostear.com/20200322-03f24e26374a450784cca6591ed5bbb0.png)

<p style="text-align:center;">用户管理</p>

![](https://cdn.ramostear.com/20200322-6eef67cd91d14a999090aff0c68481e8.png)

<p style="text-align:center;">栏目管理</p>

![](https://cdn.ramostear.com/20200322-896c5d4883094730a7b9c3e76525072d.png)

<p style="text-align:center;">博客管理</p>

![](https://cdn.ramostear.com/20200322-ef2aa4dd4863408ca6b501feafe3eb64.png)

<p style="text-align:center;">写作页面</p>

![](https://cdn.ramostear.com/20200322-22ca739dde154c6080baf6e9e7601c30.png)

<p style="text-align:center;">主题管理</p>

![](https://cdn.ramostear.com/20200322-6c11f6a9c8cd4ddd9493fc0ef4296458.png)

<p style="text-align:center;">友情连接管理</p>

![](https://cdn.ramostear.com/20200322-3691a37c103045b38e29171d2680940a.png)

<p style="text-align:center;">标签管理</p>

### 配置管理

​		配置管理板块主要包括网站常规配置(如站点名称，域名，描述，关键词，Logo，Favicon，邮箱，备案号，主题等)，存储配置（分为本地存储或七牛云存储）,评论配置(关闭评论或开启Gitalk评论插件)。

![](https://cdn.ramostear.com/20200322-eac29f195eed408f8b253c0a75a93a96.png)

<p style="text-align:center;">网站常规配置</p>

![](https://cdn.ramostear.com/20200322-84a8d6faa5054557baed2b07f0076720.png)

<p style="text-align:center">文件存储配置</p>

![](https://cdn.ramostear.com/20200322-827dcc72c87348a0981722ddeb3fca26.png)

<p style="text-align:center">Gitalk评论插件配置</p>

### 系统管理

​		系统管理主要时针对尤娜博客的系统级别的管理，包括定时任务管理，API管理，系统实时日志管理和Druid监控。

![](https://cdn.ramostear.com/20200322-4be630b75a5444358a96b76665c0f484.png)

<p style="text-align:center;">自定义定时任务管理</p>

![](https://cdn.ramostear.com/20200322-3190e791cc6f4accb22632859b15f5a0.png)

<p style="text-align:center;">基于Swagger的API管理</p>

![](https://cdn.ramostear.com/20200322-ceb8af71b60d4e27ad49ef69c55c5e88.png)

<p style="text-align:center;">系统实时日志</p>

![](https://cdn.ramostear.com/20200322-88cf5448a1b04a8087bacae1551db01b.png)

<p style="text-align:center;">数据源监控</p>

