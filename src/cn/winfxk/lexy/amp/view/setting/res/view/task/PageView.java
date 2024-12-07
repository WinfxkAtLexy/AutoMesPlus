package cn.winfxk.lexy.amp.view.setting.res.view.task;

import cn.winfxk.lexy.amp.Image;
import cn.winfxk.lexy.amp.tool.view.MyJPanel;
import cn.winfxk.lexy.amp.tool.view.image.ImageButton;
import cn.winfxk.lexy.amp.view.setting.res.view.Task;

import javax.swing.*;
import java.awt.*;

public class PageView extends MyJPanel {
    private static final Font font = new Font("楷体", Font.BOLD, 25);
    private final ImageButton up, down;
    private final JLabel timeView;

    public PageView(Task task) {
        super();
        up = new ImageButton(Image.getPageUp());
        up.setLocation(0, 0);
        up.setOnClick(task::onClickUp);
        add(up);
        down = new ImageButton(Image.getPageDown());
        down.setOnClick(task::onClickDown);
        add(down);
        timeView = new JLabel("", SwingConstants.CENTER);
        timeView.setFont(font);
        timeView.setOpaque(false);
        add(timeView);
    }

    public void setText(String text) {
        timeView.setText(text);
    }

    @Override
    public void start() {
        up.setSize(getHeight(), getHeight());
        up.start();
        down.setSize(getHeight(), getHeight());
        down.setLocation(getWidth() - down.getWidth() - 10, 0);
        down.start();
        timeView.setSize(down.getX() - up.getX() - up.getWidth(), getHeight());
        timeView.setLocation(0, 0);
    }
}
