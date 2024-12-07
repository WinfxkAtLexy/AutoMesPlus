package cn.winfxk.lexy.amp.view.setting.view.setting;

import cn.winfxk.lexy.amp.tool.Tool;

import java.util.Map;

public class Entry implements Map.Entry<String, Object> {
    private final String key;
    private Object value;

    public Entry(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public Object setValue(Object value) {
        this.value = value;
        return value;
    }

    public String getValueToString() {
        return Tool.objToString(value, null);
    }
}
