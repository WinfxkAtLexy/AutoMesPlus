package cn.winfxk.lexy.amp.view.setting.view.admin;

import cn.winfxk.lexy.amp.Log;
import cn.winfxk.lexy.amp.Main;
import cn.winfxk.lexy.amp.tool.view.JOptionPane;
import cn.winfxk.lexy.amp.tool.view.MyJPanel;
import cn.winfxk.lexy.amp.tool.view.button.Button;
import cn.winfxk.lexy.amp.view.setting.view.Admin;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class Input extends MyJPanel {
    private final static Font buttonFont = new Font("楷体", Font.BOLD, 15);
    private final static Font font = new Font("楷体", Font.BOLD, 20);
    private final Button admin, ignore;
    private final TextField editView;
    private final Admin main;

    public Input(Admin main) {
        super();
        this.main = main;
        editView = new TextField();
        editView.setLocation(0, 0);
        editView.setFont(font);
        add(editView);
        admin = new Button("添加为管理员");
        admin.setFont(buttonFont);
        admin.setOnClickListener(event -> clickAdmin());
        add(admin);
        ignore = new Button("添加忽略权限");
        ignore.setFont(buttonFont);
        ignore.setOnClickListener(event -> clickIgnore());
        add(ignore);
    }

    /**
     * 点击添加忽略权限按钮时
     */
    private void clickIgnore() {
        String string = editView.getText();
        if (string == null || string.isEmpty()) {
            JOptionPane.showMessageDialog(null, "请输入想要添加权限的计算机名！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        List<String> list = Main.getConfig().getList("忽略权限", new ArrayList<>());
        if (list.contains(string)) {
            JOptionPane.showMessageDialog(null, "该用户已有忽略权限！请悉知。", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Log.i("已为用户" + string + "添加忽略权限。");
        editView.setText("");
        JOptionPane.showMessageDialog(null, "操作完成！", "提示", JOptionPane.PLAIN_MESSAGE);
        list.add(string);
        Main.getConfig().set("忽略权限", list).save();
        main.ignoreView.adapter.reload();
        main.ignoreView.adapter.UpdateAdapter();
    }

    /**
     * 添加管理员权限
     */
    private void clickAdmin() {
        String string = editView.getText();
        if (string == null || string.isEmpty()) {
            JOptionPane.showMessageDialog(null, "请输入想要添加权限的计算机名！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        List<String> list = Main.getConfig().getList("管理员", new ArrayList<>());
        if (list.contains(string)) {
            JOptionPane.showMessageDialog(null, "该用户已有管理员权限！请悉知。", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Log.i("已为用户" + string + "添加管理员权限。");
        editView.setText("");
        JOptionPane.showMessageDialog(null, "操作完成！", "提示", JOptionPane.PLAIN_MESSAGE);
        list.add(string);
        Main.getConfig().set("管理员", list).save();
        main.adminView.adapter.reload();
        main.adminView.adapter.UpdateAdapter();
    }

    @Override
    public void start() {
        int buttonWidth = Math.max(100, Math.min(getWidth() / 5, 150));
        ignore.setSize(buttonWidth, getHeight());
        ignore.setLocation(getWidth() - ignore.getWidth() - 5, 0);
        ignore.start();
        admin.setSize(ignore.getSize());
        admin.setLocation(ignore.getX() - admin.getWidth() - 5, 0);
        admin.start();
        editView.setSize(admin.getX() - 5, getHeight());
    }
}
