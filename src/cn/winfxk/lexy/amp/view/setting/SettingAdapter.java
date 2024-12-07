package cn.winfxk.lexy.amp.view.setting;

import cn.winfxk.lexy.amp.tool.view.list.adapter.BaseAdapter;
import cn.winfxk.lexy.amp.tool.view.list.adapter.ItemClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class SettingAdapter extends BaseAdapter {
    private final List<Map.Entry<String, ItemClickListener>> list = new ArrayList<>(Settinglist.map.entrySet());

    @Override
    public int getSize() {
        return list.size();
    }

    @Override
    public Map.Entry<String, ItemClickListener> getItem(int location) {
        return list.get(location);
    }

    @Override
    public SItemView getView(int location) {
        Map.Entry<String, ItemClickListener> entry = getItem(location);
        return new SItemView(entry.getKey(), entry.getValue());
    }
}
