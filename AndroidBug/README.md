# Android Bug汇总 #
----------

## CLEARTEXT ##

    //HttpUrlConnection
    java.io.IOException: Cleartext HTTP traffic to **** not permitted
    //OKHttp
    java.net.UnknownServiceException: CLEARTEXT communication ** not permitted by network security policy
    

> Android is committed to keeping users, their devices, and their data safe. One of the ways that we keep data safe is by protecting all data that enters or leaves an Android device with Transport Layer Security (TLS) in transit. As we announced in our Android P developer preview, we're further improving these protections by preventing apps that target Android P from allowing unencrypted connections by default.

> This follows a variety of changes we've made over the years to better protect Android users.To prevent accidental unencrypted connections, we introduced the android:usesCleartextTraffic manifest attribute in Android Marshmallow. In Android Nougat, we extended that attribute by creating the Network Security Config feature, which allows apps to indicate that they do not intend to send network traffic without encryption. In Android Nougat and Oreo, we still allowed cleartext connections.

Android P 默认要求使用加密连接，禁止App使用所有未加密的连接，异常运行Android P 系统的安卓设备无论是接收或者发送流量，未来都不能明码传输，需要使用下一代（Transport Layer Security）传输层安全协议

如果由于某些原因不得不使用明码传输，则需要配置[network security config][1]

在res/xml中新建network_security_config.xml文件

    <?xml version="1.0" encoding="utf-8"?>
    <network-security-config>
        <base-config cleartextTrafficPermitted="true" />
    </network-security-config>
    
在AndroidManifest.xml文件的application中增加如下属性

    <application
    ...
     android:networkSecurityConfig="@xml/network_security_config"
    ...
        />

## 获取编译错误信息 ##

> gradlew compileDebugSource --stacktrace -info
> 
> 在AndroidStudio的terminal中输入上面的命令,重新编译查看gradle的具体报错信息


  [1]: https://developer.android.com/training/articles/security-config