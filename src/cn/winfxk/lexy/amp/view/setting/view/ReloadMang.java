package cn.winfxk.lexy.amp.view.setting.view;

import cn.winfxk.lexy.amp.Log;
import cn.winfxk.lexy.amp.Main;
import cn.winfxk.lexy.amp.crucial.CrucialThread;
import cn.winfxk.lexy.amp.mes.WebThread;
import cn.winfxk.lexy.amp.tool.Tool;
import cn.winfxk.lexy.amp.tool.view.JOptionPane;
import cn.winfxk.lexy.amp.tool.view.MyJPanel;
import cn.winfxk.lexy.amp.tool.view.list.ListView;
import cn.winfxk.lexy.amp.tool.view.list.adapter.ArrayAdapter;
import cn.winfxk.lexy.amp.tool.view.list.adapter.ItemClickListener;
import cn.winfxk.lexy.amp.view.AutoMes;
import cn.winfxk.lexy.amp.view.main.view.work.Panel;
import cn.winfxk.lexy.amp.view.setting.TitleView;

import javax.swing.*;
import java.util.LinkedHashMap;

import static cn.winfxk.lexy.amp.Main.ScreenHeight;
import static cn.winfxk.lexy.amp.Main.ScreenWidth;

public class ReloadMang extends MyJPanel {
    private static final int defWidth = Math.min(800, ScreenWidth), defHeight = Math.min(500, ScreenHeight);
    static final LinkedHashMap<String, ItemClickListener> map = new LinkedHashMap<>();
    private static long allMesTime, taskTime, dayMesTime, keylistTime, lineTime;
    private static final JFrame frame = new JFrame();
    private final ArrayAdapter adapter;
    private final TitleView titleView;
    private final ListView listView;
    private static ReloadMang main;

    static {
        map.put("更新全局MES数据", view -> {
            if (Math.abs(allMesTime - System.currentTimeMillis()) < 10000) {
                JOptionPane.showMessageDialog(null, "请勿频繁更新！！", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }
            view.setEnabled(false);
            new Thread(() -> {
                allMesTime = System.currentTimeMillis();
                WebThread.getMain().reloadAll();
                Log.i("已主动更新全局MES数据");
                JOptionPane.showMessageDialog(null, "更新完成！", "提示", JOptionPane.PLAIN_MESSAGE);
                view.setEnabled(true);
            }).start();
        });
        map.put("更新当日MES数据", view -> {
            if (Math.abs(dayMesTime - System.currentTimeMillis()) < 10000) {
                JOptionPane.showMessageDialog(null, "请勿频繁更新！！", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }
            view.setEnabled(false);
            new Thread(() -> {
                dayMesTime = System.currentTimeMillis();
                WebThread.getMain().reloadDay();
                Log.i("已主动更新当日MES数据");
                JOptionPane.showMessageDialog(null, "更新完成！", "提示", JOptionPane.PLAIN_MESSAGE);
                view.setEnabled(true);
            }).start();
        });
        map.put("更新生产滚动计划", view -> {
            if (Math.abs(taskTime - System.currentTimeMillis()) < 10000) {
                JOptionPane.showMessageDialog(null, "请勿频繁更新！！", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }
            view.setEnabled(false);
            new Thread(() -> {
                taskTime = System.currentTimeMillis();
                AutoMes.getFactory().ReloadTask();
                Log.i("已主动更新全局MES数据");
                JOptionPane.showMessageDialog(null, "更新完成！", "提示", JOptionPane.PLAIN_MESSAGE);
                view.setEnabled(true);
            }).start();
        });
        map.put("更新关键零部件清单", view -> {
            if (Math.abs(keylistTime - System.currentTimeMillis()) < 10000) {
                JOptionPane.showMessageDialog(null, "请勿频繁更新！！", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }
            view.setEnabled(false);
            new Thread(() -> {
                keylistTime = System.currentTimeMillis();
                CrucialThread.reload();
                Log.i("已主动更新关键零部件清单");
                JOptionPane.showMessageDialog(null, "更新完成！", "提示", JOptionPane.PLAIN_MESSAGE);
                view.setEnabled(true);
            }).start();
        });
        map.put("更新近日录入量", view -> {
            if (Math.abs(lineTime - System.currentTimeMillis()) < 10000) {
                JOptionPane.showMessageDialog(null, "请勿频繁更新！！", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }
            view.setEnabled(false);
            new Thread(() -> {
                lineTime = System.currentTimeMillis();
                Panel.getMain().reload();
                Log.i("已主动更新近日录入量");
                JOptionPane.showMessageDialog(null, "更新完成！", "提示", JOptionPane.PLAIN_MESSAGE);
                view.setEnabled(true);
            }).start();
        });
    }

    public ReloadMang() {
        super();
        main = this;
        frame.setTitle("更新管理");
        frame.setIconImage(AutoMes.frame.getIconImage());
        frame.setSize(defWidth, defHeight);
        frame.setLocation(ScreenWidth / 2 - frame.getWidth() / 2, Main.ScreenHeight / 2 - frame.getHeight() / 2);
        frame.addComponentListener(this);
        frame.setContentPane(this);
        setSize(frame.getSize());
        titleView = new TitleView();
        titleView.setLocation(0, 0);
        add(titleView);
        listView = new ListView(adapter = new ArrayAdapter(map));
        add(listView);
    }

    @Override
    public void start() {
        titleView.setSize(getWidth(), Tool.getMath(150, 100, frame.getHeight() / 8));
        titleView.start();
        listView.setLocation(0, titleView.getHeight());
        listView.setSize(getWidth(), getHeight() - listView.getY() - 10);
        new Thread(() -> {
            Tool.sleep(300);
            adapter.UpdateAdapter();
        }).start();
    }

    public static ReloadMang getMain() {
        if (main == null) main = new ReloadMang();
        return main;
    }

    public void setVisible() {
        if (!frame.isVisible()) frame.setVisible(true);
        start();
    }
}
