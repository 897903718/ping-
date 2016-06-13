Frame
============
本框架是基于IOC架构实现的Module注入框架
框架由以下几个部分组成：
* 具有Module的注入功能的基础模块，如AbsActivity、AbsFragment、AbsDialog
* 网络请求模块，配合Gson技术，一句话实现网络请求
* 缓存模块，一句代码实现对象的二级缓存

框架的使用
-------
1、修改AndroidManifest.xml文件, 指定Application对象为AbsApplication或者其子类。
```xml
<application
        android:name="com.arialyy.frame.application.AbsApplication">
</application>
```

2、创建Module(非必要)
```java 
/** 在这里面编写你的业务逻辑*/
public class TestModule extends AbsModule{
    ...
    //功能测试
    public void functionTest(){
        HttpUtil httpUtil;
        Map<String, String> pram = new HashMap<>();
        pram.put("username", userName);
        pram.put("password", password);
        httpUtil.post(url, pram, new HttpUtil.Response() {
            @Override
            public void onResponse(String data) {
                L.d(TAG, "result = " + data);
                int result = 100;
                //当获取完数据后，调用以下方法，便能把请求到的数据回调给Activity
                callback(100, data);
            }
        });
    }
}
```

3、创建Activity
```java 
/** 引入Module*/
public class TestActivity extends AbsActivity<TestModule>{
    //设置布局
    @Override
    public int setContentView() {
        return R.layout.test_activity_volley;
    }
    //初始化Module
    @Override
    public TestModule initModule() {
        return new TestModule(this);
    }
    //进行你的操作
     @Override
    protected void init() {
      //调用TestModule的方法
      getModule.functionTest();
    }
    
    //在这里接收Module的回调数据
     @Override
    protected void dataCallback(int result, Object data){
        if(result == 100){
            Log.d(TAG, "module回调数据 = " + data);
        }
    }
}
```
除了AbsActivity，框架还提供了AbsDialog，AbsFragmentDialog，AbsFragment等对象，使用方法和AbsActivity一致

网络请求
-------
```java
/** 实现post网络请求， 如果需要get请求，那么只需要调用httputil.get(...)方法*/
public void netTest(){
    HttpUtil httpUtil;
    Map<String, String> pram = new HashMap<>();
    pram.put("username", userName);
    pram.put("password", password);
    httpUtil.post(url, pram, new HttpUtil.Response() {
        //网络请求成功
        @Override
        public void onResponse(String data) {
            //做你需要的操作
        }
        //网络请求失败
        @Override
        public void onError(Object error) {
            //做你需要的操作
        }
    });
}
```
带缓存的网络请求
-------
```java
/**
 * 带缓存的网络请求，在请求网络成功时，框架会自动把数据缓存到本地，
 * 再次请求时，如果没有网络，框架会自动从缓存读取数据
 */
public void cacheNetTest(){
    HttpUtil httpUtil;
    Map<String, String> pram = new HashMap<>();
    pram.put("username", userName);
    pram.put("password", password);
    //带缓存的网络请求，仅仅只需要调用该方法
    httpUtil.postFromCanche(url, pram, new HttpUtil.Response() {
        //网络请求成功
        @Override
        public void onResponse(String data) {
            //做你需要的操作
        }
        //网络请求失败
        @Override
        public void onError(Object error) {
            //做你需要的操作
        }
    });
}
````

缓存使用
-------
该框架的缓存结构是典型的二级缓存结构，即：内存-磁盘缓存
```java
/**直接缓存任意obj对象*/
   public void cacheTest(){
        String key = "key";
        User user = new User();
        CacheUitl cu = new CacheUtil(this, true);
        //写入缓存
        cu.putObjectCache(User.class, key, user);
        //读取缓存
        User user1 = cu.getObjectCache(User.class, key);
   }
````

注解和ORM模型
-------
虽然框架也实现了注解技术和ORM模型，但是相比于大神们来说，自己实现的注解技术和ORM功能真的不够强大。 
<br  />现在，框架集成了JakeWharton大神[Butterknife](https://github.com/JakeWharton/butterknife)注解技术，该注解使用的是编译时的注解技术，对手机性能几乎不会有影响。
<br  />框架集成了[LitePal](https://github.com/LitePalFramework/LitePal) ORM模型，LitePal可称的上我所使用过的最好的ORM模型。

License
-------

    Copyright 2015 AriaLyy

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
