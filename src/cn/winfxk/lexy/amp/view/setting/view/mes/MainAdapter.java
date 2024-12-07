package cn.winfxk.lexy.amp.view.setting.view.mes;

import cn.winfxk.lexy.amp.crucial.Crucial;
import cn.winfxk.lexy.amp.crucial.CrucialThread;
import cn.winfxk.lexy.amp.tool.view.list.adapter.BaseAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainAdapter extends BaseAdapter {
    private final List<Map.Entry<String, Crucial>> entryList = new ArrayList<>();
    private final Map<String, Crucial> map = new HashMap<>();

    public MainAdapter() {
        super();
        reload();
    }

    /**
     * 更新基础数据
     */
    private void reload() {
        map.clear();
        entryList.clear();
        map.putAll(CrucialThread.getCrucials());
        entryList.addAll(map.entrySet());
    }

    @Override
    public int getSize() {
        return entryList.size() + 1;
    }

    @Override
    public Map.Entry<String, Crucial> getItem(int location) {
        return location == 0 ? null : entryList.get(location - 1);
    }

    @Override
    public MainItemView getView(int location) {
        return new MainItemView(getItem(location));
    }
}
