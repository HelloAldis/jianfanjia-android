# jianfanjia-android
简繁家branch user-2.5 模块和开发规则介绍：</br>
</br>
整个项目分为common,api,hub,pullrefresh,app_user,app_designer等几个模块，其中app_user,app_designer分别对应的是业主版app和
专业版app，其余模块均为library 模块。</br>
</br>
1、common为对最顶层的模块，里面主要放baseaplication,以及一些与项目无关的工具类，以后新增的工具类都可以添加到common tool包中。</br>
</br>
2、api模块是对网络请求和响应进行封装的模块，所有的与服务器对应request和返回的model都写在api模块中。</br>
</br>
3、hub模块是对加载进度对话框进行封装的模块，app模块引用它实现数据加载的效果。
