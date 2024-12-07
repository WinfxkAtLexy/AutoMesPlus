package cn.winfxk.lexy.amp.view.setting.view.email.setting;

import cn.winfxk.lexy.amp.tool.Tool;
import cn.winfxk.lexy.amp.tool.view.MyJPanel;
import cn.winfxk.lexy.amp.tool.view.button.Button;

import java.awt.*;

public class ButtonView extends MyJPanel {
    public static final Font font = new Font("楷体", Font.BOLD, 18);
    private final Button exit, save;

    public ButtonView() {
        super();
        exit = new Button("关闭");
        exit.setFont(font);
        exit.setOnClickListener(event -> Setting.getFrame().dispose());
        add(exit);
        save = new Button("保存");
        save.setFont(font);
        save.setOnClickListener(event -> Setting.Save());
        add(save);
    }

    @Override
    public void start() {
        int width = Tool.getMath(200, 100, getWidth() / 7);
        int padding = Tool.getMath(100, 10, getWidth() / 15);
        int x = getWidth() / 2 - (width * 2 + padding) / 2;
        save.setSize(width, getHeight());
        save.setLocation(x, 0);
        save.start();
        exit.setSize(width, getHeight());
        exit.setLocation(save.getX() + save.getWidth() + padding, 0);
        exit.start();
    }
}
