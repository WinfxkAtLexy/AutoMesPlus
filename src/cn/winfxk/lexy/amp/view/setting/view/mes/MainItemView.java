package cn.winfxk.lexy.amp.view.setting.view.mes;

import cn.winfxk.lexy.amp.crucial.Crucial;
import cn.winfxk.lexy.amp.tool.view.list.adapter.BaseItemView;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class MainItemView extends BaseItemView {
    private final static Font font = new Font("楷体", Font.BOLD, 20);
    private final JLabel label;

    public MainItemView(Map.Entry<String, Crucial> item) {
        super();
        label = new JLabel(item == null ? "添加型号" : item.getValue().getModel(), SwingConstants.CENTER);
        label.setFont(font);
        label.setOpaque(false);
        label.setLocation(0, 0);
        setItemClickListener(view -> new Keylist(item != null ? item.getValue() : null).start());
        add(label);
    }

    @Override
    public void start() {
        label.setSize(getSize());
    }
}
