package cn.winfxk.lexy.amp.view.setting.view.email.setting;

import cn.winfxk.lexy.amp.Main;
import cn.winfxk.lexy.amp.tool.view.MyJPanel;

import javax.swing.*;

public class Title extends MyJPanel {
    private final JTextField edit;
    private final JLabel label;
    private static Title main;

    public Title() {
        super();
        main = this;
        label = new JLabel("预警邮件发送标题:", SwingConstants.CENTER);
        label.setOpaque(false);
        label.setLocation(0, 0);
        label.setFont(Panel.font);
        add(label);
        edit = new JTextField(Main.getConfig().getString("预警邮件标题", ""));
        edit.setFont(Panel.font);
        edit.setOpaque(false);
        add(edit);
    }

    public static String getText() {
        return main.edit.getText();
    }

    @Override
    public void start() {
        label.setSize((label.getText().length() + 1) * Panel.font.getSize(), getHeight());
        edit.setSize(getWidth() - label.getWidth(), getHeight());
        edit.setLocation(label.getX() + label.getWidth(), 0);
    }
}
