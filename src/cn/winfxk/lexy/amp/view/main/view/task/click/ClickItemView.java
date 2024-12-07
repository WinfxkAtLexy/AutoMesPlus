package cn.winfxk.lexy.amp.view.main.view.task.click;

import cn.winfxk.lexy.amp.tool.view.list.adapter.BaseItemView;
import cn.winfxk.lexy.amp.tool.view.list.adapter.ItemClickListener;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class ClickItemView extends BaseItemView {
    private static final Font font = new Font("宋体", Font.BOLD, 15);
    private final JLabel label;

    public ClickItemView(Map.Entry<String, ItemClickListener> item) {
        setItemClickListener(item.getValue());
        label = new JLabel(item.getKey(), SwingConstants.CENTER);
        label.setLocation(0, 0);
        label.setFont(font);
        add(label);
    }

    @Override
    public void start() {
        label.setSize(getWidth(), getHeight());
    }
}
