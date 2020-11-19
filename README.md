# 头条资讯

## 涉及技术

SpringBoot、Spring、SpringMVC、MyBatis-Plus、七牛云、Redis、MySQL 8、Thymeleaf、Bootstrap

## 项目实现功能

1. 用户注册、登录

2. 资讯首页分页展示

3. 图片上传及管理

4. 资讯发布、资讯排序、资讯分类

5. 用户评论、用户点赞、用户点踩

6. 站内信通知

7. 邮件通知

## 细节改进

1. 资讯、评论、站内信分页使用PageHelper分页插件实现（设置页码数量navigatePages）、前端搭配Bootstrap调用PageInfo类的导航页数组navigatepageNums（默认为8，修改为5）使页码最多显示当前页码附近的5页。
2. 使用Bootstrap navbar-static-top样式使导航条静止在顶部，导航条添加站内信未读消息图标、当前用户头像、注销按钮。
3. 阅读站内信后清除未读数字。
4. 赞踩功能逻辑修改（likeCount = 点赞数 - 点踩数），编写前后端对应的代码，使点击后在添加或删除pressed样式的同时，修改Redis数据并使用Redis实现异步架构操作数据库。
