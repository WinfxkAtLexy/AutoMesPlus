package cn.winfxk.lexy.amp.view.setting.view;

import cn.winfxk.lexy.amp.Image;
import cn.winfxk.lexy.amp.Main;
import cn.winfxk.lexy.amp.tool.view.MyJPanel;
import cn.winfxk.lexy.amp.tool.view.button.Button;
import cn.winfxk.lexy.amp.view.AutoMes;
import cn.winfxk.lexy.amp.view.setting.TitleView;

import javax.swing.*;

import static cn.winfxk.lexy.amp.Main.ScreenWidth;

public class AbotApp extends MyJPanel {
    private static final int defWidth = Math.min(900, ScreenWidth);
    private static final JFrame frame = new JFrame();
    private final TitleView titleView;
    private final Button button;
    private final Author author;
    private static AbotApp main;

    public AbotApp() {
        super();
        main = this;
        frame.setSize(defWidth, 100);
        frame.setIconImage(Image.getWinfxk());
        frame.setTitle("关于" + AutoMes.frame.getTitle());
        frame.setContentPane(this);
        frame.setResizable(false);
        setSize(frame.getSize());
        titleView = new TitleView();
        titleView.setLocation(0, 0);
        add(titleView);
        author = new Author();
        add(author);
        button = new Button("关闭");
        button.setOnClickListener(event -> frame.dispose());
        add(button);
    }

    @Override
    public void start() {
        titleView.setSize(defWidth, 100);
        titleView.start();
        author.setSize(defWidth, 300);
        author.setLocation(0, titleView.getY() + titleView.getHeight() + 30);
        author.start();
        button.setSize(Math.min(150, Math.max(defWidth / 2, 80)), 45);
        button.setLocation(Math.max(0, defWidth / 2 - button.getWidth() / 2), author.getY() + author.getHeight() + 20);
        button.start();
        frame.setSize(defWidth, button.getY() + button.getHeight() + 50);
        setSize(getSize());
        frame.setLocation(Main.ScreenWidth / 2 - frame.getWidth() / 2, Main.ScreenHeight / 2 - frame.getHeight() / 2);
    }

    public static AbotApp getMain() {
        if (main == null) main = new AbotApp();
        return main;
    }

    public void setVisible() {
        if (!frame.isVisible()) frame.setVisible(true);
        frame.setFocusable(true);
        frame.setAlwaysOnTop(true);
        frame.setAlwaysOnTop(false);
        start();
    }
}
