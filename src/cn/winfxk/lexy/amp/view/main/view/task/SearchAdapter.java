package cn.winfxk.lexy.amp.view.main.view.task;

import cn.winfxk.lexy.amp.Log;
import cn.winfxk.lexy.amp.crucial.Crucial;
import cn.winfxk.lexy.amp.crucial.CrucialItem;
import cn.winfxk.lexy.amp.excel.factory.Task;
import cn.winfxk.lexy.amp.tool.view.JOptionPane;
import cn.winfxk.lexy.amp.tool.view.list.adapter.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends BaseAdapter {
    private static final List<TaskItemView> search = new ArrayList<>();
    private static final List<TaskItemView> all = new ArrayList<>();

    public SearchAdapter() {
        super();
        setUpdateCompletedListener(adapter -> TitleView.getMain().setSearchButtonEnabled(true));
    }

    @Override
    public int getSize() {
        return search.size() + 1;
    }

    @Override
    public TaskItemView getItem(int location) {
        return location == 0 ? null : search.get(location - 1);
    }

    @Override
    public TaskItemView getView(int location) {
        return location == 0 ? new TaskItemView(null, null) : getItem(location);
    }

    public static boolean reload(String key) {
        Log.i("搜索关键字：" + key);
        TitleView.getMain().setSearchButtonEnabled(false);
        key = key.toLowerCase();
        all.clear();
        all.addAll(TaskAdapter.getInstance().getAllViews());
        search.clear();
        Task task;
        Crucial crucial;
        for (TaskItemView view : all) {
            task = view.getTask();
            if (task == null) continue;
            if (task.S1500 != null && task.S1500.toLowerCase().contains(key)) {
                search.add(view);
                continue;
            }
            if (task.Model != null && task.Model.toLowerCase().contains(key)) {
                search.add(view);
                continue;
            }
            if (view.getInspectionl() == null) continue;
            crucial = view.getInspectionl().getCrucial();
            if (crucial == null) continue;
            for (CrucialItem item : new ArrayList<>(crucial.getAllItems())) {
                if (item.getName() != null && item.getName().toLowerCase().contains(key)) {
                    search.add(view);
                    break;
                }
                if (item.getCode() != null && ListOnString(key, item.getCode())) {
                    search.add(view);
                    break;
                }
            }
        }
        if (search.isEmpty()) {
            Log.w("搜索不到任何关于" + key + "的内容");
            JOptionPane.showMessageDialog(null, "找不到任何关于" + key + "的内容！", "提示", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private static boolean ListOnString(String string, List<String> list) {
        for (String s : list)
            if (s != null && s.toLowerCase().contains(string))
                return true;
        return false;
    }

    @Override
    public synchronized void UpdateAdapter() {
        if (isLoad()) return;
        super.UpdateAdapter();
    }
}
