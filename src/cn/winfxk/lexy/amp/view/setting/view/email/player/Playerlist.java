package cn.winfxk.lexy.amp.view.setting.view.email.player;

import cn.winfxk.lexy.amp.tool.view.MyJPanel;
import cn.winfxk.lexy.amp.tool.view.list.ListView;
import cn.winfxk.lexy.amp.view.setting.view.email.time.Timelist;

import javax.swing.*;
import javax.swing.border.TitledBorder;

public class Playerlist extends MyJPanel {
    private static final TitledBorder border = BorderFactory.createTitledBorder("邮件接收人");
    private final PlayerAdapter adapter;
    private final ListView listView;
    private final EditView editView;

    static {
        border.setTitleFont(Timelist.borderFont);
    }

    public Playerlist() {
        super();
        setBorder(border);
        listView = new ListView(adapter = new PlayerAdapter());
        listView.setLocation(0, 30);
        add(listView);
        add(editView = new EditView());
    }

    @Override
    public void start() {
        editView.setSize(getWidth(), 50);
        editView.setLocation(0, getHeight() - editView.getHeight() - 10);
        editView.start();
        listView.setSize(getWidth(), editView.getY() - 20 - listView.getY());
        adapter.UpdateAdapter();
    }
}
