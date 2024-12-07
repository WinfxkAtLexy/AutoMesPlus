package cn.winfxk.lexy.amp.view.setting.view;

import cn.winfxk.lexy.amp.tool.view.MyJPanel;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class Author extends MyJPanel {
    public static final String text = "作者：Winfxk（冰月）<br>微信: WinfxkBy<br>Mail: admin@winfxk.com<br>Mail: ji.luo@Kingclean.com<br>GitHub: https://github.com/Winfxk";
    private static final Font titleFont = new Font("楷体", Font.BOLD, 20);
    private static final Font font = new Font("黑体", Font.PLAIN, 20);


    public Author() {
        super();
        setLayout(new GridLayout(0, 2));
        add(getTitle("作者"));
        add(getString("Winfxk（冰月）"));
        add(getTitle("微信"));
        add(getString("WinfxkBy"));
        add(getTitle("Mail"));
        add(getString("admin@winfxk.com"));
        add(getTitle("Mail"));
        add(getString("ji.luo@Kingclean.com"));
        add(getTitle("GitHub"));
        add(getString("https://github.com/Winfxk"));
        add(getTitle("Tel"));
        add(getString("17585577428"));
    }

    @Override
    public void start() {
    }

    private JLabel getString(String s) {
        JLabel label = new JLabel(s);
        label.setFont(font);
        label.setOpaque(false);
        return label;
    }

    private JLabel getTitle(String s) {
        JLabel label = new JLabel(s + ":  ", SwingConstants.RIGHT);
        label.setFont(titleFont);
        label.setOpaque(false);
        return label;
    }
}
