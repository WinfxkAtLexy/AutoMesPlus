package cn.winfxk.lexy.amp.view.main.view.work;

import cn.winfxk.lexy.amp.tool.view.MyJPanel;

import javax.swing.*;
import java.awt.*;

public class TitleView extends MyJPanel {
    private final static Font font = new Font("楷体", Font.BOLD, 25);
    private final JLabel title;

    public TitleView() {
        super();
        this.title = new JLabel("近期MES录入明细", SwingConstants.CENTER);
        this.title.setLocation(0, 0);
        this.title.setOpaque(false);
        this.title.setFont(font);
        this.add(title);
    }

    @Override
    public void start() {
        this.title.setSize(getSize());
    }
}
