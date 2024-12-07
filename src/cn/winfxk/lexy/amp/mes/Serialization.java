package cn.winfxk.lexy.amp.mes;

import cn.winfxk.lexy.amp.Log;
import cn.winfxk.lexy.amp.Main;
import cn.winfxk.lexy.amp.tool.Utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 用来序列化MES数据的封装类
 */
public class Serialization {
    /**
     * 保存MES数据时的文件名
     */
    public static final SimpleDateFormat Filename = new SimpleDateFormat("yyyy/MM/dd");

    /**
     * 根据时间段获取对应的MES数据
     *
     * @param date1 想要获取数据的开始时间
     * @param date2 想要获取数据的结束时间
     * @return 这个时间段内的MES数据
     */
    public static Map<Long, List<MesItem>> getMesItemByTime(long date1, long date2) {
        return getMesItemByTime(new Date(Math.min(date1, date2)), new Date(Math.max(date1, date2)));
    }

    /**
     * 根据时间段获取对应的MES数据
     *
     * @param date1 想要获取数据的开始时间
     * @param date2 想要获取数据的结束时间
     * @return 这个时间段内的MES数据
     */
    public static Map<Long, List<MesItem>> getMesItemByTime(Date date1, Date date2) {
        long startTime = Math.min(date1.getTime(), date2.getTime());
        long endTime = Math.max(date1.getTime(), date2.getTime());
        Map<Long, List<MesItem>> map = new HashMap<>();
        File file;
        long time = startTime;
        List<MesItem> list;
        while (time < (endTime + 86400000L)) {
            file = getMesDataByTime(time);
            try {
                if (!file.exists() || !file.isFile()) continue;
                list = getMesItemByTime(time);
                if (list == null || list.isEmpty()) continue;
                map.put(time, list);
            } catch (Exception e) {
                Log.w("在获取" + file.getAbsolutePath() + "的数据时出现异常！", e);
            } finally {
                time += 86400000L;
            }
        }
        return map;
    }

    /**
     * 根据时间获取对应的MES数据
     *
     * @param date 想要获取数据的时间
     * @return 这个时间段内的MES数据
     */
    public static List<MesItem> getMesItemByTime(Date date) {
        List<MesItem> list = new ArrayList<>();
        File file = getMesDataByTime(date);
        if (!file.exists()) {
            Log.e("无法根据时间" + Filename.format(date) + "获取MES数据，因为所获取的文件不存在！");
            return null;
        }
        try {
            String string = Utils.readFile(file);
            if (string.isEmpty()) {
                Log.e("无法根据时间" + Filename.format(date) + "获取MES数据，因为所获取数据为空！！");
                return null;
            }
            for (String s : string.split("\n"))
                if (s != null && !s.isEmpty())
                    list.add(new MesItem(s));
            return list;
        } catch (Exception e) {
            Log.e("无法读取对应的MES数据！文件地址：" + file.getAbsolutePath(), e);
            return null;
        }
    }

    /**
     * 根据时间获取对应的MES数据
     *
     * @param date 想要获取数据的时间
     * @return 这个时间段内的MES数据
     */
    public static List<MesItem> getMesItemByTime(long date) {
        return getMesItemByTime(new Date(date));
    }

    /**
     * 根据提供的时间戳返回对应的MES原始数据
     *
     * @param time 需要提取MES数据的时间戳
     * @return MES数据
     */
    public static File getMesDataByTime(long time) {
        return getMesDataByTime(new Date(time));
    }

    /**
     * 根据提供的时间返回对应的MES原始数据
     *
     * @param time 需要提取MES数据的时间
     * @return MES数据
     */
    public static File getMesDataByTime(Date time) {
        return new File(Main.MesDataDir, Filename.format(time) + ".xk");
    }
}
