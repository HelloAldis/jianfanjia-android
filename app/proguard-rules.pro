# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Documents and Settings\Administrator\AppData\Local\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-keepattributes Signature
-keep class com.jianfanjia.cn.bean.** { *; }

#个推混淆
-dontwarn com.igexin.**
-dontwarn android.support.**
-keep class com.igexin.**{*;}
-keep class android.support.v4.** { *; }
-keep interface android.support.v4.app.** { *; }

#ormlite混淆
-dontwarn com.j256.**
-keep class org.slf4j.** {*;}
-keep class com.j256.ormlite.** { *; }
-keep class com.j256.ormlite.android.** { *; }
-keep class com.j256.ormlite.field.** { *; }
-keep class com.j256.ormlite.stmt.** { *; }

#okio混淆
-dontwarn com.squareup.okhttp.**
-keep class com.squareup.okhttp.** { *;}
-dontwarn okio.**

#androidannotations混淆
-dontwarn org.androidannotations.**