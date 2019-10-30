# 诊所在线预约系统微信企业号前端

本项目包含两部分，一个是部署在学校微信平台上的前端，另一个是负责转发所有请求的Grails脚本。

## 特性

- 使用 Vue.js + VUX 提供最佳体验
- 提供公告、日历、当前工单、历史工单等等功能
- 接入学校微应用平台


## 访问方式

北京理工大学微信企业号 -> 网络服务 -> 电脑义诊

## 项目架构图

![架构](./docs/images/archi.svg)

## 项目结构

- `records/` 微信前端
- `script_playground/` 模拟学校服务的本地代理
- `groovy-scripts` 发布到生产环境的代理脚本
- `misc/` 其他内容

## 使用方式

运行 Grails 代理服务

```bash
cd script_playground
# 确保使用 java-6, grails 2.3.11
grails run-app
```

运行前端服务，请参考相应[readme](./records/README.md)

打包最终版本

```bash
bash packing.sh

# 将会生成 package-clinic.zip 即可用于上传
```

git tag
```bash
# tag_it.sh 会读取 `misc/lightapp.json` 中的版本号
bash tag_it.sh
```

## FAQ

### 如何创建本地开发环境

通常我会在本地同时启动 django, grails, wechat前端部分。

### 如何测试并修改代理脚本

`./script_playground/grails-app/controllers/script_playground/ApiController.groovy` 这个文件实际上是开发环境的代理脚本，发布版本的脚本作为一个闭包运行在这里。

但是请注意，所有的修改要保证最终应用到 `./groovy-scripts/requestProxy.groovy` 之中。


## Contributes

欢迎提交 Issues 和 PR
