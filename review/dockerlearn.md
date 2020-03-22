# docker 学习

## docker的好处，用途



## docker的使用

### docker的安装

1. 卸载旧版本docker

    ```
yum remove docker \
                  docker-client \
                  docker-client-latest \
                  docker-common \
                  docker-latest \
                  docker-latest-logrotate \
                  docker-logrotate \
                  docker-engine
    ```

2. 安装 docker ec
    - 安装所需软件包
    
        ```
        yum install -y yum-utils \
  device-mapper-persistent-data \
  lvm2
        # 设置docker仓库
  yum-config-manager \
    --add-repo \
    https://download.docker.com/linux/centos/docker-ce.repo
        ```
    
    - 安装docker ec
    
    ```
    yum install docker-ce docker-ce-cli containerd.io
    ```
   
3. 启动docker 

```
systemctl start docker
```

### docker hello world

1. 运行一个容器

    ```
    # run 指运行一个容器 ；ubuntu:15.10只一个镜像，不写tag，会只用最新的镜像，docker会先从本地查找镜像，不存在会从docker仓库下载；/bin/echo ss指在启动的容器中执行该命令
    docker run ubuntu:15.10 /bin/echo
    ```


2. 进入容器交互

    ```
    # -i 对容器的标准输入进行交互，键盘输入； -t 在新容器中，指定一个伪终端；
    docker run -i -t ubuntu 
    ```

3. 后台模式运行



    ```
    # -d指后台模式运行
    docker run -d ubuntu [命令]
    ```

4. 显示所有容器

    ```
    docker pa -a
    
    CONTAILNER ID容器id；IMAGE使用的镜像；COMMAND启动时运行的命令；CREATED创建时间；STATUS容器状态；PORTS容器端口信息;NAMES自动分配的容器名称
    [root@localhost ~]# docker ps -a
    CONTAINER ID        IMAGE               COMMAND             CREATED             STATUS                            PORTS               NAMES
    66bc65587b8e        ubuntu              "/bin/bash"         5 minutes ago       Exited (127) About a minute ago                       nervous_shockley
    3c3bb9b89e95        training/webapp     "python app.py"     2 weeks ago         Exited (137) 2 weeks ago                              fervent_aryabhata
    53844198b7b7        training/webapp     "python aa.py"      2 weeks ago         Exited (2) 2 weeks ago                                lucid_ritchie
    3dfacba15872        ubuntu              "509ea88912ae"      2 weeks ago         Created                                               keen_wing
    509ea88912ae        ubuntu              "/bin/bash"         2 weeks ago         Exited (130) 2 weeks ago                              busy_mccarthy
    ```

5. 停止容器

    ```
    # docker stop 容器id
    docker stop a0250c8aaf24 
    ```

### 容器

1. 获取镜像

    ```
    docker pull ubuntu 
    ```

2. 进入容器

    ```
    docker exec 容器id
    ```

3. 删除容器

    ```
    docker rm -f 容器id
    ```

4. 查看docker底层信息

    ```
    docker inspect a0250c8aaf24
    ```

### docker 镜像使用

1. 镜像列表

    ```
    docker images
    
    #REPOSITORY镜像的仓库源；TAG镜像标签；IMAGE ID镜像ID;CREATED创建时间;SIZE大小
    [root@localhost ~]# docker images
    REPOSITORY          TAG                 IMAGE ID            CREATED             SIZE
    httpd               latest              c5a012f9cf45        3 weeks ago         165MB
    ubuntu              latest              72300a873c2c        4 weeks ago         64.2MB
    ubuntu              <none>              ccc6e87d482b        2 months ago        64.2MB
    mysql               latest              3a5e53f63281        2 months ago        465MB
    
    ```


2. 查找镜像


    ```
    docker search httpd
    
    # NAME镜像仓库源的名称；DESCRIPTION镜像的描述；OFFICIAL是否为docker官方发布；stars 点赞数；AUTOMATED自动构建
    [root@localhost ~]# docker search redis
    NAME                             DESCRIPTION                                     STARS               OFFICIAL            AUTOMATED
    redis                            Redis is an open source key-value store that…   7940                [OK]                
    bitnami/redis                    Bitnami Redis Docker Image                      138                                     [OK]
    sameersbn/redis                                                                  79                                      [OK]
    grokzen/redis-cluster            Redis cluster 3.0, 3.2, 4.0 & 5.0               64                                      
    rediscommander/redis-commander   Alpine image for redis-commander - Redis man…   35                                      [OK]
    
    ```
    
3. 删除镜像

    ```
    docker rmi ubuntu
    ```

4. 构建镜像Dockerfile

待补



### 配置端口和网桥

待补

### docker compose

待补





