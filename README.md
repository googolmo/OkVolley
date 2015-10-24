OkVolley
========

A volley library used okhttp

[![Build Status](https://drone.io/github.com/googolmo/OkVolley/status.png)](https://drone.io/github.com/googolmo/OkVolley/latest)

[![Build Status](https://travis-ci.org/googolmo/OkVolley.svg?branch=master)](https://travis-ci.org/googolmo/OkVolley)

[ ![Download](https://api.bintray.com/packages/googolmo/maven/okvolley/images/download.svg) ](https://bintray.com/googolmo/maven/okvolley/_latestVersion)

fork 自 [OKVolley](https://github.com/googolmo/OkVolley)

 1. 增加了 Gson 对 解析 Json 进行解耦；
 2. 增加了 Demo 里 Retrofit 请求与 OKVolley 的对比；
 3. Retrofit 请求的实现用了两种方式：Callback 和 RxJava 。

感谢 [googolmo](https://github.com/googolmo)

### How to use

```groovy
compile 'im.amomo.volley:okvolley:1.1.5@aar'
```

### Proguard

```
-dontwarn im.amomo.volley.**
-dontwarn com.android.volley.**
```

