package cn.winfxk.lexy.amp.tool.view.list.adapter;

import cn.winfxk.lexy.amp.tool.Tool;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ArrayAdapter extends BaseAdapter {
    private final List<Map.Entry<?, ItemClickListener>> list;

    public ArrayAdapter(Map<?, ItemClickListener> list) {
        this.list = new ArrayList<>(list.entrySet());
    }

    @Override
    public int getSize() {
        return list.size();
    }

    @Override
    public Map.Entry<?, ItemClickListener> getItem(int location) {
        return list.get(location);
    }

    @Override
    public ItemView getView(int location) {
        Map.Entry<?, ItemClickListener> entry = getItem(location);
        return new ItemView(entry.getKey(), entry.getValue());
    }

    public static class ItemView extends BaseItemView {
        private final static Font font = new Font("楷体", Font.BOLD, 15);
        private final JLabel label;

        public ItemView(Object obj, ItemClickListener listener) {
            label = new JLabel(Tool.objToString(obj, "null"), SwingConstants.CENTER);
            label.setLocation(0, 0);
            label.setFont(font);
            label.setOpaque(false);
            add(label, CENTER_ALIGNMENT);
            setItemClickListener(listener);
        }

        @Override
        public void start() {
            label.setSize(getSize());
        }
    }
}
