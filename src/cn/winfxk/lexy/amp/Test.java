package cn.winfxk.lexy.amp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Test {
    private static final SimpleDateFormat TaskFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat TitleFormat = new SimpleDateFormat("yyyy-MM");

    public static void main(String[] args) {
        reload(System.currentTimeMillis());
    }

    public static void reload(long time) {
        Date date = new Date(time);
        String title = TitleFormat.format(date);
        try {
            Date startTime = TaskFormat.parse(title + "-01");
            long startTiemms = startTime.getTime();
            long endTimems = startTiemms;
            String MMdate = TitleFormat.format(endTimems);
            while (title.equals(MMdate))
                MMdate = TitleFormat.format(endTimems += 86400000L);
            endTimems -= 86400000L;
            System.out.println(TaskFormat.format(endTimems));
        } catch (ParseException e) {
            Log.e("反序列化日期时出现问题！", e);
        }
    }
}
