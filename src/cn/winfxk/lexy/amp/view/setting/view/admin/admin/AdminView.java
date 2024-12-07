package cn.winfxk.lexy.amp.view.setting.view.admin.admin;

import cn.winfxk.lexy.amp.tool.view.MyJPanel;
import cn.winfxk.lexy.amp.tool.view.list.ListView;

import javax.swing.*;
import java.awt.*;

public class AdminView extends MyJPanel {
    private final static Font font = new Font("楷体", Font.BOLD, 25);
    public final AdminAdapter adapter;
    private final ListView listView;
    private final JLabel title;

    public AdminView() {
        super();
        title = new JLabel("管理权限清单");
        title.setOpaque(false);
        title.setFont(font);
        title.setLocation(0, 0);
        add(title);
        listView = new ListView(adapter = new AdminAdapter());
        add(listView);
    }

    @Override
    public void start() {
        title.setSize(getWidth(), 40);
        listView.setSize(getWidth(), getHeight() - title.getHeight());
        listView.setLocation(0, title.getHeight());
        adapter.UpdateAdapter();
    }
}
