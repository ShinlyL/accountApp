# accountApp
Java、Android、SQLite

学校大四时的Android课程设计项目

一个具有记账、查看与统计、用户登录注册的个人记账应用

<b>本应用的几点说明</b>

1. 本应用为满足课程设计要求，没使用任何第三方框架技术，所以一切控件的实例化方式都是原生api方法。 

2. 本应用只使用了数据库的一小部分功能，关于用户的增改查和记账信息的增删改查，是的，因为没有不需要用户的删除功能，所以关于用户的删除，建议使用SQLite的官方管理工具 ```sqlitestudio``` ，进行账号的管理。

3. 本应用在用户登录模块设计了的自动登录和记住账号功能。

4. 本应用的数据都来源于本地和用户自行添加的，所以没有网络需求，不涉及网络编程。

   <h3>以下是部分应用功能界面介绍</h3>

![img](file:////./rmd_img/image1.png)

<center>图1 记账宝登录界面</center>

![img](file:///C:/Users/ADMINI~1/AppData/Local/Temp/msohtmlclip1/01/clip_image002.jpg)

<center>图2 记账宝注册界面</center>

![img](file:///C:/Users/ADMINI~1/AppData/Local/Temp/msohtmlclip1/01/clip_image002.jpg) ![img](file:///C:/Users/ADMINI~1/AppData/Local/Temp/msohtmlclip1/01/clip_image004.jpg) ![img](file:///C:/Users/ADMINI~1/AppData/Local/Temp/msohtmlclip1/01/clip_image006.jpg)

​              图3 记账显示界面                                   图4 记账统计页面                                     图5 用户信息页面

![img](file:///C:/Users/ADMINI~1/AppData/Local/Temp/msohtmlclip1/01/clip_image002.jpg)   ![img](file:///C:/Users/ADMINI~1/AppData/Local/Temp/msohtmlclip1/01/clip_image004.jpg)

<center>图6 记账按钮点击事件触发及输入栏效果展示

![img](file:///C:/Users/ADMINI~1/AppData/Local/Temp/msohtmlclip1/01/clip_image006.jpg)   ![img](file:///C:/Users/ADMINI~1/AppData/Local/Temp/msohtmlclip1/01/clip_image008.jpg)

<center>图7 统计记账信息展示