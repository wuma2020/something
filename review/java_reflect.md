# java reflect 学习记录


---

> 直接上示例代码

---

## 需要反射获取的类

```
package club.geek.dev.kktest;

public class Person {

    String name;

    String sex;

    int age;

    public Person() {
        System.out.println("空的构造函数");
    }

    private Person(String name){
        this.name = name;
        System.out.println("私有有参构造器");
    }

    public Person(String name, String sex, int age) {
        this.name = name;
        this.sex = sex;
        this.age = age;
        System.out.println("有参构造函数");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    private void testGetDeclaredMethods(){
        System.out.println("testGetDeclaredMethods 被调用");
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", age=" + age +
                '}';
    }
}


```

## 构造器获取方式示例
```
package club.geek.dev.kktest;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 *  class 对象的Constructor方法的测试类
 */
public class ConstructorTest {

    /**
     *  获取class对象的构造方法有四种
     *
     *       //获取参数类型为 parameterTypes 的public修饰的构造函数（唯一一个）
     *      1. public Constructor    getConstructor(Class[] parameterTypes)
     *
     *      //获取所有public修饰的构造函数
     *      2.public Constructor[]    getConstructors()
     *
     *      //获取声明的参数为 parameterTypes 的构造函数 包含 public protected private 修饰的构造函数
     *      3.public Constructor    getDeclaredConstructor(Class[] parameterTypes)
     *
     *      //获取所有声明的构造函数 包含 public protected private 修饰的构造函数
     *      4.public Constructor[]    getDeclaredConstructors()
     *
     *      //如果这个类是某个类的构造函数，getEnclosingConstructor()将调用该内部类的构造函数
     *      5.public Constructor    getEnclosingConstructor()
     *
     */

    public static void main(String[] args) {
        testGetConstuctor();
        testGetConstructors();
        testGetDeclaredConstructor();
        testGetDeclaredConstructors();
    }

    /**
     * 通过反射获取类的所有声明的构造函数
     */
    private static void testGetDeclaredConstructors() {
        try {
            Class person = Class.forName("club.geek.dev.kktest.Person");
            Constructor[] constructors = person.getDeclaredConstructors();
            System.out.println(constructors);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private static void testGetDeclaredConstructor() {
        try {
            Class person = Class.forName("club.geek.dev.kktest.Person");
            Constructor personDeclaredConstructor = person.getDeclaredConstructor(new Class[]{String.class,String.class,int.class
            });
            Object obj1 = personDeclaredConstructor.newInstance("马凯","se",33);
            System.out.println(obj1.toString());

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private static void testGetConstructors() {
        try {
            Class person = Class.forName("club.geek.dev.kktest.Person");
            Constructor constructor = person.getConstructor(new Class[]{});
            Object obj = constructor.newInstance();
            System.out.println(obj);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private static void testGetConstuctor() {
        try {
            Class person = Class.forName("club.geek.dev.kktest.Person");
            Constructor[] constructors = person.getConstructors();

            System.out.println(constructors.length + " length :  tostring" + constructors.toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


}


```

## 方法获取示例
```
package club.geek.dev.kktest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 测试反射中方法对象的获取
 */
public class MethodReflect {

    /**
     *  获取class对象中的method方法也主要分为5种
     *
     *      1.获取class对象的所有public 修饰的方法，得到方法数组对象
     *          getMethods()
     *
 *          2.获取class对象的指定参数的方法对象，也是public 方法，的到该方法对象
     *          getMethod("方法名" , 参数类型);
     *
 *          3.获取class对象的所有声明的方法，包括 修饰符为 private protected public
     *          getDeclaredMethods()
     *
 *          4.获取class对象的对应参数的方法，包含 private protected public 修饰的方法
     *          getDeclaredMethod("方法名",参数类型);
     *
     */


    public static void main(String[] args) {
        testMethods();
        testMethod();
        testDeclaredMethods();
        testDeclaredMethod();
    }

    private static void testDeclaredMethod() {
        try {
            Class aClass = Class.forName("club.geek.dev.kktest.Person");
            Object obj = aClass.newInstance();
            Method testGetDeclaredMethods = aClass.getDeclaredMethod("testGetDeclaredMethods", new Class[]{});

            //这里在访问私有方法时，需要设置访问权限，即调用 setAccessiable(true)即可  否则会报private方法无权访问
            testGetDeclaredMethods.setAccessible(true);
            testGetDeclaredMethods.invoke(obj,new Object[]{});

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    private static void testDeclaredMethods() {
        try {
            Class person = Class.forName("club.geek.dev.kktest.Person");
            Object obj = person.newInstance();
            Method[] declaredMethods = person.getDeclaredMethods();
            System.out.println( "所有方法的 个数是： " + declaredMethods.length);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

    }

    private static void testMethod() {
        try {
            Class person = Class.forName("club.geek.dev.kktest.Person");
            Object obj = person.newInstance();
            Method setName = person.getMethod("setName", String.class);
            //invoke(obj,参数值)  obj为该反射方法所处对象实例，即上面的obj变量
            setName.invoke(obj,new Object[]{"马恺恺"});
            Method getName = person.getMethod("getName", new Class[]{});
            //同上 invoke 方法调用
            Object invoke = getName.invoke(obj,new Object[]{});
            System.out.println(invoke);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

    }

    private static void testMethods() {
        try {
            Class person = Class.forName("club.geek.dev.kktest.Person");
            Method[] methods = person.getMethods();
            System.out.println("person 的 public  methods 的 数量： " + methods.length);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}



```

## 属性获取示例
```
package club.geek.dev.kktest;

import java.lang.reflect.Field;

/**
 * 通过反射获取class对应的对象的属性
 */
public class FieldReflection {

    /**
     * Field 的获取和方法的获取方式一致，如果需要访问被保护的属性，setAccessible( 参数) 参数设置成true即可.
     *      其他示例请看  方法对象 的 获取.
     */

    public static void main(String[] args) {
        testGetField();
    }

    private static void testGetField() {
        try {
            Class aClass = Class.forName("club.geek.dev.kktest.Person");
            Object obj = aClass.newInstance();

            Field name = aClass.getDeclaredField("name");
            name.set(obj,"小马");
            System.out.println(obj);


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }


    }

}

```
