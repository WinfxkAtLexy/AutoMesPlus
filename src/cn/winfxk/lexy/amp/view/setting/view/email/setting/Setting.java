package cn.winfxk.lexy.amp.view.setting.view.email.setting;

import cn.winfxk.lexy.amp.Image;
import cn.winfxk.lexy.amp.Log;
import cn.winfxk.lexy.amp.Main;
import cn.winfxk.lexy.amp.tool.Tool;
import cn.winfxk.lexy.amp.tool.view.JOptionPane;
import cn.winfxk.lexy.amp.tool.view.MyJPanel;
import cn.winfxk.lexy.amp.view.setting.TitleView;

import javax.swing.*;

import static cn.winfxk.lexy.amp.Main.ScreenHeight;
import static cn.winfxk.lexy.amp.Main.ScreenWidth;

public class Setting extends MyJPanel {
    private static final int defWidth = Math.min(800, ScreenWidth), defHeight = Math.min(700, ScreenHeight);
    private static final JFrame frame = new JFrame();
    private final TitleView titleView;
    private static Setting main;
    private final Panel panel;

    private Setting() {
        super();
        main = this;
        frame.setTitle("预警邮件高级设置");
        frame.setIconImage(Image.getEmail());
        frame.setSize(defWidth, defHeight);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setLocation(ScreenWidth / 2 - frame.getWidth() / 2, ScreenHeight / 2 - frame.getHeight() / 2);
        frame.setContentPane(this);
        frame.addComponentListener(this);
        setSize(frame.getSize());
        titleView = new TitleView();
        titleView.setLocation(0, 0);
        add(titleView);
        add(panel = new Panel());
    }

    public static void Save() {
        String pc = Master.getText();
        boolean box = Switch.getSwitvh();
        if ((pc == null || pc.isEmpty()) && box) {
            JOptionPane.showMessageDialog(null, "请输入需要发送预警邮件的电脑名称！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String title = Title.getText();
        if ((title == null || title.isEmpty()) && box) {
            JOptionPane.showMessageDialog(null, "请输入发送预警邮件的标题！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Main.getConfig().set("启用邮件系统", box);
        Main.getConfig().set("邮件发送主机", pc);
        Main.getConfig().set("预警邮件标题", title);
        if (Main.getConfig().save()) {
            Log.i("高级邮件设置保存成功！");
            JOptionPane.showMessageDialog(null, "保存成功！", "提示", JOptionPane.PLAIN_MESSAGE);
            frame.dispose();
        } else JOptionPane.showMessageDialog(null, "保存遇到问题！可能尚未保存成功，请尝试再次保存。",
                "警告", JOptionPane.WARNING_MESSAGE);
    }

    @Override
    public void start() {
        titleView.setSize(getWidth(), Tool.getMath(200, 100, getHeight() / 7));
        titleView.start();
        panel.setSize(getWidth(), getHeight() - titleView.getHeight());
        panel.setLocation(0, titleView.getY() + titleView.getHeight());
        panel.start();
    }

    public static void setVisible() {
        if (main == null) main = new Setting();
        if (!frame.isVisible()) frame.setVisible(true);
        main.start();
        frame.setFocusable(true);
    }

    public static JFrame getFrame() {
        return frame;
    }
}
