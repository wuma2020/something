package cn.kkcoder;

import java.util.ArrayList;
import java.util.List;

/**
 * bean标签中所有property的值
 */
public class PropertyValues {

    //这个是bean标签所有的属性配置信息的list
    private List<PropertyValue> propertyValueList;

    //利用无参构造器创建一个arraylist来存放bean的所有属性信息
    public PropertyValues() {
        this.propertyValueList = new ArrayList<PropertyValue>();
    }

    public List<PropertyValue> getPropertyValueList() {
        return propertyValueList;
    }

    public void setPropertyValueList(List<PropertyValue> propertyValueList) {
        this.propertyValueList = propertyValueList;
    }
}
