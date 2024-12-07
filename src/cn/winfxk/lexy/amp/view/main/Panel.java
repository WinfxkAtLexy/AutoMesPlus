package cn.winfxk.lexy.amp.view.main;

import cn.winfxk.lexy.amp.tool.view.MyJPanel;
import cn.winfxk.lexy.amp.view.main.view.Modellist;
import cn.winfxk.lexy.amp.view.main.view.OtherView;
import cn.winfxk.lexy.amp.view.main.view.RecentlyWork;
import cn.winfxk.lexy.amp.view.main.view.Tasklist;

import java.awt.*;

/**
 * 主体UI容器
 */
public class Panel extends MyJPanel {
    public static final Color Background = new Color(0xEE, 0xEE, 0XEE);
    private final RecentlyWork recentlyWork;
    private final Modellist modellist;
    private final OtherView otherView;
    private final Tasklist tasklist;
    private static Panel main;

    public static Panel getMain() {
        return main;
    }

    public Tasklist getTasklist() {
        return tasklist;
    }

    /**
     * 主体UI容器
     */
    public Panel() {
        super();
        main = this;
        add(recentlyWork = new RecentlyWork());
        add(modellist = new Modellist());
        add(otherView = new OtherView());
        tasklist = new Tasklist();
        tasklist.setLocation(0, 0);
        add(tasklist);
    }

    @Override
    public void start() {
        tasklist.setSize((int) (getWidth() / 5 * 2.2), getHeight());
        tasklist.start();
        modellist.setSize((int) (getWidth() / 2.8), tasklist.getHeight());
        modellist.setLocation(tasklist.getWidth(), 0);
        modellist.start();
        recentlyWork.setSize(getWidth() - (modellist.getWidth() + modellist.getX()), getHeight());
        recentlyWork.setLocation(modellist.getWidth() + modellist.getX(), 0);
        recentlyWork.start();
        otherView.setSize(300, 80);
        otherView.setLocation(getWidth() - otherView.getWidth() - 80, getHeight() - otherView.getHeight() - 20);
        otherView.start();
    }
}
