package cn.winfxk.lexy.amp.view.log;

import cn.winfxk.lexy.amp.tool.view.MyJPanel;

import javax.swing.*;
import java.awt.*;

public class TitleView extends MyJPanel {
    private static final Font font = new Font("楷体", Font.BOLD, 20);
    private final JLabel label;

    public TitleView() {
        super();
        setOpaque(true);
        label = new JLabel("最新程序异常点点击查看");
        label.setLocation(0, 0);
        label.setFont(font);
        label.setOpaque(false);
        add(label);
    }

    @Override
    public void start() {
        label.setSize(getSize());
    }
}
