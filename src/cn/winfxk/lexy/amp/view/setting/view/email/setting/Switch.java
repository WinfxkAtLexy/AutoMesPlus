package cn.winfxk.lexy.amp.view.setting.view.email.setting;

import cn.winfxk.lexy.amp.Main;
import cn.winfxk.lexy.amp.tool.Tool;
import cn.winfxk.lexy.amp.tool.view.MyJPanel;

import javax.swing.*;

public class Switch extends MyJPanel {
    private final JCheckBox box;
    private final JLabel label;
    private static Switch main;

    public Switch() {
        super();
        main = this;
        label = new JLabel("启用预警邮件", SwingConstants.CENTER);
        label.setOpaque(false);
        label.setLocation(0, 0);
        label.setFont(Panel.font);
        add(label);
        box = new JCheckBox();
        box.setFont(Panel.font);
        box.setSelected(Main.getConfig().getBoolean("启用邮件系统"));
        box.setOpaque(false);
        add(box);
    }

    public static boolean getSwitvh() {
        return main.box.isSelected();
    }

    @Override
    public void start() {
        label.setSize((label.getText().length() + 1) * Panel.font.getSize(), getHeight());
        box.setSize(Tool.getMath(150, 80, getWidth() / 8), getHeight());
        box.setLocation(getWidth() - box.getWidth() - 10, 0);
    }
}
