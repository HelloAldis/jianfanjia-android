# jianfanjia-android
###简繁家branch user-2.5 模块介绍:
</br>
整个项目分为common,api,imageshow,photopicker,hub,pullrefresh,app_user,app_designer等几个模块，其中app_user,app_designer分别对应的是业主版app和
专业版app，其余模块均为library 模块。</br>
</br>
1、common为对最顶层的模块，里面主要放baseaplication,以及一些与项目无关的工具类，以后新增的工具类都可以添加到common tool包中。</br>
</br>
2、api模块是对网络请求和响应进行封装的模块，所有的与服务器对应request和返回的model都写在api模块中。</br>
</br>
3、hub模块是对加载进度对话框进行封装的模块，app模块引用它实现数据加载的效果。</br>
</br>
4、imageshow模块是对图片加载和显示功能进行封装的模块，目前我们依赖的第三方图片加载库是imageaload，以后根据功能的需要，想要换
加载库可以在此进行替换。</br>
</br>
5、photopicker是单独写的一个多图选取的模块，可以同时获取多张图片，目前主要在工地图片上传中用到。
</br>
###简繁家branch user-2.5总体开发原则：
1、能抽象出独立功能的代码尽量封装成模块。</br>
</br>
2、不能封装成模块的功能代码尽量创建在一个工具类中，对外提供统一的调用方法。</br>
</br>
3、base里面只能写最通用的功能，不要为了某些特定范围的子类方便而随便往base里加代码。</br>
</br>
4、base里面不谢util方法，只是重写父类的方法。
