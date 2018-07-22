package cn.kkcoder;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * bean工厂类，用于存放所有的bean标签对应的beanDefinition对象
 *          存放在一个map中 key 为 bean 的id，value 为 beanDefinition 对象
 */
public class BeanFactory {

    //这个map是已经实例了bean
    //这个map用于存放bean对应的实例对象，value是已经利用beanname反射获取到的class对象的实例化对象
    private Map<String,Object> beanInstanceMap = new ConcurrentHashMap<String, Object>(64);

    //这个map是用于注册bena，并没有实例化.
    //这个map用于存放bean对应的java类beanDefinition对象实例.
    //beanDefinition是把bean标签中的信息提取出来，并封装到beanDefinition中。这只是提取得信息，并不是该bean对应的class的实例对象
    //而beanInstanceMap中存放的是根据beanDefinition来创建的相对应的实例对象. getBean方法获取bean的实例对象就是该map中的value值
    private Map<String,BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>();


    //-----------------这里只提供两个接口供外界访问---2.getBean()---1.registerBeanDefinition()------------------

    /**
     * 向beanFactory中注册bean的信息，此时并没有实例化.
     * @param beanName
     * @param beanDefinition bean标签对应的信息封装的类
     */
    public void registerBeanDefinition(String beanName,BeanDefinition beanDefinition){
        beanDefinitionMap.put(beanName,beanDefinition);
    }


    /**
     *  beanFactory 的核心方法 getBean();
     * @param beanName
     * @return
     */
    public Object getBean(String beanName){
        Object bean = beanInstanceMap.get(beanName);
        if(bean == null){
            try {
                //beanFactory中并没有该bean对象实例，所以根据beanDefinition创建该bean实例对象
                bean = doCreateBean(beanName, beanDefinitionMap.get(beanName));
            }catch (Exception e){
                e.printStackTrace();
            }
            //创建成功后，再向beanInstanceMap中添加
            beanInstanceMap.put(beanName, bean);
        }
     return bean;
    }

    /**
     * 根据beanName 和 benaDefinition 来创建 bean 的实例对象
     *      1.创建bean对象实例
     *      2.并利用反射设置成员属性的值
     * @param beanName
     * @param beanDefinition beanName对应的bean 信息的封装类 beanDifinition
     * @return bean实例对象
     */
    private Object doCreateBean(String beanName, BeanDefinition beanDefinition) {

        Object bean = null;
        try {
            // 错误示例：  bean = Class.forName(beanDefinition.getBeanClassName()).newInstance();
            // 这样会相当于重新new了一个实例对象，并不是相应的BeadDefinition对象实例了

            // 1.获取bean实例
            bean = beanDefinition.getBeanClass().newInstance();
            // 2.设置bean实例属性的值
            setBeanProperties(bean,beanDefinition);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return bean;
    }

    /**
     *  根据反射设置bean实例的值
     */
    private void setBeanProperties(Object bean, BeanDefinition beanDefinition) {
        PropertyValues propertyValues = beanDefinition.getPropertyValues();
        if(propertyValues != null){
            List<PropertyValue> list = propertyValues.getPropertyValueList();
            if(list != null && list.size() > 0){
                for(PropertyValue p : list){
                    try {
                        //1.获取 bean 实例的 属性名为 p.getName() 的属性对象
                        Field declaredField = bean.getClass().getDeclaredField(p.getName());
                        //2.设置属性的访问权限为true
                        if(!declaredField.isAccessible()){
                            declaredField.setAccessible(true);
                        }
                        //3.把该bean的该属性的值设置为 对应的值
                        declaredField.set(bean,p.getValue());
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

}
