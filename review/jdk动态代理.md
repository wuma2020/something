# jdk动态代理

---

## 介绍
```
目的： 通过在程序运行期间，生成需要 真实对象 的 代理类对象，
        然后在生成的代理类对象中添加相应的操作，就可以满足相应的需求.
        如：希望在执行真实对象的某个方法之前或者之后执行某些操作，就可以采用此方法.
            如：spring 的 aop .
            
```

## 流程

```
准备工作：
    1.创建一个接口.
    2.创建接口实现类
    3.创建  InvocationHandler 接口的实现类.
真正创建代理类：
    4.利用Proxy.newProxyInstance（...）方法创建相应的真实对象的代理类. 
        参数为 真实类对象的classload,接口数组，和 InvocationHandler.
        
注意点：
    返回的对象也必需用接口来强转.（因为在生成代理类时，是以接口类型作为返回参数的，
    所以只能用接口或者父接口来强转，用实现类来强转不行，因为java是不允许向下转型的，
    只允许向上转型）

```

## 代码
```
//1.接口
public interface SubjectInterface {
    void sayHello();
    void sayGoodbay();
}

//2.实现类
public class SubjectInterfaceImpl implements SubjectInterface {
    @Override
    public void sayHello() {
        System.out.println(this.getClass().getSimpleName() + " ： say hello.");
    }
    @Override
    public void sayGoodbay() {
        System.out.println(this.getClass().getSimpleName() + " :  say goodbay ."  );
    }
}

//3.InvocationHandler实现类
public class MyInvocationHandlerImpl implements InvocationHandler {
   //object为真实类对象
    private Object object;
    public MyInvocationHandlerImpl(Object object){
        this.object = object;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("使用 realSubject 之前");
        Object invoke = method.invoke(object, args);
        System.out.println("使用 realSubject 之后");
        return "-----";
    }
}


//4.测试类
public class DynamicProxyTest {
    public static void main(String[] args) {
        //真实类实例对象
        SubjectInterfaceImpl s = new SubjectInterfaceImpl();
        //创建 反射处理 接口的实现类对象.
        InvocationHandler handle = new MyInvocationHandlerImpl(s);
        //获取相应的 真实对象的 类加载器以及接口数组
        ClassLoader classLoader = s.getClass().getClassLoader();
        Class<?>[] interfaces = s.getClass().getInterfaces();
        //使用jdk中的Proxy类来动态生成真实对象s的代理类
        SubjectInterface proxyInstance = (SubjectInterface) Proxy.newProxyInstance(classLoader, interfaces, handle);
        //打印被代理对象的相关信息
        System.out.println(proxyInstance.getClass().getName() );
        //通过代理对象来调用被代理对象的方法
        proxyInstance.sayHello();
        System.out.println("-------------------------");
        proxyInstance.sayGoodbay();
    }
}

```

## 代理对象详情
```
//这是网上找的别人生成的代理对象并反编译之后的代码，可以参考.
public final class ProxySubject extends Proxy implements Subject
{
  private static Method m1;
  private static Method m3;
  private static Method m4;
  private static Method m2;
  private static Method m0;
  
  public ProxySubject(InvocationHandler paramInvocationHandler)
  {
    super(paramInvocationHandler);
  }
  
  public final boolean equals(Object paramObject)
  {
    try
    {
      return ((Boolean)this.h.invoke(this, m1, new Object[] { paramObject })).booleanValue();
    }
    catch (Error|RuntimeException localError)
    {
      throw localError;
    }
    catch (Throwable localThrowable)
    {
      throw new UndeclaredThrowableException(localThrowable);
    }
  }
  
  public final String SayGoodBye()
  {
    try
    {
      return (String)this.h.invoke(this, m3, null);
    }
    catch (Error|RuntimeException localError)
    {
      throw localError;
    }
    catch (Throwable localThrowable)
    {
      throw new UndeclaredThrowableException(localThrowable);
    }
  }
  
  public final String SayHello(String paramString)
  {
    try
    {
      return (String)this.h.invoke(this, m4, new Object[] { paramString });
    }
    catch (Error|RuntimeException localError)
    {
      throw localError;
    }
    catch (Throwable localThrowable)
    {
      throw new UndeclaredThrowableException(localThrowable);
    }
  }
  
  public final String toString()
  {
    try
    {
      return (String)this.h.invoke(this, m2, null);
    }
    catch (Error|RuntimeException localError)
    {
      throw localError;
    }
    catch (Throwable localThrowable)
    {
      throw new UndeclaredThrowableException(localThrowable);
    }
  }
  
  public final int hashCode()
  {
    try
    {
      return ((Integer)this.h.invoke(this, m0, null)).intValue();
    }
    catch (Error|RuntimeException localError)
    {
      throw localError;
    }
    catch (Throwable localThrowable)
    {
      throw new UndeclaredThrowableException(localThrowable);
    }
  }
  
  static
  {
    try
    {
      m1 = Class.forName("java.lang.Object").getMethod("equals", new Class[] { Class.forName("java.lang.Object") });
      m3 = Class.forName("jiankunking.Subject").getMethod("SayGoodBye", new Class[0]);
      m4 = Class.forName("jiankunking.Subject").getMethod("SayHello", new Class[] { Class.forName("java.lang.String") });
      m2 = Class.forName("java.lang.Object").getMethod("toString", new Class[0]);
      m0 = Class.forName("java.lang.Object").getMethod("hashCode", new Class[0]);
      return;
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      throw new NoSuchMethodError(localNoSuchMethodException.getMessage());
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      throw new NoClassDefFoundError(localClassNotFoundException.getMessage());
    }
  }
}


```

---

**由代理对象可知，实际上，代理类的每个方法执行都会通过调用自定义InvocationHandler来进行相应的操作，
所以，真正处理逻辑的位置在该invoke()方法中.**

---
```
public final String SayHello(String paramString)
  {
    try
    {
    //实际上，代理类的每个方法执行都会通过调用自定义InvocationHandler来进行相应的操作，所以，真正处理逻辑的位置在该invoke()方法中.
      return (String)this.h.invoke(this, m4, new Object[] { paramString });
    }
    catch (Error|RuntimeException localError)
    {
      throw localError;
    }
    catch (Throwable localThrowable)
    {
      throw new UndeclaredThrowableException(localThrowable);
    }
  }
```

--------------



