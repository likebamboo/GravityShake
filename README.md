GravityShake
============
## 关于

该程序是一个运用手机加速度传感器(```Sensor.TYPE_ACCELEROMETER```)实现的一个特效程序。  
具体来说，是在一张图片(该图片可能还支持手势缩放)上绘制一个或多个小标签(图片)，这些小标签能够随手机的晃动(或转动)而左右摆动(有点类似单摆，但是是阻尼摆动，最终静止，感觉更像风铃的那种效果)。  
另外，该程序还支持小标签点击事件的监听，即使在底图放大或缩小的情况下。


## 安装

apk文件夹下附带安装包(未签名)。

## 编译与运行

本项目开发工具为Eclipse，你可以将source文件夹下的项目导入到eclipse中编译并运行。   
如果你使用ant来编译构建项目，你需要在 source/GravityShake 文件夹下建立一个local.properties文件，该文件的内容为

    sdk.dir=【Android SDK所在的目录】   

之后，你可以在命令行中定位到GravityShake文件夹，执行ant debug命令来编译该项目。

## 预览

(由于在模拟器上不好模拟手机晃动，需要用真机测试，录制屏幕又比较麻烦，所以预览效果稍后给出，大家可以先自己安装下应用程序，看看效果。有任何问题请及时反馈给我)

## 用到的第三方库

* [PhotoView](https://github.com/chrisbanes/PhotoView) (非必须)

## 关于我

[I am  here](http://likebamboo.github.io/)。
