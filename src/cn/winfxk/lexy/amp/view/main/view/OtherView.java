package cn.winfxk.lexy.amp.view.main.view;

import cn.winfxk.lexy.amp.Main;
import cn.winfxk.lexy.amp.tool.Tool;
import cn.winfxk.lexy.amp.tool.view.MyJPanel;
import cn.winfxk.lexy.amp.view.AutoMes;

import javax.swing.*;
import java.awt.*;

/**
 * 一个用于显示杂项窗口的容器，例如显示时间等
 */
public class OtherView extends MyJPanel implements Runnable {
    private static final Font font = new Font("楷体", Font.BOLD, 22);
    private final JLabel titleView;
    private final JLabel timeView;

    /**
     * 一个用于显示杂项窗口的容器，例如显示时间等
     */
    public OtherView() {
        super();
        titleView = new JLabel("莱克电气 - " + AutoMes.getBranchFactory().getName(), SwingConstants.CENTER);
        titleView.setFont(font);
        titleView.setOpaque(false);
        titleView.setLocation(0, 0);
        titleView.setVerticalAlignment(SwingConstants.CENTER);
        add(titleView, CENTER_ALIGNMENT);
        timeView = new JLabel("", SwingConstants.CENTER);
        timeView.setFont(font);
        timeView.setOpaque(false);
        timeView.setVerticalAlignment(SwingConstants.CENTER);
        add(timeView, CENTER_ALIGNMENT);
        new Thread(this).start();
    }

    @Override
    public void start() {
        titleView.setSize(getWidth(), getHeight() / 2);
        timeView.setSize(titleView.getSize());
        timeView.setLocation(titleView.getX(), titleView.getHeight());
    }

    @Override
    public void run() {
        while (Main.runing) {
            Tool.sleep(1000);
            timeView.setText(Tool.getDate() + " " + Tool.getTime());
        }
    }
}
