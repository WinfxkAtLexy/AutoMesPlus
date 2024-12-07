package cn.winfxk.lexy.amp.view.setting.view.email.setting;

import cn.winfxk.lexy.amp.Main;
import cn.winfxk.lexy.amp.tool.view.MyJPanel;

import javax.swing.*;

public class Master extends MyJPanel {
    private final JTextField field;
    private final JLabel label;
    private static Master main;

    public Master() {
        super();
        main = this;
        label = new JLabel("选择一台电脑来发送邮件:", SwingConstants.CENTER);
        label.setOpaque(false);
        label.setLocation(0, 0);
        label.setFont(Panel.font);
        add(label);
        field = new JTextField(Main.getConfig().getString("邮件发送主机", ""));
        field.setFont(Panel.font);
        field.setOpaque(false);
        add(field);
    }

    public static String getText() {
        return main.field.getText();
    }

    @Override
    public void start() {
        label.setSize((label.getText().length() + 1) * Panel.font.getSize(), getHeight());
        field.setSize(getWidth() - label.getWidth(), getHeight());
        field.setLocation(label.getX() + label.getWidth(), 0);
    }
}
