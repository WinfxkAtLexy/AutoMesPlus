package cn.winfxk.lexy.amp.view.setting.view.email.player;

import cn.winfxk.lexy.amp.Main;
import cn.winfxk.lexy.amp.tool.Tool;
import cn.winfxk.lexy.amp.tool.view.list.adapter.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class PlayerAdapter extends BaseAdapter {
    private final List<String> list = new ArrayList<>();
    private static PlayerAdapter adapter;

    public PlayerAdapter() {
        super();
        reload();
        adapter = this;
    }

    @Override
    public int getSize() {
        return list.size();
    }

    @Override
    public String getItem(int location) {
        return list.get(location);
    }

    @Override
    public PlayerItemView getView(int location) {
        return new PlayerItemView(getItem(location));
    }

    @Override
    public synchronized void UpdateAdapter() {
        if (isLoad()) return;
        reload();
        super.UpdateAdapter();
    }

    private void reload() {
        list.clear();
        List<Object> array = Main.getConfig().getList("预警邮件接收清单", new ArrayList<>());
        if (array == null) return;
        String string;
        for (Object s : array) {
            if (s == null) continue;
            string = Tool.objToString(s, null);
            if (string == null || string.isEmpty()) continue;
            list.add(string);
        }
    }

    public static PlayerAdapter getAdapter() {
        return adapter;
    }
}
