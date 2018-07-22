package cn.kkcoder;


/**
 * xml文件中bean标签对应的java类
 *      就是一个bean的java实例对象
 */
public class BeanDefinition {

    /*bean标签对应的class的类的class对象*/
    private Class beanClass;
    /*bean标签class的全路径*/
    private String beanClassName;
    /*bean标签的property的值*/
    private PropertyValues propertyValues;

    public BeanDefinition() {
    }

//---------------set and get 方法-------------------------


    public Class getBeanClass() {
        return beanClass;
    }


    public String getBeanClassName() {
        return beanClassName;
    }

    /*在setBeanName的时候，根据beanNaem设置beanclass属性的值*/
    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
        try {
            //设置beanClass的值
            this.beanClass = Class.forName(beanClassName);
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }

    }

    public PropertyValues getPropertyValues() {
        return propertyValues;
    }

    public void setPropertyValues(PropertyValues propertyValues) {
        this.propertyValues = propertyValues;
    }
}
