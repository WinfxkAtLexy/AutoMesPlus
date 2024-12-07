package cn.winfxk.lexy.amp.view.setting.view.email.setting;

import cn.winfxk.lexy.amp.tool.view.MyJPanel;

import java.awt.*;

public class Panel extends MyJPanel {
    public static final Font font = new Font("楷体", Font.BOLD, 18);
    private static final int ItemHeight = 50;
    private final ButtonView buttonView;
    private final Switch aSwitch;
    private final Master master;
    private final Title title;

    public Panel() {
        super();
        title = new Title();
        title.setLocation(0, 0);
        add(title);
        add(aSwitch = new Switch());
        add(master = new Master());
        add(buttonView = new ButtonView());
    }

    @Override
    public void start() {
        Dimension size = new Dimension(getWidth(), ItemHeight);
        title.setSize(size);
        title.setLocation(0, 0);
        title.start();
        aSwitch.setSize(size);
        aSwitch.setLocation(0, title.getY() + title.getHeight() + 10);
        aSwitch.start();
        master.setSize(size);
        master.setLocation(0, aSwitch.getY() + aSwitch.getHeight() + 10);
        master.start();
        buttonView.setSize(size);
        buttonView.setLocation(0, getHeight() - buttonView.getHeight() - 10);
        buttonView.start();
    }
}
