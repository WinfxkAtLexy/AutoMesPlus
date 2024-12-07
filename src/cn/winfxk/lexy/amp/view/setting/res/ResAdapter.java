package cn.winfxk.lexy.amp.view.setting.res;

import cn.winfxk.lexy.amp.tool.view.OnclickListener;
import cn.winfxk.lexy.amp.tool.view.list.adapter.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class ResAdapter extends BaseAdapter {
    private final List<String> list = new ArrayList<>();

    @Override
    public int getSize() {
        return list.size();
    }

    @Override
    public OnclickListener getItem(int location) {
        return Res.map.get(list.get(location));
    }

    @Override
    public ItemView getView(int location) {
        return new ItemView(getItem(location), list.get(location));
    }

    @Override
    public synchronized void UpdateAdapter() {
        if (isLoad()) return;
        list.clear();
        list.addAll(Res.map.keySet());
        super.UpdateAdapter();
    }
}
