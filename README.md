##简介
我目前已知，并且尝试过的获取当前前台应用的方法有如下几种：

1. Android5.0以前，使用ActivityManager的getRunningTasks()方法，可以得到应用包名和Activity；
2. Android5.0以后，通过使用量统计功能来实现，只能得到应用包名；
3. 通过辅助服务来实现，可以得到包名和Activity；
4. Android5.0以后，可以通过设备辅助应用程序来实现，能得到包名和Activity，不过这种方式必须用户主动触发（长按Home键）

**本demo中对这四种方法进行了演示。**

目前最常使用的是**第二种**方法，但**要注意的是，只是这样并不够**因为在一些手机上，
应用发起通知栏消息的时候，或者是下拉通知栏，也会被记录到使用量中，
就会导致按最近时间排序出现混乱，而且收起通知栏以后，这种混乱并不会被修正，而是必须重新开启一个应用才行。
比如下图:

######未改进版，会将通知栏误判成前台应用中：
![未改进版](https://raw.githubusercontent.com/l465659833/RecentTask/master/pics/Original.gif)

######改进版，正确的判断前台应用：

![改进版](https://raw.githubusercontent.com/l465659833/RecentTask/master/gif/Improved.gif)



##相关文章

[4种获取前台应用的方法（肯定有你不知道的）](http://www.jianshu.com/p/a513accd40cd)


##License


![DO WHAT THE FUCK YOU WANT TO PUBLIC LICENSE](http://www.wtfpl.net/wp-content/uploads/2012/12/logo-220x1601.png)


```
Copyright © 2016 l465659833 <l465659833@gmail.com>
This work is free. You can redistribute it and/or modify it under the
terms of the Do What The Fuck You Want To Public License, Version 2,
as published by Sam Hocevar. See the COPYING file for more details.

```
