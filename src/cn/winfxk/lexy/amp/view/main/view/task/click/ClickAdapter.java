package cn.winfxk.lexy.amp.view.main.view.task.click;

import cn.winfxk.lexy.amp.tool.view.list.adapter.BaseAdapter;
import cn.winfxk.lexy.amp.tool.view.list.adapter.ItemClickListener;
import cn.winfxk.lexy.amp.view.main.view.task.TipView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClickAdapter extends BaseAdapter {
    private final List<Map.Entry<String, ItemClickListener>> list;

    public ClickAdapter(TipView main) {
        list = new ArrayList<>(main.getOnClickListeners().entrySet());
    }

    @Override
    public int getSize() {
        return list.size();
    }

    @Override
    public Map.Entry<String, ItemClickListener> getItem(int location) {
        return list.get(location);
    }

    @Override
    public ClickItemView getView(int location) {
        return new ClickItemView(getItem(location));
    }
}
