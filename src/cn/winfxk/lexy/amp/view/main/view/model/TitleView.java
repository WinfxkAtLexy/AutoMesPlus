package cn.winfxk.lexy.amp.view.main.view.model;

import cn.winfxk.lexy.amp.Image;
import cn.winfxk.lexy.amp.tool.view.MyJPanel;
import cn.winfxk.lexy.amp.tool.view.image.ImageButton;

import javax.swing.*;
import java.awt.*;

public class TitleView extends MyJPanel {
    public final static Font font = new Font("楷体", Font.BOLD, 25);
    private final ImageButton pageUp, pageDown;
    private static TitleView main;
    private final JLabel title;

    public TitleView() {
        super();
        main = this;
        title = new JLabel("", SwingConstants.CENTER);
        title.setOpaque(false);
        title.setFont(font);
        add(title, CENTER_ALIGNMENT);
        pageUp = new ImageButton(Image.getPageUp());
        pageUp.setLocation(0, 0);
        add(pageUp);
        add(pageDown = new ImageButton(Image.getPageDown()));
    }

    public void setImageButtonEnabled(boolean enabled) {
        pageUp.setEnabled(enabled);
        pageDown.setEnabled(enabled);
    }

    public static TitleView getMain() {
        return main;
    }

    @Override
    public void start() {
        title.setSize(getWidth(), getHeight());
        title.setLocation(getWidth() / 2 - title.getWidth() / 2, 0);
        pageUp.setSize(getHeight(), getHeight());
        pageUp.setPadding(pageDown.getWidth() / 5);
        pageUp.setOnClick(imageView1 -> Renew.clickPageUp());
        pageUp.start();
        pageDown.setPadding(pageDown.getWidth() / 5);
        pageDown.setOnClick(imageView -> Renew.clickPageDown());
        pageDown.setSize(getHeight(), getHeight());
        pageDown.setLocation(getWidth() - pageDown.getWidth(), 0);
        pageDown.start();
    }

    /**
     * @param title 设置要显示的日期文本
     */
    public static void setTitleString(String title) {
        main.title.setText(title);
    }
}
