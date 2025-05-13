# Server

## 项目简介
该项目是一个Java服务器应用程序，包含多个模块，如server-admin、server-common、server-framework等。每个模块负责不同的功能，如管理、通用功能、框架支持等。

Server对应的前端项目是一个基于Vue3的后台管理系统，项目地址：https://github.com/851543/Admin-Vue3.git

## 安装步骤
1. 克隆项目到本地：
   ```bash
   git clone <repository-url>
   ```
2. 进入项目目录：
   ```bash
   cd Java/Server
   ```
3. 使用Maven构建项目：
   ```bash
   mvn clean install
   ```

## 使用说明
- 启动服务器：
  进入目标模块目录并运行：
  ```bash
  mvn spring-boot:run
  ```
- 访问API接口：
  通过浏览器或Postman访问`http://localhost:<port>`。

## 贡献指南
欢迎贡献代码！请确保在提交PR之前运行所有测试并遵循代码风格指南。