package cn.winfxk.lexy.amp.view.main.view.work;

import cn.winfxk.lexy.amp.Log;
import cn.winfxk.lexy.amp.Main;
import cn.winfxk.lexy.amp.mes.MesItem;
import cn.winfxk.lexy.amp.mes.Serialization;
import cn.winfxk.lexy.amp.tool.Tool;
import cn.winfxk.lexy.amp.tool.view.MyJPanel;
import cn.winfxk.lexy.amp.view.AutoMes;
import cn.winfxk.lexy.amp.view.main.view.Progress;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Panel extends MyJPanel implements Runnable {
    protected static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    private static final int ItemHeight = 50;
    private static final int space = 25;
    public final static int sleep = 1;
    private final JLabel label;
    private static Panel main;

    public Panel() {
        super();
        main = this;
        new Thread(this).start();
        label = new JLabel("", SwingConstants.CENTER);
        label.setBackground(Color.black);
        label.setLocation(0, 0);
        label.setOpaque(true);
    }

    public static Panel getMain() {
        return main;
    }

    @Override
    public void start() {
    }

    public void reload() {
        Log.i("准备更新时间线");
        removeAll();
        label.setSize(1, 0);
        add(label);
        int day = Math.max(7, Main.getConfig().getInt("时间线显示天数", 10));
        long time = System.currentTimeMillis(), oldTime = time - (86400000L * day);
        Map<Long, List<MesItem>> map = Serialization.getMesItemByTime(oldTime, time);
        Log.i("已提取需要显示时间线的MES清单(最近" + day + "天)，正在准备解析");
        int max = 0;
        for (List<MesItem> list : map.values())
            max = Math.max(max, list == null ? 0 : list.size());
        LineView lineView;
        List<Long> keylist = new ArrayList<>(map.keySet());
        long cacheTime, cacheKey;
        Log.i("解析完毕！准备更新UI。");
        for (int i = 0; i < day; i++) {
            cacheKey = cacheTime = time - (i * 86400000L);
            for (Long l : keylist) {
                if (dateFormat.format(l).equals(dateFormat.format(cacheTime))) {
                    cacheKey = l;
                    break;
                }
            }
            lineView = new LineView(dateFormat.format(cacheTime), map.get(cacheKey), max);
            lineView.setSize(getWidth(), ItemHeight);
            lineView.setLocation(label.getWidth() + 1, label.getHeight());
            lineView.start();
            add(lineView);
            for (int lineHeight = 0; lineHeight < ((i < day - 1 ? space : 0) + ItemHeight); lineHeight++) {
                label.setSize(label.getWidth(), label.getHeight() + 1);
                label.updateUI();
                Tool.sleep(sleep);
            }
        }
    }

    @Override
    public void run() {
        Log.i("时间线系统启动！");
        int time = 0, maxTime = Math.max(60, Main.getConfig().getInt("时间线更新间隔", 60));
        Progress.getLineProgres().setMax(maxTime * 1000);
        while (Main.runing) {
            for (int i = 0; i < 1000; i++) {
                Tool.sleep(1);
                Progress.getLineProgres().setCurrent(((maxTime - time) * 1000) + i);
            }
            if (!Main.runing) break;
            if (AutoMes.isIsSleep() || time-- > 0) continue;
            maxTime = Math.max(60, Main.getConfig().getInt("时间线更新间隔", 60));
            Progress.getLineProgres().setMax((time = maxTime) * 1000);
            reload();
        }
    }
}
