# 3D 地图 V5.0.0之后
-keep   class com.amap.api.maps.**{*;}
-keep   class com.autonavi.**{*;}
-keep   class com.amap.api.trace.**{*;}
-dontwarn com.amap.apis.utils.core.crash.d$b

# 定位
-keep class com.amap.api.location.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}

# 导航
-keep class com.amap.api.navi.**{*;}
-keep class com.autonavi.**{*;}

# native
-keepclasseswithmembernames class ** {
    native <methods>;
}
-keep class sun.misc.Unsafe { *; }
-keep class com.taobao.** {*;}
-keep class com.alibaba.** {*;}
-keep class com.alipay.** {*;}
-keep class com.ut.** {*;}
-keep class com.ta.** {*;}
-keep class anet.**{*;}
-keep class anetwork.**{*;}
-keep class org.android.spdy.**{*;}
-keep class org.android.agoo.**{*;}
-keep class android.os.**{*;}
-dontwarn com.taobao.**
-dontwarn com.alibaba.**
-dontwarn com.alipay.**
-dontwarn anet.**
-dontwarn org.android.spdy.**
-dontwarn org.android.agoo.**
-dontwarn anetwork.**
-dontwarn com.ut.**
-dontwarn com.ta.**

#  OKHTTP3

#-dontwarn okhttp3.**
#-dontwarn okio.**
#-dontwarn javax.annotation.**
# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# JSR 305 annotations are for embedding nullability information.
-dontwarn javax.annotation.**

# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.
-dontwarn org.codehaus.mojo.animal_sniffer.*

# OkHttp platform used only on JVM and when Conscrypt dependency is available.
-dontwarn okhttp3.internal.platform.ConscryptPlatform

# guava android 23.5-android
-dontwarn afu.org.checkerframework.**
-dontwarn org.checkerframework.**

-dontwarn com.google.errorprone.annotations.*
-dontwarn sun.misc.Unsafe
-dontwarn java.lang.ClassValue

# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic TYPE information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions
# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-dontwarn sun.misc.**
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }

# Prevent proguard from stripping interface information from TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

##---------------End: proguard configuration for Gson  ----------

## Dagger2
-dontwarn com.google.errorprone.annotations.**

## JS
-keep class com.kohler.mirror.domain.entity.JSBean.* { *; }

-keepattributes *Annotation*
-keepattributes *JavascriptInterface*
-keep public class com.kohler.mirror.common.JsBridge{
   public <methods>;
}
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

# 阿里热修复
-keepclassmembers class com.uaes.android.App {
    public <init>();
}
#基线包使用，生成mapping.txt
-printmapping mapping.txt
#生成的mapping.txt在app/build/outputs/mapping/release路径下，移动到/app路径下
#修复后的项目使用，保证混淆结果一致
#-applymapping mapping.txt
#hotfix
-keep class com.taobao.sophix.**{*;}
-keep class com.ta.utdid2.device.**{*;}
#防止inline
-dontoptimize

-keepattributes *Annotation*
-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context);
}

# MpAndroidChart
-keep class com.github.mikephil.charting.** { *; }

# lib自身类
-keep class com.zhny.library.base.** { *; }

-keep class * implements java.io.Serializable { *; }


