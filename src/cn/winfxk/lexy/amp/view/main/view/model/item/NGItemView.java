package cn.winfxk.lexy.amp.view.main.view.model.item;

import cn.winfxk.lexy.amp.crucial.CrucialItem;
import cn.winfxk.lexy.amp.tool.view.list.adapter.BaseItemView;

import javax.swing.*;
import java.awt.*;

public class NGItemView extends BaseItemView {
    public final static Font buttonFont = new Font("楷体", Font.BOLD, 20);
    private final JButton label;

    public NGItemView(CrucialItem item) {
        super();
        label = new JButton(item == null ? "未检" : item.getName());
        label.setFont(buttonFont);
        label.setOpaque(true);
        label.setBackground(Color.yellow);
        add(label, CENTER_ALIGNMENT);
    }

    @Override
    public void start() {
        label.setSize(getSize());
    }
}
