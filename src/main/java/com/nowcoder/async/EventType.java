package com.nowcoder.async;

/**
 * 枚举类
 * 注意int value的使用
 * Created by Chen on 10/05/2017.
 */
public enum EventType {
    LIKE(0),
    COMMENT(1),
    LOGIN(2),
    MAIL(3);

    private int value;

    EventType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
