package cn.winfxk.lexy.amp.view.setting.view.email.time;

import cn.winfxk.lexy.amp.Main;
import cn.winfxk.lexy.amp.tool.Tool;
import cn.winfxk.lexy.amp.tool.view.list.adapter.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class TimeAdapter extends BaseAdapter {
    private final List<String> list = new ArrayList<>();
    private static TimeAdapter adapter;

    public TimeAdapter() {
        super();
        adapter = this;
    }

    public static TimeAdapter getAdapter() {
        return adapter;
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
    public synchronized void UpdateAdapter() {
        if (isLoad()) return;
        reload();
        super.UpdateAdapter();
    }

    @Override
    public TimeItemView getView(int location) {
        return new TimeItemView(getItem(location));
    }

    public void reload() {
        list.clear();
        List<Object> list1 = Main.getConfig().getList("邮件发送时间时", new ArrayList<>());
        String string;
        for (Object obj : list1) {
            string = Tool.objToString(obj, null);
            if (string == null || string.isEmpty()) continue;
            list.add(string);
        }
    }
}
