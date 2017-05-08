package com.nowcoder.model;

import org.apache.commons.collections.map.HashedMap;

import java.util.HashMap;
import java.util.Map;

/**
 * ViewObject是一个盒子类型, 用来装往前端放的各种对象
 * 典型例子: 页面上的comment不仅需要一个comment对象, 也需要一个user对象
 * 那么用一个vo对象来装这两个对象, 就是一个非常方便的选择;
 * Created by Chen on 04/05/2017.
 */
public class ViewObject {
    private Map<String, Object> objs = new HashMap<String, Object>();

    public void set(String key, Object value) {
        objs.put(key, value);
    }

    public Object get(String key) {
        return objs.get(key);
    }
}
