package cn.winfxk.lexy.amp.view.main;

import cn.winfxk.lexy.amp.tool.view.MyJPanel;
import cn.winfxk.lexy.amp.view.main.view.Progress;

import javax.swing.*;
import java.awt.*;

/**
 * 一个用来显示标题的容器
 */
public class TitleView extends MyJPanel {
    public static final Font font = new Font("楷体", Font.BOLD, 80);
    private final Progress progress;
    private final JLabel title;

    /**
     * 一个用来显示标题的容器
     */
    public TitleView() {
        super();
        title = new JLabel("关键零部件检验预警系统", SwingConstants.CENTER);
        title.setFont(font);
        title.setOpaque(false);
        title.setLocation(0, 0);
        add(title, CENTER_ALIGNMENT);
        progress = new Progress();
        progress.setLocation(30, 20);
        add(progress);
    }

    @Override
    public void start() {
        title.setSize(getSize());
        progress.setSize(300, 50);
        progress.start();
    }
}
