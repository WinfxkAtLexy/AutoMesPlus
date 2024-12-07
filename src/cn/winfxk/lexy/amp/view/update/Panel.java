package cn.winfxk.lexy.amp.view.update;

import cn.winfxk.lexy.amp.Main;
import cn.winfxk.lexy.amp.tool.view.MyJPanel;

import javax.swing.*;
import java.awt.*;

public class Panel extends MyJPanel {
    public static final Font titleFont = new Font("楷体", Font.BOLD, 50);
    public static final Font font = new Font("楷体", Font.BOLD, 20);
    private final JLabel label;

    public Panel() {
        super();
        JLabel title = new JLabel("检测到新版本！", SwingConstants.CENTER);
        title.setFont(titleFont);
        title.setOpaque(false);
        title.setLocation(0, 0);
        add(title);
        label = new JLabel("", SwingConstants.CENTER);
        label.setFont(font);
        label.setOpaque(false);
        add(label);
    }

    @Override
    public void start() {
        label.setSize(getSize());
    }

    public void setDate(String title, String Version, int VersionID, String Host, String Hint) {
        label.setText("<html>" + title + "<br>版本：" + Main.Version + "(" + Main.VersionID + ")->" + Version + "(" + VersionID + ")" +
                "<br>新版地址：" + Host + "<br>" + Hint + "</html>");
        label.updateUI();
    }
}
