# Distributor 

### 介绍 (why)
分布式技术提升了系统的运行效率，但是也产生了新的问题(如分布式锁、全局唯一序列等)

这些问题不能直接用Java原生的技术解决，但在分布式系统中却不可或缺

Distributor基于Redis实现常用的分布式组件，简单、可靠、开箱即用

### 实现功能 (what)
 1. Lock( 基于Redis的分布式锁，支持可重入锁 )
 
 2. 开发中


###  如何使用 (how)
初始化Distributor 
```java
    // 获得实例
    Distributor  distributor  = Distributor.getInstance();

    /* 连接配置
     可以根据实际情况选择不同的配置方式 */
    
    // 配置1
    distributor.initJedisConfig("xxx", 6379, "");
    
    // 配置2
    distributor.initJedisConfig(String host, int port, String auth,JedisPoolConfig jedisPoolConfig)
    
    // 配置3
    distributor.initJedisConfig(JedisPool jedisPool)
```

回收Distributor 
```java
    // 回收资源
    distributor.destory();
```

Lock的使用
```java
    // 获得锁对象, 只指定了锁名
    IDtorLock lock = Distributor.newRedisLock("myLock");
    // 还可以手动指定锁的过期时间
    IDtorLock lock = Distributor.newRedisLock("myLock", 2000);
    
    /* 加锁方式有3种 */
    // 1. lock()方法, 直到获取锁成功为止
    String lockId = lock.lock(); 
    // 2. tryLock()方法，尝试加锁，2s内未成功返回
    String lockId = lock.tryLock(); 
    // 3. tryLock(long tryLockTime, TimeUnit unit)，尝试加锁，指定尝试时间
    String lockId = lock.tryLock(2000，TimeUnit.MILLISECONDS);
    
    // ----> 业务处理
    
    // 最后释放锁
    lock.unLock(lockId);
```

### 性能测试 (test)
测试代码可以在测试类中看到

Lock：开启20个线程，每个线程获取10次锁


### 友情链接 (friends)
 + [水不要鱼 & GitHub](https://github.com/FishGoddess)

 + [Mackyhuang & GitHub](https://github.com/Mackyhuang)

 + [MaDao & GitHub](https://github.com/Madaovo)