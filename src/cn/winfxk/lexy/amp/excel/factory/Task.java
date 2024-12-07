package cn.winfxk.lexy.amp.excel.factory;

import cn.winfxk.lexy.amp.Log;
import cn.winfxk.lexy.amp.Main;
import cn.winfxk.lexy.amp.excel.ExcelFactory;
import cn.winfxk.lexy.amp.tool.Tool;

import java.text.SimpleDateFormat;
import java.util.*;

public class Task {
    /**
     * 吧滚动计划内所有日期都保存起来方便使用
     */
    public static final LinkedHashMap<Integer, Long> allTaskTimes = new LinkedHashMap<>();
    /**
     * 用来保存订单号、定好等数据在表格中所在的位置
     */
    public static final Keymap Keymap = new Keymap();
    /**
     * 型号
     */
    public final String Model;
    /**
     * 订单号
     */
    public final String S1500;
    /**
     * 生产线
     */
    public final String Line;
    /**
     * 订单数量
     */
    public final String Count;
    /**
     * 计划表的原始数据
     */
    public final Map<Integer, Object> map;

    /**
     * 滚动计划所属的Keymap对象
     */
    private final Keymap meKeymap;

    /**
     * @return 判断这个计划是否加载正常，是否是正常计划
     */
    public boolean isLoad() {
        return Model != null && !Model.isEmpty() && S1500 != null && !S1500.isEmpty();
    }

    /**
     * 根据提供的Keymap对象来解析滚动计划表
     *
     * @param map    滚动计划表
     * @param keymap keymap对象
     */
    public Task(Map<Integer, Object> map, Keymap keymap) {
        this.map = map;
        this.meKeymap = keymap;
        this.S1500 = Tool.objToString(map.get(meKeymap.S1500));
        this.Line = Tool.objToString(map.get(meKeymap.line));
        this.Model = Tool.objToString(map.get(meKeymap.Model));
        this.Count = Tool.objToString(map.get(meKeymap.Count));
    }

    /**
     * 使用默认的Keymap对象解析滚动计划
     *
     * @param map 滚动计划表
     */
    public Task(Map<Integer, Object> map) {
        this.map = map;
        this.S1500 = Tool.objToString(map.get(Keymap.S1500));
        this.Line = Tool.objToString(map.get(Keymap.line));
        this.Model = Tool.objToString(map.get(Keymap.Model));
        this.Count = Tool.objToString(map.get(Keymap.Count));
        this.meKeymap = Keymap;
    }

    @Override
    public String toString() {
        return "class Task:{Model: " + Model + ", S1500: " + S1500 + ", Line: " + Line + ", Count: " + Count + ", mapSize: " + map.size() + "}\n";
    }

    public static void reloadKey(List<Object> list) {
        if (list == null || list.isEmpty()) {
            Log.e("未获取到任何计划！请检查检查计划文件是否被加密或滚动计划Excel文件版本是否合法");
            return;
        }
        Log.i("正在定位各数据所在位置..");
        String objString, Value;
        Map<Integer, Object> map = null;
        Keymap.clear();
        Date date;
        for (Object obj : list) {
            objString = Tool.objToString(obj, "");
            if (objString.contains("订单") && objString.contains("型号") && objString.contains("业务") && objString.contains("客户")) {
                map = (Map<Integer, Object>) obj;
                break;
            }
        }
        Log.i("已定位滚动计划标题，正在解析格式....");
        if (map == null || map.isEmpty()) {
            Log.e("无法识别计划格式！请检查滚动计划表是否发生重大变化！");
            return;
        }
        //开始获取最新一天的计划所在的位置
        boolean dateIsOK = false;
        String[] Times = new String[ExcelFactory.DateFormat.length];
        long thisMillisecond = System.currentTimeMillis();
        int ErrorCount = 0;
        String string;
        while (Main.runing) {
            date = new Date(thisMillisecond);
            for (int i = 0; i < Times.length; i++)
                Times[i] = new SimpleDateFormat(ExcelFactory.DateFormat[i]).format(date);
            for (Map.Entry<Integer, Object> entry : map.entrySet()) {
                Value = Tool.objToString(entry.getValue(), null);
                if (Value == null || Value.isEmpty()) continue;
                for (String time : Times) {
                    string = time;
                    if (Value.equals(string) || Value.startsWith(string + "(") || Value.startsWith(string + "（")) {
                        dateIsOK = true;
                        Keymap.thisTimemillisecond = thisMillisecond;
                        break;
                    }
                }
            }
            if (dateIsOK) break;
            Times = new String[ExcelFactory.DateFormat.length];
            thisMillisecond += 86400000;
            if (ErrorCount++ > 10) {
                Log.e("无法获取计划日期！请检查系统时间是否正常或检查滚动计划格式是否发生重大变化。");
                return;
            }
        }
        Keymap.excelMap = map;
        //开始识别计划关键词所在的位置
        List<Integer> Keylist = new ArrayList<>(map.keySet());
        for (Map.Entry<Integer, Object> entry : map.entrySet()) {
            Value = Tool.objToString(entry.getValue());
            if (Value == null || Value.isEmpty()) continue;
            if (equal(Value, "线别")) Keymap.line = entry.getKey();
            if (equal(Value, "订单号", "订单单号", "订单型号")) Keymap.S1500 = entry.getKey();
            if (equal(Value, "订单数量")) Keymap.Count = entry.getKey();
            if (equal(Value, "型号")) Keymap.Model = entry.getKey();
            if (equal(Value, Times)) {
                Keymap.StartTime = entry.getKey();
                Keymap.thisTime = Value;
                Keymap.setTimeKey(Value, entry.getKey());
            } else if (Keymap.StartTime == null)
                for (String thisDate : Times)
                    if (Value.startsWith(thisDate + "(") || Value.startsWith(thisDate + "（")) {
                        Keymap.StartTime = entry.getKey();
                        Keymap.thisTime = Value;
                        Keymap.setTimeKey(Value, entry.getKey());
                        break;
                    }
            if (Keymap.StartTime != null && Keymap.getTimeKeySize() < 2) {
                int TaskDays = Main.getConfig().getInt("显示计划天数", 3);
                int key;
                String otherDate;
                for (int index = 1; index < map.size() && Keymap.getTimeKeySize() < TaskDays; index++) {
                    key = Keymap.StartTime + index;
                    otherDate = Tool.objToString(map.get(Keylist.get(key)), null);
                    if (otherDate == null || otherDate.isEmpty() || Keymap.containsKey(otherDate)) continue;
                    Keymap.setTimeKey(otherDate, key);
                }
            }
            if (Keymap.isInit()) break;
        }
        // 吧计划里所有显示的时间/日期都转为时间戳保存起来方便使用
        String excelDateString, thisTimeString;
        List<SimpleDateFormat> formatList = new ArrayList<>();
        for (String DateFormat : ExcelFactory.DateFormat)
            formatList.add(new SimpleDateFormat(DateFormat));
        long time, error;
        Long excelTime;
        allTaskTimes.clear();
        for (Map.Entry<Integer, Object> entry : Keymap.excelMap.entrySet()) {
            excelDateString = Tool.objToString(entry.getValue(), null);
            if (excelDateString == null || excelDateString.isEmpty()) continue;
            error = 0;time = Keymap.thisTimemillisecond;
            excelTime = null;
            while (error++ < 100 && Main.runing && excelTime == null) {
                date = new Date(time);
                for (SimpleDateFormat format : formatList) {
                    thisTimeString = format.format(date);
                    if (((excelDateString.contains("(") || excelDateString.contains("（")) &&
                            excelDateString.startsWith(thisTimeString)) || excelDateString.equals(thisTimeString)) {
                        excelTime = time;
                        allTaskTimes.put(entry.getKey(), excelTime);
                        break;
                    }
                }
                time += 86400000L;
            }
        }

    }

    private static boolean equal(String obj, String... strings) {
        for (String s : strings) if (s.equals(obj)) return true;
        return false;
    }

}
