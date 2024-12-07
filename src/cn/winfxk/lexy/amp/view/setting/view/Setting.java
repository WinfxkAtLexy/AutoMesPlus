package cn.winfxk.lexy.amp.view.setting.view;

import cn.winfxk.lexy.amp.Main;
import cn.winfxk.lexy.amp.tool.Tool;
import cn.winfxk.lexy.amp.tool.view.JOptionPane;
import cn.winfxk.lexy.amp.tool.view.MyJPanel;
import cn.winfxk.lexy.amp.view.AutoMes;
import cn.winfxk.lexy.amp.view.setting.TitleView;
import cn.winfxk.lexy.amp.view.setting.view.setting.BaseView;
import cn.winfxk.lexy.amp.view.setting.view.setting.ButtonView;
import cn.winfxk.lexy.amp.view.setting.view.setting.Panel;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

import static cn.winfxk.lexy.amp.Main.ScreenHeight;
import static cn.winfxk.lexy.amp.Main.ScreenWidth;

public class Setting extends MyJPanel {
    private static final int defWidth = Math.min(1000, ScreenWidth), defHeight = Math.min(800, ScreenHeight);
    private static final JFrame frame = new JFrame();
    private final ButtonView buttonView;
    private final TitleView titleView;
    private static Setting main;
    private final Panel panel;

    public Setting() {
        super();
        main = this;
        frame.setTitle("应用设置");
        frame.setIconImage(AutoMes.frame.getIconImage());
        frame.setSize(defWidth, defHeight);
        frame.setLocation(ScreenWidth / 2 - frame.getWidth() / 2, Main.ScreenHeight / 2 - frame.getHeight() / 2);
        frame.addComponentListener(this);
        frame.setContentPane(this);
        setSize(frame.getSize());
        titleView = new TitleView();
        titleView.setLocation(0, 0);
        add(titleView);
        add(panel = new Panel());
        add(buttonView = new ButtonView(this));
    }

    @Override
    public void start() {
        titleView.setSize(getWidth(), Tool.getMath(150, 100, getHeight() / 8));
        titleView.start();
        panel.setSize(getWidth(), getHeight() - titleView.getHeight() - 20);
        panel.setLocation(0, titleView.getHeight() + 10);
        panel.start();
        buttonView.setSize(getWidth(), 50);
        buttonView.setLocation(0, getHeight() - 20 - buttonView.getHeight());
        buttonView.start();
    }

    public static Setting getMain() {
        if (main == null) main = new Setting();
        return main;
    }

    public void setVisible() {
        if (!frame.isVisible()) frame.setVisible(true);
        start();
    }

    public void onSave() {
        String v;
        boolean isOK = true;
        Map<String, Object> map = new HashMap<>();
        int i;
        for (BaseView view : panel.getList()) {
            v = view.getText();
            if (v == null || v.isEmpty()) {
                JOptionPane.showMessageDialog(null, "所有值均不能为空！请输入" + view.getHint() + "的值！", "错误", JOptionPane.ERROR_MESSAGE);
                isOK = false;
                break;
            }
            if (!Tool.isInteger(v)) {
                JOptionPane.showMessageDialog(null, "所有值均仅支持纯数字！！请更改" + view.getHint() + "的值！", "错误", JOptionPane.ERROR_MESSAGE);
                isOK = false;
                break;
            }
            i = Tool.ObjToInt(v);
            if (i <= 0) {
                JOptionPane.showMessageDialog(null, "所有值均仅支持大于零的纯数字！！请更改" + view.getHint() + "的值！", "错误", JOptionPane.ERROR_MESSAGE);
                isOK = false;
                break;
            }
            map.put(view.getKey(), i);
        }
        if (!isOK) return;
        for (Map.Entry<String, Object> entry : map.entrySet())
            Main.getConfig().set(entry.getKey(), entry.getValue());
        Main.getConfig().save();
        JOptionPane.showMessageDialog(null, "保存成功！部分设置可能需要重启才会生效！", "提示", JOptionPane.PLAIN_MESSAGE);
        frame.dispose();
    }

    public void onClear() {
        if (JOptionPane.showConfirmDialog(null, "您确定要将配置设置为默认值吗？这同时会删除权限设置\n回复后您可能需要重新添加管理权限。", "警告",
                JOptionPane.WARNING_MESSAGE, JOptionPane.OK_CANCEL_OPTION) != 0) return;
        Main.reloadCOnfig();
        JOptionPane.showMessageDialog(null, "已将配置设置为默认值！", "提示", JOptionPane.PLAIN_MESSAGE);
        frame.dispose();
    }
}
