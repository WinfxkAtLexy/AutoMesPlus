package cn.winfxk.lexy.amp.view.main.view;

import cn.winfxk.lexy.amp.tool.view.MyJPanel;
import cn.winfxk.lexy.amp.view.main.view.work.Panel;
import cn.winfxk.lexy.amp.view.main.view.work.TitleView;

/**
 * 一个用于显示最近几天录入情况的容器
 */
public class RecentlyWork extends MyJPanel {
    private final TitleView titleView;
    private final Panel panel;

    /**
     * 一个用于显示最近几天录入情况的容器
     */
    public RecentlyWork() {
        super();
        titleView = new TitleView();
        titleView.setLocation(0, 0);
        add(titleView);
        panel = new Panel();
        add(panel);
    }

    @Override
    public void start() {
        titleView.setSize(getWidth(), 50);
        titleView.start();
        panel.setSize(getWidth(), getHeight() - titleView.getHeight() - 50);
        panel.setLocation(0, titleView.getHeight());
        panel.start();
    }
}
