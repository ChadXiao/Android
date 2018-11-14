# Android Bug���� #
----------

## CLEARTEXT ##

    //HttpUrlConnection
    java.io.IOException: Cleartext HTTP traffic to **** not permitted
    //OKHttp
    java.net.UnknownServiceException: CLEARTEXT communication ** not permitted by network security policy
    

> Android is committed to keeping users, their devices, and their data safe. One of the ways that we keep data safe is by protecting all data that enters or leaves an Android device with Transport Layer Security (TLS) in transit. As we announced in our Android P developer preview, we're further improving these protections by preventing apps that target Android P from allowing unencrypted connections by default.

> This follows a variety of changes we've made over the years to better protect Android users.To prevent accidental unencrypted connections, we introduced the android:usesCleartextTraffic manifest attribute in Android Marshmallow. In Android Nougat, we extended that attribute by creating the Network Security Config feature, which allows apps to indicate that they do not intend to send network traffic without encryption. In Android Nougat and Oreo, we still allowed cleartext connections.

Android P Ĭ��Ҫ��ʹ�ü������ӣ���ֹAppʹ������δ���ܵ����ӣ��쳣����Android P ϵͳ�İ�׿�豸�����ǽ��ջ��߷���������δ�����������봫�䣬��Ҫʹ����һ����Transport Layer Security������㰲ȫЭ��

�������ĳЩԭ�򲻵ò�ʹ�����봫�䣬����Ҫ����[network security config][1]

��res/xml���½�network_security_config.xml�ļ�

    <?xml version="1.0" encoding="utf-8"?>
    <network-security-config>
        <base-config cleartextTrafficPermitted="true" />
    </network-security-config>
    
��AndroidManifest.xml�ļ���application��������������

    <application
    ...
     android:networkSecurityConfig="@xml/network_security_config"
    ...
        />


  [1]: https://developer.android.com/training/articles/security-config