# OpenCVNDKDemo

## 1. 相关路径版本
这是我测试的时候的一些相关的路径。

`Android SDK: D:\Android\sdk`

`Android NDK: D:\Android\ndk\android-ndk-r10e`

`OpenCV4Android: D:\Android\opencv`

> 其中OpenCV的版本是2.4.10

关于编译的版本：

`Android SDK Tools: 24.4`

`Android SDK Platform-tools: 23.0.1`

`Android SDK Build-tools: 23.0.1`

`SDK Platform: Android 6.0 API 23`

## 2. 准备

将Android Studio更新到1.4版本。

> 在Help->About中查看版本

> 在Help->Check for Update中可以更新

创建一个项目，或者导入一个现有的项目。

## 3. 配置OpenCV

1 . 在当前项目根目录下创建一个目录`libraries`，将OpenCV文件夹中的`sdk\java`文件夹复制到`libraries`中，把`java`改名为`opencv`

2 . 在`opencv`文件夹中创建文件`build.gradle`，填入以下内容：

```
apply plugin: 'android-library'

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:1.3.0'
    }
}

android {
    compileSdkVersion 23
    buildToolsVersion "23.0.1"

    defaultConfig {
        minSdkVersion 14
        targetSdkVersion 23
        versionCode 24100
        versionName "2.4.10"
    }

    sourceSets {
        main {
            manifest.srcFile 'AndroidManifest.xml'
            java.srcDirs = ['src']
            resources.srcDirs = ['src']
            res.srcDirs = ['res']
            aidl.srcDirs = ['src']
        }
    }
}
```

其中：

> 1. `classpath 'com.android.tools.build:gradle:1.3.0'`内容应和项目根目录下的`build.gradle`内的对应的保持一致。

> 2. `compileSdkVersion`、`buildToolsVersion`、`minSdkVersion`和`versionName`应该和项目根目录下`app`文件夹里的`build.gradle`内对应的内容保持一致。

> 3. `versionCode`和`versionName`表示`opencv`的版本号，这里的是`2.4.10`的，其他的类推，如`2.4.9`就是`2490`，`2.4.11`就是`24110`。

3 .  在`settings.gradle`文件中添加一句`include ':libraries:opencv'`，然后点击右上角的`Sync`

4 . 在`Android Studio`中的当前项目上右键，点击`Open Module Settings`，在`Modules`一栏选择`app`，然后在右边选择选项卡`Dependencies`，点一下右边的加号，然后选择第三个`Module Dependency`，最后选择`opencv`，保存退出。

5 . 在项目的`/app/src/main/`文件夹下创建文件夹`jniLibs`，然后将`OpenCV`中的`/sdk/native/libs`文件夹下的`armeabi-v7a`文件夹复制过去。

6 . 【可选】目前`Android`上的`OpenCV`只能在`5.0`以下的系统上使用，如果需要添加对`Android 5.0`及以上系统的支持，可以修改`OpenCV`的`initOpenCV`函数，其位置在`/libraries/opencv/src/org/opencv/android/AsyncServiceHelper.java`中，将其修改为：
```java
public static boolean initOpenCV(String Version, final Context AppContext, final LoaderCallbackInterface Callback) {
    AsyncServiceHelper helper = new AsyncServiceHelper(Version, AppContext,
            Callback);
    Intent intent = new Intent("org.opencv.engine.BIND");
    intent.setPackage("org.opencv.engine");
    if (AppContext.bindService(intent, helper.mServiceConnection,
            Context.BIND_AUTO_CREATE)) {
        return true;
    } else {
        AppContext.unbindService(helper.mServiceConnection);
        InstallService(AppContext, Callback);
        return false;
    }
}
```

7 . 到目前为止，`OpenCV`已经配置完毕，demo里有一个示例程序。  

## 4. 配置NDK
1 . 点击`File->Project Structure`，在左边选择`SDK Location`，然后在右边添加NDK的位置，如`D:\Android\ndk\android-ndk-r10e`。

2 . 在`gradle.properties`的最后，添加一句：
```
android.useDeprecatedNdk=true;
```

3 . 在`app`目录下的`build.gradle`中的`defaultConfig`项中添加：
```
ndk {
    moduleName "demo-jni"
}
```
修改之后的`build.gradle`大致为：
```
    ...
    defaultConfig {
        applicationId "com.chenly.opencvndkdemo"
        minSdkVersion 14
        targetSdkVersion 23
        versionCode 1
        versionName "1.0"

        ndk {
            moduleName "demo-jni"
        }
    }
    ...
```
`demo-jni`表示库的名字。

4 . 在项目的`/app/src/main`下添加文件夹`jni`，里面放的就是NDK所用到的`C/C++`代码。

5 . 至此NDK配置完毕，demo里有NDK的使用示例。
