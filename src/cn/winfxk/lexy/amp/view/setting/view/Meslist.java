package cn.winfxk.lexy.amp.view.setting.view;

import cn.winfxk.lexy.amp.Main;
import cn.winfxk.lexy.amp.tool.Tool;
import cn.winfxk.lexy.amp.tool.view.MyJPanel;
import cn.winfxk.lexy.amp.tool.view.list.ListView;
import cn.winfxk.lexy.amp.view.AutoMes;
import cn.winfxk.lexy.amp.view.setting.TitleView;
import cn.winfxk.lexy.amp.view.setting.view.mes.MainAdapter;

import javax.swing.*;

import static cn.winfxk.lexy.amp.Main.ScreenHeight;
import static cn.winfxk.lexy.amp.Main.ScreenWidth;

public class Meslist extends MyJPanel {
    private static final int defWidth = Math.min(500, ScreenWidth), defHeight = Math.min(800, ScreenHeight);
    private static final JFrame frame = new JFrame();
    private final TitleView titleView;
    private final MainAdapter adapter;
    private final ListView listView;
    private static Meslist main;

    public Meslist() {
        super();
        main = this;
        frame.setTitle("关键零部件清单");
        frame.setIconImage(AutoMes.frame.getIconImage());
        frame.setSize(defWidth, defHeight);
        frame.setLocation(ScreenWidth / 2 - frame.getWidth() / 2, Main.ScreenHeight / 2 - frame.getHeight() / 2);
        frame.addComponentListener(this);
        frame.setContentPane(this);
        setSize(frame.getSize());
        titleView = new TitleView();
        titleView.setLocation(0, 0);
        add(titleView);
        adapter = new MainAdapter();
        listView = new ListView(adapter);
        add(listView);
    }

    public static void reload() {
        main.adapter.UpdateAdapter();
    }

    @Override
    public void start() {
        titleView.setSize(getWidth(), 120);
        titleView.start();
        listView.setLocation(titleView.getX(), titleView.getHeight());
        listView.setSize(getWidth(), getHeight() - titleView.getHeight() - 10);
        new Thread(() -> {
            Tool.sleep(300);
            adapter.UpdateAdapter();
        }).start();
    }

    public static Meslist getMain() {
        if (main == null) main = new Meslist();
        return main;
    }

    public void setVisible() {
        if (!frame.isVisible()) frame.setVisible(true);
        start();
    }
}
