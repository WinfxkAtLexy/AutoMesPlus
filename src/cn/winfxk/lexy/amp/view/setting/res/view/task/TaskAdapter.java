package cn.winfxk.lexy.amp.view.setting.res.view.task;

import cn.winfxk.lexy.amp.Log;
import cn.winfxk.lexy.amp.excel.ExcelFactory;
import cn.winfxk.lexy.amp.tool.view.list.adapter.BaseAdapter;
import cn.winfxk.lexy.amp.view.setting.res.view.Task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TaskAdapter extends BaseAdapter {
    private static final SimpleDateFormat TaskFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat TitleFormat = new SimpleDateFormat("yyyy-MM");
    private final List<Data> list = new ArrayList<>();
    private final Task task;

    public TaskAdapter(Task task) {
        super();
        this.task = task;
    }

    @Override
    public int getSize() {
        return list.size();
    }

    @Override
    public Data getItem(int location) {
        return list.get(location);
    }

    @Override
    public ItemView getView(int location) {
        return new ItemView(getItem(location));
    }

    @Override
    public synchronized void UpdateAdapter() {
        super.UpdateAdapter();
    }

    public synchronized boolean UpdateAdapter(long time) {
        if (isLoad()) return false;
        if (!reload(time)) return false;
        super.UpdateAdapter();
        return true;
    }

    public boolean reload(long time) {
        list.clear();
        Date date = new Date(time);
        String title = TitleFormat.format(date);
        task.getPageView().setText(TitleFormat.format(date));
        try {
            Date startTime = TaskFormat.parse(title + "-01");
            long startTiemms = startTime.getTime();
            long endTimems = startTiemms;
            String MMdate = TitleFormat.format(endTimems);
            while (title.equals(MMdate))
                MMdate = TitleFormat.format(endTimems += 86400000L);
            endTimems -= 86400000L;
            Data data;
            for (long i = startTiemms; i < endTimems; i += 86400000L) {
                data = new Data();
                data.file = ExcelFactory.getTaskFile(i);
                if (!data.file.exists()) continue;
                data.list = ExcelFactory.getTaskByFile(data.file);
                if (data.list == null || data.list.isEmpty()) continue;
                data.time = i;
                list.add(data);
            }
            return true;
        } catch (ParseException e) {
            Log.e("反序列化日期时出现问题！", e);
        }
        return false;
    }
}
