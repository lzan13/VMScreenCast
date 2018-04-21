VMScreenCast
========

使用 Cling 实现了 DMS DMC 功能的投屏应用

### #简介
本项目使用 [4thline/cling](https://github.com/4thline/cling) 库实现投屏功能，其中实现了`DMS`及`DMC`功能，
测试是通过智能电视和盒子设备，包括手机端安装 AirPin 软件进行测试


### #实现功能
- 浏览本地资源投屏播放
- 请求网络资源，投屏播放网络视频
- 控制投屏设备，静音，拖动，播放，暂停等
- 根据投屏设备播放状态，同步更新本地设备播放状态，包括进度音量等


### #遇到的问题及解决
- 首先是在刚开始用另一台手机安装 AirPin 测试的，测试`设置音量`和`视频的拖动进度`不可用投屏设备无效，
后来在真正的智能电视和盒子上测试有效，这个应该是因为手机设备本身不支持导致
- 后来在只能电视和盒子上测试时发现`BaseSubscriptionCallback`这个回调部分支持的并不全，
比如在 AirPin 上测试回调会有当前播放进度，当前音量和静音状态，这个在电视和盒子上是都不会回调的，
所以这里为了兼容实现，这里做了调整，改为手动去开定时器去获取当前播放进度和声音状态
- 现在又遇到一个新的问题，就是在小米盒子上，获取到的声音永远是 0，这个暂时还没找到解决方案


### #参考&感谢
- [Cling DLNA库](https://github.com/4thline/cling)
- [DLNA 百科介绍](https://baike.baidu.com/item/DLNA)
- [简书-细卷子](https://www.jianshu.com/p/4452182d2b48)
- [hubing8658/UPnP-DLNA-Demo](https://github.com/hubing8658/UPnP-DLNA-Demo)
- [kevinshine/BeyondUPnP](https://github.com/kevinshine/BeyondUPnP)