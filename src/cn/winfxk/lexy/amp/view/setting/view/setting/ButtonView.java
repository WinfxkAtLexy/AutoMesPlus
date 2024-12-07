package cn.winfxk.lexy.amp.view.setting.view.setting;

import cn.winfxk.lexy.amp.tool.view.MyJPanel;
import cn.winfxk.lexy.amp.tool.view.button.Button;
import cn.winfxk.lexy.amp.view.setting.view.Setting;

import java.awt.*;

public class ButtonView extends MyJPanel {
    private final static Font font = new Font("楷体", Font.BOLD, 20);
    private final Button save, clear;

    public ButtonView(Setting setting) {
        super();
        save = new Button("保存");
        save.setFont(font);
        save.setOnClickListener(event -> setting.onSave());
        add(save);
        clear = new Button("恢复默认");
        clear.setFont(font);
        clear.setOnClickListener(event -> setting.onClear());
        add(clear);
    }

    @Override
    public void start() {
        int width = Math.min(150, Math.max(100, getWidth() / 3));
        int panding = Math.max(100, Math.min(150, getWidth() / 5));
        save.setSize(width, getHeight());
        clear.setSize(save.getSize());
        save.setLocation(getWidth() / 2 - (save.getWidth() + clear.getWidth() + panding) / 2, 0);
        clear.setLocation(save.getX() + save.getWidth() + panding, 0);
        clear.start();
        save.start();
    }
}
