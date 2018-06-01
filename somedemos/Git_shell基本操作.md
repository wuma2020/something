# Git shell 基本操作

标签： Gitshell

---

# 简单了解一下

> 首先看一下图.

![此处输入图片的描述][1]

    1.Remote : 远程仓库.就是github主页上显示的仓库.
    2.Repository : 本地仓库.先把文件放到本地仓库，再可以放到远程仓库.
    3.workspace : 工作区.就是本地的文件夹.(.git文件夹同级目录的地方，就是工作区.)



# 下面开始具体的提交文件到远程仓库的教程

> 1. git init  (初始化workspace,即，在该路径生成一个 .git 文件夹，相当于建立的一个 workspace 。)
示例：![此处输入图片的描述][2]

---        
    
> 2.git add 文件（将需要上传的文件放到 该目录位置，然后 执行add）
示例：![此处输入图片的描述][3]
 
---   

> 3 . git commit -m "提交的文件的介绍信息" 
    示例：
![此处输入图片的描述][4]

---   

>   4 . git remote add origin <server>(server 是你的远程仓库的地址，[在仓库的右上角][5]可以看到。把本地仓库和远程仓库关联)
    示例：![此处输入图片的描述][6]
    
    
--- 

>  5 . git push origin master(提交到远程仓库的 master 分支，也就是主分支)
    示例：![此处输入图片的描述][7]
    
    
---
 
 # 演示git clone
 
 > 一般情况下，我是使用clone来把远程仓库克隆到本地，这样的话，该本地仓库会直接有 .git文件，以及，不需要再remote add .也就是说， 不需要执行 步骤 1 和 4.其他操作相同.
    　　　演示：
　　　![此处输入图片的描述][8]
 
 
    
# 关于 git 的 links    

> 上面的其实基本够用，下面这些事详细的介绍.

1. [http://www.bootcss.com/p/git-guide/][9]
2. [阮一峰][10]
2. [https://marklodato.github.io/visual-git-guide/index-zh-cn.html][11]
    
    


  [1]: http://www.ruanyifeng.com/blogimg/asset/2014/bg2014061202.jpg
  [2]: https://raw.githubusercontent.com/static-mkk/learn_note/master/pic/init.png
  [3]: https://raw.githubusercontent.com/static-mkk/learn_note/master/pic/add.png
  [4]: https://raw.githubusercontent.com/static-mkk/learn_note/master/pic/commit.png
  [5]: https://raw.githubusercontent.com/static-mkk/learn_note/master/pic/remote.png
  [6]: https://raw.githubusercontent.com/static-mkk/learn_note/master/pic/remote2.png
  [7]: https://raw.githubusercontent.com/static-mkk/learn_note/master/pic/push.png
  [8]: https://raw.githubusercontent.com/static-mkk/learn_note/master/pic/clone.png
  [9]: http://www.bootcss.com/p/git-guide/
  [10]: http://www.ruanyifeng.com/blog/2014/06/git_remote.html
  [11]: https://marklodato.github.io/visual-git-guide/index-zh-cn.html