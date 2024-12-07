package cn.winfxk.lexy.amp.view.setting;

import cn.winfxk.lexy.amp.All;
import cn.winfxk.lexy.amp.Log;
import cn.winfxk.lexy.amp.Main;
import cn.winfxk.lexy.amp.tool.view.JOptionPane;
import cn.winfxk.lexy.amp.tool.view.MyJPanel;
import cn.winfxk.lexy.amp.tool.view.button.Button;
import cn.winfxk.lexy.amp.tool.view.list.ListView;
import cn.winfxk.lexy.amp.tool.view.list.adapter.ItemClickListener;
import cn.winfxk.lexy.amp.view.AutoMes;
import cn.winfxk.lexy.amp.view.setting.res.Res;
import cn.winfxk.lexy.amp.view.setting.view.*;
import cn.winfxk.lexy.amp.view.setting.view.email.Emailsetting;

import javax.swing.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static cn.winfxk.lexy.amp.Main.ScreenHeight;
import static cn.winfxk.lexy.amp.Main.ScreenWidth;

/**
 * 设置界面
 */
public class Settinglist extends MyJPanel {
    private static final int defWidth = Math.min(700, ScreenWidth), defHeight = Math.min(800, ScreenHeight);
    static final LinkedHashMap<String, ItemClickListener> map = new LinkedHashMap<>();
    private static final JFrame frame = new JFrame();
    private static final String Passwd = "8342395";
    private final SettingAdapter adapter;
    private final TitleView titleView;
    private final ListView listView;
    private static Settinglist main;
    private final Button button;

    static {
        map.put("权限设置", view -> {
            if (!Main.isAdmin()) {
                String string = JOptionPane.showInputDialog(null, "您还没有管理员权限！如需使用请输入密码！", "警告", JOptionPane.WARNING_MESSAGE);
                if (string == null || string.isEmpty()) return;
                if (!string.equals(Passwd)) {
                    JOptionPane.showMessageDialog(null, "密码错误！", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                List<String> list = Main.getConfig().getList("管理员", new ArrayList<>());
                list.add(Main.Username);
                Log.i("已为" + Main.Username + "添加管理员权限！");
                Main.getConfig().set("管理员", list).save();
            }
            Admin.getMain().setVisible();
        });
        map.put("程序设置", view -> {
            if (!Main.isAdmin()) {
                String string = JOptionPane.showInputDialog(null, "您还没有管理员权限！如需使用请输入密码！", "警告", JOptionPane.WARNING_MESSAGE);
                if (string == null || string.isEmpty()) return;
                if (!string.equals(Passwd)) {
                    JOptionPane.showMessageDialog(null, "密码错误！", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                List<String> list = Main.getConfig().getList("管理员", new ArrayList<>());
                list.add(Main.Username);
                Main.getConfig().set("管理员", list).save();
                Log.i("已为" + Main.Username + "添加管理员权限！");
            }
            Setting.getMain().setVisible();
        });
        map.put("关键零部件清单", view -> {
            if (!Main.isAdmin()) {
                JOptionPane.showMessageDialog(null, "您没有权限执行此项操作！", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Meslist.getMain().setVisible();
        });
        map.put("预警邮件管理", view -> {
            if (!Main.isAdmin()) {
                JOptionPane.showMessageDialog(null, "您没有权限执行此项操作！", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Emailsetting.getMain().setVisible();
        });
        if (All.isAll) map.put("清除默认配置", view -> {
            if (!Main.isAdmin()) {
                String string = JOptionPane.showInputDialog(null, "您还没有管理员权限！如需使用请输入密码！", "警告", JOptionPane.WARNING_MESSAGE);
                if (string == null) return;
                if (!string.equals(Passwd)) {
                    JOptionPane.showMessageDialog(null, "密码错误！您没有权限执行此项操作！", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
            if (JOptionPane.showConfirmDialog(null, "您确定要清除程序的路径设置吗？", "警告", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == 0) {
                Log.i("执行清除默认配置操作。");
                if (All.baseConfigFile.delete()) {
                    JOptionPane.showMessageDialog(null, "清除成功！重启应用程序后可重新选择路径。", "提示", JOptionPane.PLAIN_MESSAGE);
                    Main.close(0);
                } else JOptionPane.showMessageDialog(null, "清除失败！请重试。", "错误", JOptionPane.ERROR_MESSAGE);
            }
        });
        map.put("资源管理", view -> Res.setVisible());
        map.put("更新控制", view -> ReloadMang.getMain().setVisible());
        map.put("关于软件", view -> AbotApp.getMain().setVisible());
    }

    private Settinglist() {
        super();
        frame.setTitle("设置");
        frame.setIconImage(AutoMes.frame.getIconImage());
        frame.setSize(defWidth, defHeight);
        frame.setLocation(ScreenWidth / 2 - frame.getWidth() / 2, Main.ScreenHeight / 2 - frame.getHeight() / 2);
        frame.addComponentListener(this);
        frame.setContentPane(this);
        setSize(frame.getSize());
        titleView = new TitleView();
        titleView.setLocation(0, 0);
        add(titleView);
        listView = new ListView(adapter = new SettingAdapter());
        add(listView);
        button = new Button("关闭");
        button.setFont(SItemView.font);
        button.setOnClickListener(event -> frame.dispose());
        add(button);
    }

    public static Settinglist getMain() {
        if (main == null) main = new Settinglist();
        return main;
    }

    public void setVisible() {
        this.start();
        if (!frame.isVisible()) frame.setVisible(true);
        frame.setFocusable(true);
        frame.setAlwaysOnTop(true);
        frame.setAlwaysOnTop(false);
    }

    @Override
    public void start() {
        titleView.setSize(getWidth(), 100);
        titleView.start();
        button.setSize(Math.min(150, Math.max(getWidth() / 2, 80)), 45);
        button.setLocation(Math.max(getWidth() / 2 - button.getWidth() / 2, 0), getHeight() - 10 - button.getHeight());
        button.start();
        listView.setLocation(0, titleView.getY() + titleView.getHeight() + 20);
        listView.setSize(getWidth(), button.getY() - listView.getY() - 20);
        adapter.UpdateAdapter();
    }
}
