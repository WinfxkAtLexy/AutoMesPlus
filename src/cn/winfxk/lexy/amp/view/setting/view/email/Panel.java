package cn.winfxk.lexy.amp.view.setting.view.email;

import cn.winfxk.lexy.amp.tool.Tool;
import cn.winfxk.lexy.amp.tool.view.MyJPanel;
import cn.winfxk.lexy.amp.view.setting.view.email.time.Timelist;

public class Panel extends MyJPanel {

    private final Timelist timelist;
    private final SubPanel panel;

    public Panel() {
        super();
        timelist = new Timelist();
        timelist.setLocation(0, 0);
        add(timelist);
        panel = new SubPanel();
        add(panel);
    }

    @Override
    public void start() {
        timelist.setSize(getWidth(), Tool.getMath(500, 300, getHeight() / 5));
        timelist.start();
        panel.setLocation(0, timelist.getY() + timelist.getHeight() + 10);
        panel.setSize(getWidth(), getHeight() - panel.getY());
        panel.start();
    }
}
