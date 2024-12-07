package cn.winfxk.lexy.amp.view.setting;

import cn.winfxk.lexy.amp.Main;
import cn.winfxk.lexy.amp.tool.Tool;
import cn.winfxk.lexy.amp.tool.view.MyJPanel;
import cn.winfxk.lexy.amp.view.AutoMes;

import javax.swing.*;
import java.awt.*;

public class TitleView extends MyJPanel {
    private static final Font versionFont = new Font("黑体", Font.PLAIN, 15);
    private static final Font titleFont = new Font("楷体", Font.BOLD, 50);
    private final JLabel version;
    private final JLabel title;


    public TitleView() {
        super();
        title = new JLabel(getTtitle(), SwingConstants.CENTER);
        title.setOpaque(false);
        title.setFont(titleFont);
        title.setLocation(0, 0);
        add(title);
        version = new JLabel("版本：" + Main.Version, SwingConstants.CENTER);
        version.setFont(versionFont);
        version.setOpaque(false);
        add(version);
    }

    /**
     * @return 返回一个随机颜色的文字
     */
    private String getTtitle() {
        String title = AutoMes.frame.getTitle();
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < title.length(); i++)
            string.append("<font color=\"").append(getColor()).append("\">").append(title.charAt(i)).append("</font>");
        return "<html>" + string + "</html>";
    }

    /**
     * @return 返回16进制的颜色变吗
     */
    public static String getColor() {
        return "#" + Integer.toHexString(Tool.getRand(0, 255)) +
                Integer.toHexString(Tool.getRand(0, 255)) +
                Integer.toHexString(Tool.getRand(0, 255));
    }

    @Override
    public void start() {
        title.setSize(getWidth(), 80);
        version.setSize(getWidth(), 20);
        version.setLocation(0, title.getY() + title.getHeight());
    }
}
