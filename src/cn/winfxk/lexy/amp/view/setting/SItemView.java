package cn.winfxk.lexy.amp.view.setting;

import cn.winfxk.lexy.amp.tool.view.list.adapter.BaseItemView;
import cn.winfxk.lexy.amp.tool.view.list.adapter.ItemClickListener;

import javax.swing.*;
import java.awt.*;

public class SItemView extends BaseItemView {
    public final static Font font = new Font("楷体", Font.BOLD, 20);
    private final JLabel label;

    public SItemView(String title, ItemClickListener listener) {
        super();
        setItemClickListener(listener);
        label = new JLabel(title, SwingConstants.CENTER);
        label.setLocation(0, 0);
        label.setOpaque(false);
        label.setFont(font);
        label.addMouseListener(this);
        add(label);
    }

    @Override
    public void start() {
        label.setSize(getWidth(), getHeight());
    }
}
