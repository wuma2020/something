package cn.kkcoder;

/**
 * Bean标签中的每一个property属性对应的类
 */
public class PropertyValue {
    //属性名
    private String name;
    //属性值
    private Object value;

    public PropertyValue() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public PropertyValue(String name, Object value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String toString() {
        return "PropertyValue{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}
