# windows下redis的安装以及redisdesktop的安装连接

标签：redis

---
```
reids官网建议在linux系统下开发，但是windows 用户，又不想装虚拟机的可以参考这个来
进行windows系统下的redis的安装使用
```

------

下载windows 的redis的压缩包
下载地址：https://github.com/MicrosoftArchive/redis/releases

----

1. 解压到  E:\redis 目录下（位子自己放置） 
2. 修改 redis.windows.conf 文件的相关配置
    1. 设置maxmemory 最大内存 <bytes> 里面是以字节为单位
        
            maxmemory <209715200>

   2. 注掉 原先绑定的127.0.0.1的IP 设置自己的IP
    
            #bind 127.0.0.1   设置如下
            bind 192.168.1.112
   3. 启动redis
                    
            cmd命令：  redis-server.exe redis.windows.conf  
            此时在 E:\redis 目录下执行该命令

3. 把redis服务加到windows系统中

    1.  此时在 E:\redis 目录下执行下一条命令
    
            安装命令： redis-server.exe --service-install redis.windows.conf
            卸载命令：redis-server --service-uninstall
    
    2. 此时进入windows的 services.msc 就可以看到redis服务已经加进去
            
4. 下载windows下的redis 图形化管理工具软件 redisDesktopManager

    1. 下载地址 https://redisdesktop.com/download  
        
            接下来就是简单的安装，很简单。

5. 连接redis服务器
    1. 点击 左上角连接到Redis服务器
    2. 名字随便设置 地址写 2.2 中自己设置的 IP ，我这里是：192.168.1.112
    3. 端口就是默认端口：6379 也可以自己在配置文件中修改
    4. 点击 ‘好’ 及可完成连接redis服务器



