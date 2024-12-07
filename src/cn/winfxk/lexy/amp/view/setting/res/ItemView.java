package cn.winfxk.lexy.amp.view.setting.res;

import cn.winfxk.lexy.amp.tool.view.OnclickListener;
import cn.winfxk.lexy.amp.tool.view.list.adapter.BaseItemView;

import javax.swing.*;
import java.awt.*;

public class ItemView extends BaseItemView {
    private static final Font font = new Font("楷体", Font.BOLD, 20);
    private final JLabel label;

    public ItemView(OnclickListener item, String title) {
        super();
        label = new JLabel(title, SwingConstants.CENTER);
        label.setOpaque(false);
        label.setFont(font);
        label.setLocation(0, 0);
        add(label);
        setOnclickListener(item);
    }

    @Override
    public void start() {
        label.setSize(getSize());
    }
}
