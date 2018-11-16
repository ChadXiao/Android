# Kotlin使用注解
----------
kotlin无法使用（annotationProcessor ）java的注解处理工具，需要使用kotlin注解工具（Kotlin Annotation processing tool，kapt）替代，kapt也能处理java文件，所以不需要保留annotationProcessor

使用方式：

 1. 在模块build.gradle中添加

    apply plugin: 'kotlin-kapt'
    
 2. 用kapt替换annotationProcessor