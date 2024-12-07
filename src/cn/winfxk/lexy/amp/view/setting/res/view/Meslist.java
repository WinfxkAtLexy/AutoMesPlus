package cn.winfxk.lexy.amp.view.setting.res.view;

import cn.winfxk.lexy.amp.Image;
import cn.winfxk.lexy.amp.tool.view.MyJPanel;

import javax.swing.*;

import static cn.winfxk.lexy.amp.Main.ScreenHeight;
import static cn.winfxk.lexy.amp.Main.ScreenWidth;

public class Meslist extends MyJPanel {
    private static final int defWidth = Math.min(1000, ScreenWidth), defHeight = Math.min(800, ScreenHeight);
    private static final JFrame frame = new JFrame();
    private static Meslist main;

    public Meslist() {
        super();
        main = this;
        frame.setTitle("资源管理");
        frame.setIconImage(Image.getIcon());
        frame.setSize(defWidth, defHeight);
        frame.setLocation(ScreenWidth / 2 - frame.getWidth() / 2, ScreenHeight / 2 - frame.getHeight() / 2);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setContentPane(this);
        setSize(frame.getSize());
    }

    @Override
    public void start() {

    }

    public static void setVisible() {
        if (main == null) new Task();
        if (!frame.isVisible()) frame.setVisible(true);
        frame.setFocusable(true);
        main.start();
    }

    public static Meslist getMain() {
        return main;
    }
}
