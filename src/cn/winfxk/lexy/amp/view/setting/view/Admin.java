package cn.winfxk.lexy.amp.view.setting.view;

import cn.winfxk.lexy.amp.Main;
import cn.winfxk.lexy.amp.tool.view.JOptionPane;
import cn.winfxk.lexy.amp.tool.view.MyJPanel;
import cn.winfxk.lexy.amp.view.AutoMes;
import cn.winfxk.lexy.amp.view.setting.TitleView;
import cn.winfxk.lexy.amp.view.setting.view.admin.Input;
import cn.winfxk.lexy.amp.view.setting.view.admin.admin.AdminView;
import cn.winfxk.lexy.amp.view.setting.view.admin.ignore.IgnoreView;

import javax.swing.*;

import static cn.winfxk.lexy.amp.Main.ScreenHeight;
import static cn.winfxk.lexy.amp.Main.ScreenWidth;

public class Admin extends MyJPanel {
    private static final int defWidth = Math.min(1000, ScreenWidth), defHeight = Math.min(800, ScreenHeight);
    private static final JFrame frame = new JFrame();
    public final IgnoreView ignoreView;
    private final TitleView titleView;
    public final AdminView adminView;
    private static Admin main;
    private final Input input;

    public Admin() {
        super();
        JOptionPane.showMessageDialog(null, "该页面用于设置管理员权限和忽略权限\n\n" +
                "管理员权限用于全局管理员，权限高于忽略权限，并且必须拥有管理员权限才能设置应用程序和管理关键零部件清单，\n" +
                "对于该权限请谨慎添加，因为拥有此权限的用户还可以更改其他用户的权限，例如将其他用户设置为非管理员或删除已经录入的关键零部件清单。\n" +
                "同时使用此权限对应用程序进行设置时请谨慎操作，您的操作可能导致关键零部件清单被清空或程序运行异常等，\n" +
                "如果出现问题可将应用程序的“System.yml”文件删除来改善，但同时也会删除一些基础设置,\n" +
                "（只要不删除Keylist.yml文件均不会对关键零部件清单造成影响）\n\n" +
                "忽略权限用于主页点击“已检/未检”物料数时的权限，\n" +
                "拥有这个权限可以设置某些物料忽略本单的检测，可以将一个物料强制更改为已检状态", "功能介绍", JOptionPane.WARNING_MESSAGE);
        main = this;
        frame.setTitle("权限设置");
        frame.setIconImage(AutoMes.frame.getIconImage());
        frame.setSize(defWidth, defHeight);
        frame.setLocation(ScreenWidth / 2 - frame.getWidth() / 2, Main.ScreenHeight / 2 - frame.getHeight() / 2);
        frame.addComponentListener(this);
        frame.setContentPane(this);
        setSize(frame.getSize());
        titleView = new TitleView();
        titleView.setLocation(0, 0);
        add(titleView);
        adminView = new AdminView();
        add(adminView);
        ignoreView = new IgnoreView();
        add(ignoreView);
        input = new Input(this);
        add(input);
    }

    @Override
    public void start() {
        titleView.setSize(getWidth(), 100);
        titleView.start();
        input.setSize(getWidth(), 50);
        input.setLocation(0, getHeight() - input.getHeight() - 20);
        input.start();
        adminView.setLocation(0, titleView.getY() + titleView.getHeight() + 10);
        adminView.setSize(getWidth() / 2, input.getY() - adminView.getY() - 10);
        adminView.start();
        ignoreView.setLocation(adminView.getWidth() + adminView.getX(), adminView.getY());
        ignoreView.setSize(adminView.getSize());
        ignoreView.start();
    }

    public static Admin getMain() {
        if (main == null) main = new Admin();
        return main;
    }

    public void setVisible() {
        if (!frame.isVisible()) frame.setVisible(true);
        start();
    }
}
