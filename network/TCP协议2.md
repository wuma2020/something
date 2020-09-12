
## tcp抓包工具

> `tcpdump` 和 `wireshark`

1. tcpdump 仅仅支持命令格式使用，常用在linux服务器中抓取保存分析网络包。
2. wireshark提供了可视化的分析网络包的图形界面，可以分析tcpdump保存下来的数据包。分析更加的直观，还有很多其他功能。

```
# tcpdump 抓包常用命令

# -i eth1 指抓取eth1网口的数据包
# http 指 抓取http协议的数据包
# host 指抓取对应主机ip 的数据包
tcpdump -i eth1 http and host 172.x.x.x 

```

```
wireshark 使用

```

## tcp 3次握手和4次挥手

![image](https://mmbiz.qpic.cn/mmbiz_png/J0g14CUwaZctmf3ObkESj41ayTbgy9q4qY22TbaYibfEM8jSqdribXBDHgkJQNo7dsj5S9l2VvL22N0w7ckI9tgQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

1. 在客户端第一次向服务器发送syn数据包时，会生成一个随机数，作为seq的值；服务器端在给客户端发送 syn/ack包时，也会生成一个随机数，作为seq的值，并且ack的值为客户端发来的seq +1;随后客户端就会发送一个确认包，ack为服务器发送的随机数seq的值 +1；服务器收到包后，至此，双方就都认为连接建立成功，双方都会进入established状态。一共完成了三次数据包的交换，称为3次握手。

2. 4次挥手：客户端给服务器端恭送fin报文，进入FIN_WAIT_1状态；服务器收到后，返回ack报文，服务器进入CLOSE_EAIT状态；客户端收到确认报文后，进入FIN_WAIT_2状态；等待服务器发送FIN报文，此时服务器进入LAST_ACK状态；客户端收到后，进入TIME_TAIT状态，并发送确认报文，等待2MSL时间后，进入close状态；服务器收到确认报文，进入CLOSE状态。

    - 当客户端发送FIN报文时，服务器端也没有数据需要输出，可以吧FIN报文和ACK确认报文放在同一个包中返回，此时抓包，tcp的挥手为3次数据包的发送。
    

## tcp的重传

```
网络包进入主机的顺序 网线  -> 网卡 -> tcpdump -> iptables
网络包离开主机的顺序 iptables -> tcpdump -> 网卡 -> 网线
```

- tcp 第一次握手 SYN 包丢失，会发生什么？
- tcp 第二次握手 SYN 、ACK 包丢失，会发生什么？
- tcp 第三次握手 ACK 包丢失，会发生什么？

1. tcp SYN数据包丢失，会进行重传，每次重传的间隔时间RTO是不同的，是翻倍上涨的，而且有个重传次数的限制，在 ` cat /proc/sys/net/ipv4/tcp_syn_retries`中可以查看。
 
2. SYN/ACK 包和SYN数据包一样，也有个重传次数的参数设置，为`/proc/sys/net/ipv4/tcp_synack_retries`

3. 第三次握手的ack包丢失，此时客户端为established状态，服务器为syn_recv状态。这种状态，在服务器端，SYS_RECV状态持续不到1分钟就会关闭连接，而客户端的状态在数据包传输，尝试一定次数后，一直失败，则会关闭。即建立连接后的数据包最大传输次数是由参数`/proc/sys/net/ipv4/tcp_retries2`确定的，默认15

    - 如果客户端没有进行数据的传输，客户端的ESTABLISHED状态持续多久呢？
        
        ```
        tcp的保活机制：在一定时间段内，tcp的保活机制每隔一段时间，发送一个嗅探保温，如果连续几个报文都没有响应，则认为该tcp连接死亡.
        
        net.ipv4.tcp_keepalive_time=7200 该时间段内无连接活动，触发保活机制
        net.ipv4.tcp_keepalive_intvl=75  每次检测时间间隔
        net.ipv4.tcp_keepalive_probes=9  尝试的次数
        
        ```


## tcp的快速连接

客户端向服务器发起HTTP get请求时，一个完整的交互过程需要2.5个RTT（数据包的往返时间+应用程序处理数据包的时间）的时延。

由于第三次握手是可以携带数据的，所以这样发起http get请求时，需要2个RTT时延。所有的HTTP请求都是一样，需要2个RTT时间。


在linux 3.7内核版本中，提供了TCP FAST OPEN 功能，可以减少TCP建立连接的时延。

    - 在开启快速连接的情况下，客户端第一次建立连接时，先发送syn包，服务端会在返回的syn/ack包上添加cookie信息，然后客户端收到后，继续返回ack，并携带http请求数据，然后服务器返回数据。消耗2 RTT时间。后面的第二次http请求时，在syn包中，直接发送http 请求数据，和cookie信息，服务器验证cookie后，直接返回syn、ack和数据信息。消耗1 RTT时延。
    - 可以通过参数`net.ipv4.tcp_fastopn`来设置fastopen功能
    
![image](https://mmbiz.qpic.cn/mmbiz_png/J0g14CUwaZctmf3ObkESj41ayTbgy9q4jPl9JahGicerp3A4tdQdQffuHXfRIovRK0UC3LmoicISkF3TBrXq75LQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)
    
    
    
    
    



## tcp 的重复确认和快速重传

> 当接收方收到乱序的数据包时，并且没有收到某个包时，会发送重复的ack，以告知发送方要重发该数据包，当发送方连续收到3个重复的ack包时，就会触发快速重传，立即发送丢失的数据。


![image](https://mmbiz.qpic.cn/mmbiz_png/J0g14CUwaZctmf3ObkESj41ayTbgy9q4VGzYwgFSHBts9GC74Y92B4QzribnTVMrQrAAvZRMRyE1iayC1Gp4eebQ/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

- 快速重传机制需要在tcp三尺握手时协商，开启选择重传SACK机制。如果不开启选择重传机制，每次收到ack包就要把ack包后续的所有包重传一次。可以通过`net.ipv4.tcp_sack`参数打开。linux 2.4后，默认开启。


 ## tcp流量控制
 
 tcp为了防止发送端一直发送数据，导致接收方缓冲区填满，所以就有了滑动窗口机制，他可以控制发送方的要发送的数据量，达到流量控制的目的。
 
 在tcp的三次握手中，就已经协商好双方的接收窗口的大小，接受窗口是由接受方指定，告诉对方自己的tcp缓冲区的大小，应用程序读取了自己缓冲区的数据，数据就会在缓冲区中删除，在发送ack时，就会更新自己的窗口大小，如果应用程序没有读取数据，数据就会一直留在缓冲区。最后会导致缓冲区满了，导致滑动窗口大小为0，发送方无法发送消息，就会触发0窗口通知和窗口探测。
 
 零窗口通知和窗口探测：当出现0窗口通知时，发送方就会定时发送窗口大小探测报文，当接收端一直是0窗口时，窗口探测报文的时间间隔会倍增，每次探测报文都会马上收到一个0窗口的通知。
 
 接受窗口大小实际值 =  tcp 窗口大小值 * tcp 窗口大小缩放因子； 这两个值在tcp握手时会确定，如果抓包没有抓到tcp三次握手，就无法计算出真正的接受窗口的大小了。
 
 
 发送窗口实际值 = 拥塞窗口 和 接受窗口的 最小值
 
 发送窗口和MSS(最大分段大小)的关系：发送窗口决定一下能发送多少字节数据，而MSS决定这些字节分多少个包发送。
 
 tcp有累计确认机制，即当收到多个数据包时，只需要返回最后一个数据包对应的ack报文即可
 
 
 ## tcp延迟确认和Nagle算法
 
 应用场景：在tcp报文携带的数据量非常小时，几个字节，而tcp和ip协议的报文头各占20字节（不考虑附加特殊属性时），那么整个报文的利用率就会很低。于是就有了两种处理小包的策略：延迟确认和Nagle算法。
 
 
 Nagle算法策略发生在发送方，而延迟确认发生在接收方。两者不要同时开启，否则可能会造成更大的延迟。
 
 
 Nagle算法策略：
    
    - 没有 已经发送但未确认 报文时，立即发送数据。
    - 存在 未确认报文时，直到 没有已发送但未确认 报文 或 数据包长度达到MSS大小时，再发送数据。
    
Nagle算法第一个报文一定是一个小的报文。


![image](https://mmbiz.qpic.cn/mmbiz_png/J0g14CUwaZctmf3ObkESj41ayTbgy9q4vBYd8ovMrvDnoel9KAmEPc11OyDVU204B11luT4mSsIzVRvaMBJjMg/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)


> telnet 或 ssh这种交互性比较强的程序，需要关闭Nagle算法.


延迟确认策略：
    
    - 应用场景：返回没有数据的ack报文时，效率也很低。
    - 策略：
        
        - 当有相应数据发送时，ack会和相应数据一起立刻发送
        - 当没有相应数据发送时，ack会延迟一段时间，等待是否有响应数据一起发送
        - 如果在等待延迟发送ack时，发送方第二个数据包又到达了，这是就会立刻返回ack数据包
    - 可以通过socket设置`TCP_QUICKACK`是否关闭这个算法
    

延迟确认和Nagle算法策略一起使用会导致延迟很大，如图：

![image](https://mmbiz.qpic.cn/mmbiz_png/J0g14CUwaZctmf3ObkESj41ayTbgy9q4A1fkc0Glbtxs8mnk7pWIELckxG32TlZrjhHws66DWMic1L3gBCCZVTw/640?wx_fmt=png&tp=webp&wxfrom=5&wx_lazy=1&wx_co=1)

    










 





