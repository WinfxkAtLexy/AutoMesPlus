package cn.winfxk.lexy.amp.view.setting.view.admin.admin;

import cn.winfxk.lexy.amp.tool.view.list.adapter.BaseItemView;
import cn.winfxk.lexy.amp.tool.view.list.adapter.ItemClickListener;

import javax.swing.*;
import java.awt.*;

public class ItemView extends BaseItemView {
    private static final Font font = new Font("楷体", Font.PLAIN, 20);
    private final JLabel label;

    public ItemView(String title, ItemClickListener listener) {
        label = new JLabel(title, SwingConstants.CENTER);
        label.setOpaque(false);
        label.setFont(font);
        label.setLocation(0, 0);
        add(label);
        setItemClickListener(listener);
    }

    @Override
    public void start() {
        label.setSize(getSize());
    }
}
