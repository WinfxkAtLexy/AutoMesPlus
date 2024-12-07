package cn.winfxk.lexy.amp.view.setting.view.email;

import cn.winfxk.lexy.amp.Main;
import cn.winfxk.lexy.amp.tool.Tool;
import cn.winfxk.lexy.amp.tool.view.MyJPanel;
import cn.winfxk.lexy.amp.view.AutoMes;
import cn.winfxk.lexy.amp.view.setting.TitleView;

import javax.swing.*;

import static cn.winfxk.lexy.amp.Main.ScreenHeight;
import static cn.winfxk.lexy.amp.Main.ScreenWidth;

public class Emailsetting extends MyJPanel {
    private static final int defWidth = Math.min(800, ScreenWidth), defHeight = Math.min(800, ScreenHeight);
    private static final JFrame frame = new JFrame();
    private final TitleView titleView;
    private static Emailsetting main;
    private final Panel panel;

    public Emailsetting() {
        super();
        main = this;
        frame.setTitle("预警邮件管理");
        frame.setIconImage(AutoMes.frame.getIconImage());
        frame.setSize(defWidth, defHeight);
        frame.setLocation(ScreenWidth / 2 - frame.getWidth() / 2, Main.ScreenHeight / 2 - frame.getHeight() / 2);
        frame.addComponentListener(this);
        frame.setContentPane(this);
        setSize(frame.getSize());
        titleView = new TitleView();
        titleView.setLocation(0, 0);
        add(titleView);
        panel = new Panel();
        add(panel);
    }

    @Override
    public void start() {
        titleView.setSize(getWidth(), Tool.getMath(150, 100, getHeight() / 8));
        titleView.start();
        panel.setLocation(0, titleView.getHeight() + titleView.getY());
        panel.setSize(getWidth(), getHeight() - titleView.getHeight() - 10);
        panel.start();
    }

    public static Emailsetting getMain() {
        if (main == null) main = new Emailsetting();
        return main;
    }

    public static JFrame getFrame() {
        return frame;
    }

    public void setVisible() {
        if (!frame.isVisible()) frame.setVisible(true);
        start();
    }
}
