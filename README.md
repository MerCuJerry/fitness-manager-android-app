## 基于Android的健身信息管理系统

本系统使用Rust-Rocket作为服务器，数据库采用MySQL 8.2.0。

[服务器](https://github.com/MerCuJerry/gymcenter-server)
[客户端](https://github.com/MerCuJerry/gymcenter-client-android)

三种用户类型：管理员、教练、会员

输入账号密码后，进入对应用户类型的主界面

### 一些碎碎念

为了偷懒我确实想用原仓库直接上的，但是后来发现原仓库的代码有些逻辑实在我看的不是很下去

所以就有了这个Fork

大部分使用方法都和原仓库差不多 主要是删掉了打卡以及评论那些

然后不要用JDK1.8了 求求了这都2023年了怎么还在用JDK1.8搞开发 少说用个JDK11吧

没想到自己最后还是改了这个服务端（（（