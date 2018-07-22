package cn.kkcoder;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * IOC测试类
 */
public class TestMyIOC {

    @Test
    public void testIOC(){
        //1.创建bean工厂
        BeanFactory beanFactory = new BeanFactory();

        //2.注册bean
        BeanDefinition beanDefinition = new BeanDefinition();
        beanDefinition.setBeanClassName("cn.kkcoder.Person");
        BeanDefinitionHolder bh = new BeanDefinitionHolder("person",beanDefinition);

        //2.1设置beanDefinition属性
        PropertyValues propertyValues = new PropertyValues();
        List list= new ArrayList<PropertyValue>();
        PropertyValue propertyValue = new PropertyValue("name","ObjectValue");
        list.add(propertyValue);
        propertyValues.setPropertyValueList(list);
        beanDefinition.setPropertyValues(propertyValues);

        //2.2向beanFactory中注册该BeanDefinition
        beanFactory.registerBeanDefinition(bh.getBeanName(),bh.getBeanDefinition());

        //3.获取bean实例
        Person person = (Person)beanFactory.getBean("person");
        System.out.println(person.toString());


    }
}
