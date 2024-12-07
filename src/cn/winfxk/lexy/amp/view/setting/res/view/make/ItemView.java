package cn.winfxk.lexy.amp.view.setting.res.view.make;

import cn.winfxk.lexy.amp.tool.view.OnclickListener;
import cn.winfxk.lexy.amp.tool.view.list.adapter.BaseItemView;

import javax.swing.*;
import java.awt.*;

public class ItemView extends BaseItemView {
    private final static Font font = new Font("楷体", Font.BOLD, 20);
    private final JLabel label;

    public ItemView(String title, OnclickListener listener) {
        super();
        label = new JLabel(title, SwingConstants.CENTER);
        label.setFont(font);
        label.setOpaque(false);
        label.setLocation(0, 0);
        add(label);
        setOnclickListener(listener);
    }

    @Override
    public void start() {
        label.setSize(getSize());
    }
}
