package cn.winfxk.lexy.amp.view.setting.view.email;

import cn.winfxk.lexy.amp.tool.Tool;
import cn.winfxk.lexy.amp.tool.view.MyJPanel;
import cn.winfxk.lexy.amp.tool.view.button.Button;
import cn.winfxk.lexy.amp.view.setting.view.email.setting.Setting;

import java.awt.*;

public class ButtonView extends MyJPanel {
    private static final Font font = new Font("楷体", Font.BOLD, 18);
    private final Button button, setting;

    public ButtonView() {
        super();
        button = new Button("关闭");
        button.setFont(font);
        button.setOnClickListener(event -> Emailsetting.getFrame().dispose());
        add(button);
        setting = new Button("其它设置");
        setting.setFont(font);
        setting.setOnClickListener(event -> Setting.setVisible());
        add(setting);
    }

    @Override
    public void start() {
        int width = Tool.getMath(getWidth(), 120, getWidth() / 5);
        int padding = Tool.getMath(100, 10, getWidth() / 10);
        int x = getWidth() / 2 - (width * 2 + padding) / 2;
        setting.setSize(width, getHeight());
        setting.setLocation(x, 0);
        setting.start();
        button.setSize(setting.getSize());
        button.setLocation(setting.getX() + setting.getWidth() + padding, setting.getY());
        button.start();
    }
}
