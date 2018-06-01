# Blade之使用提示

标签： Blade使用

---

# 错误: 找不到或无法加载主类 

> 解决方案 ：  执行 mvn clean compile  

---

# 写了路由，返回指定html，却返回404
---
> 解决方案 ： 1. 路径是否正确

    正确路径：
    .
    ├── pom.xml
    └── src
      ├── main
      │  ├── java
      │  │   └── com
      │  │   └─── example
      │  │      ├── Application.java
      │  │      ├── config
      │  │      ├── controller
      │  │      ├── hooks
      │  │      ├── model
      │  │      └── service
      │  └── resources
      │      ├── app.properties
      │      ├── static
      │      │   ├── css
      │      │   │ └─ style.css
      │      └── templates
      │          └─ index.html
      └── test
          └── java

---

>  2 . 是否执行 mvn clean compile 
    
    eclipse中：右击项目 -> Run as -> Maven Buile ... -> 在Goals选项框中输入 clean compile  -> 点击运行  .  编译结束后，点击 Run As Java Application 即可.
    
---

# 编译出错，显示请使用jdk8

> 解决方案： 在 pom.xml 文件中，加入指定jdk编译版本的代码.

    <build>
      <plugins>
        <plugin>
          <groupId>org.apache.maven.plugins</groupId>
          <artifactId>maven-compiler-plugin</artifactId>
          <configuration>
            <source>1.8</source>
            <target>1.8</target>
          </configuration>
        </plugin>
      </plugins>
    </build>
    
---    