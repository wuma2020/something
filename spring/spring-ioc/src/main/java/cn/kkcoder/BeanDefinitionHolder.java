package cn.kkcoder;

/**
 * BeanDifinition的吃力方法
 */
public class BeanDefinitionHolder {
    private BeanDefinition beanDefinition;
    private String beanName;

    public BeanDefinitionHolder() {
    }

    public BeanDefinitionHolder(String beanName,BeanDefinition beanDefinition) {
        this.beanDefinition = beanDefinition;
        this.beanName = beanName;
    }

    public BeanDefinition getBeanDefinition() {
        return beanDefinition;
    }

    public void setBeanDefinition(BeanDefinition beanDefinition) {
        this.beanDefinition = beanDefinition;
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }
}
