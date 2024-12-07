package cn.winfxk.lexy.amp.excel.factory;

import cn.winfxk.lexy.amp.Log;
import cn.winfxk.lexy.amp.tool.Tool;

import java.util.HashMap;
import java.util.Map;

public class Keymap {
    /**
     * 订单号所在的位置
     */
    public Integer S1500;
    /**
     * 线别所在的位置
     */
    public Integer line;
    /**
     * 型号所在的位置
     */
    public Integer Model;
    /**
     * 当天时间所在的位置
     */
    public Integer StartTime;
    /**
     * 所选日期内日期所在的位置
     */
    private Map<String, Integer> TimeKey;
    /**
     * 当天的时间
     */
    public String thisTime;
    /**
     * 数量所在位置
     */
    public Integer Count;
    /**
     * 保存订单号、型号等哪一行数据
     */
    public Map<Integer, Object> excelMap;
    /**
     * 临时存储当天时间戳，以便在别处推导出后两天
     */
    public long thisTimemillisecond;

    /**
     * 根据提供的Keymap对象返回序列化的数据
     *
     * @param keymap Keymap对象
     * @return 已序列化的对象
     */
    public static Map<String, Object> getData(Keymap keymap) {
        Map<String, Object> map = new HashMap<>();
        map.put("S1500", keymap.S1500);
        map.put("line", keymap.line);
        map.put("Model", keymap.Model);
        map.put("StartTime", keymap.StartTime);
        map.put("TimeKey", keymap.TimeKey);
        map.put("thisTime", keymap.thisTime);
        map.put("Count", keymap.Count);
        map.put("excelMap", keymap.excelMap);
        map.put("thisTimemillisecond", keymap.thisTimemillisecond);
        return map;
    }

    /**
     * 根据序列化的数据返回Keymap数据
     *
     * @param map 已序列化的数据
     * @return keymap对象
     */
    public static Keymap getKeymap(Map<String, Object> map) {
        Keymap keymap = new Keymap();
        keymap.StartTime = Tool.ObjToInt(map.get("StartTime"));
        keymap.S1500 = Tool.ObjToInt(map.get("S1500"));
        keymap.line = Tool.ObjToInt(map.get("line"));
        keymap.Model = Tool.ObjToInt(map.get("Model"));
        keymap.TimeKey = (Map<String, Integer>) map.get("TimeKey");
        keymap.thisTime = Tool.objToString(map.get("thisTime"));
        keymap.Count = Tool.ObjToInt(map.get("Count"));
        keymap.thisTimemillisecond = Tool.objToLong(map.get("thisTimemillisecond"));
        keymap.excelMap = (Map<Integer, Object>) map.get("excelMap");
        return keymap;
    }

    public Keymap() {

    }

    /**
     * @return 返回一个经过克隆的日期清单，防止在遍历时被更改
     */
    public Map<String, Integer> getTimeKey() {
        return new HashMap<>(TimeKey);
    }

    /**
     * @param key 需要判断的数据
     * @return 判断这个Map内时候包含特定数据
     */
    public boolean containsKey(String key) {
        return TimeKey.containsKey(key);
    }

    /**
     * @return 返回日期大小
     */
    public int getTimeKeySize() {
        return TimeKey.size();
    }

    /**
     * 为保护数据不被强行更改
     *
     * @param k Key
     * @param v 值
     */
    public void setTimeKey(String k, Integer v) {
        if (TimeKey == null) TimeKey = new HashMap<>();
        if (k != null) {
            if (k.contains("(") || k.contains("（"))
                k = k.substring(0, k.indexOf(k.contains("(") ? "(" : "（"));
            k = k.replace(" ", "");
        }
        TimeKey.put(k, v);
    }

    /**
     * @return 判断封装的值是否已经全部被初始化
     */
    public boolean isInit() {
        return S1500 != null && line != null && Model != null && StartTime != null && TimeKey != null && thisTime != null;
    }

    /**
     * 清空所有值
     */
    public synchronized void clear() {
        Log.i("清空计划坐标的所有值");
        S1500 = null;
        line = null;
        Model = null;
        StartTime = null;
        TimeKey = new HashMap<>();
    }
}
