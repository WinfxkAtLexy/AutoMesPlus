package cn.winfxk.lexy.amp.view.setting.res.view.make;

import cn.winfxk.lexy.amp.tool.view.OnclickListener;
import cn.winfxk.lexy.amp.tool.view.list.adapter.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class MakeAdapter extends BaseAdapter {
    private final List<String> list = new ArrayList<>();
    private final MakeTip main;

    public MakeAdapter(MakeTip main) {
        super();
        this.main = main;
    }

    @Override
    public int getSize() {
        return list.size();
    }

    @Override
    public OnclickListener getItem(int location) {
        return main.getMap().get(list.get(location));
    }

    @Override
    public ItemView getView(int location) {
        return new ItemView(list.get(location), getItem(location));
    }

    @Override
    public synchronized void UpdateAdapter() {
        if (isLoad()) return;
        list.clear();
        list.addAll(main.getMap().keySet());
        super.UpdateAdapter();
    }
}
