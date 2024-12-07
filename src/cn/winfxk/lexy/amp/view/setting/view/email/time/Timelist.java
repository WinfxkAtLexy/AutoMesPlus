package cn.winfxk.lexy.amp.view.setting.view.email.time;

import cn.winfxk.lexy.amp.tool.view.MyJPanel;
import cn.winfxk.lexy.amp.tool.view.list.ListView;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class Timelist extends MyJPanel {
    private static final TitledBorder border = BorderFactory.createTitledBorder("邮件发送时间");
    public static final Font borderFont = new Font("楷体", Font.BOLD, 15);
    private final TimeAdapter adapter;
    private final ListView listView;

    private final EditView editView;

    static {
        border.setTitleFont(borderFont);
    }

    public Timelist() {
        super();
        setBorder(border);
        listView = new ListView(adapter = new TimeAdapter());
        listView.setLocation(0, 30);
        add(listView);
        add(editView = new EditView());
    }

    @Override
    public void start() {
        editView.setSize(getWidth(), 50);
        editView.setLocation(0, getHeight() - editView.getHeight() - 10);
        editView.start();
        listView.setSize(getWidth(), editView.getY() - 10 - listView.getY());
        adapter.UpdateAdapter();
    }
}
