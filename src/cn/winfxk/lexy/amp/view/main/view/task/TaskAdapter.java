package cn.winfxk.lexy.amp.view.main.view.task;

import cn.winfxk.lexy.amp.Log;
import cn.winfxk.lexy.amp.Main;
import cn.winfxk.lexy.amp.excel.ExcelFactory;
import cn.winfxk.lexy.amp.excel.factory.Task;
import cn.winfxk.lexy.amp.mes.MesItem;
import cn.winfxk.lexy.amp.mes.Serialization;
import cn.winfxk.lexy.amp.tool.view.JOptionPane;
import cn.winfxk.lexy.amp.tool.view.list.adapter.BaseAdapter;
import cn.winfxk.lexy.amp.tool.view.list.adapter.UpdateCompletedListener;
import cn.winfxk.lexy.amp.view.main.view.Tasklist;

import javax.swing.*;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

public class TaskAdapter extends BaseAdapter implements UpdateCompletedListener {
    public static final SimpleDateFormat TaskTitle = new SimpleDateFormat("MM/dd");
    private final List<TaskItemView> itemViews = new ArrayList<>();
    private final List<TaskItemView> AllViews = new ArrayList<>();
    private final static List<Task> tasks = new ArrayList<>();
    private final List<MesItem> mesItems = new ArrayList<>();
    private static TaskAdapter main;
    private final Tasklist tasklist;
    private long time;
    private int height;

    public TaskAdapter(Tasklist tasklist) {
        super();
        main = this;
        this.tasklist = tasklist;
        setUpdateCompletedListener(this);
    }

    /**
     * @return 返回对外接口
     */
    public static TaskAdapter getInstance() {
        return main;
    }

    @Override
    public int getSize() {
        return tasks.size() + 1;
    }

    @Override
    public Task getItem(int location) {
        return location == 0 ? null : tasks.get(location - 1);
    }

    @Override
    public TaskItemView getView(int location) {
        TaskItemView view;
        if (location != 0) {
            view = new TaskItemView(getItem(location), mesItems);
            AllViews.add(view);
            if (view.getInspectionl().getOkItem().size() + view.getInspectionl().getNgItem().size() > 0)
                itemViews.add(view);
        } else view = new TaskItemView(null, mesItems);
        return view;
    }

    /**
     * 根据提供的日期来获取并显示这个日期的滚动计划
     *
     * @param time 需要提取并显示滚动计划的日期时间戳
     */
    public synchronized boolean UpdateAdapter(long time) {
        if (isLoad()) return false;
        Log.i("正在获取" + ExcelFactory.Filename.format(new Date(time)) + "的滚动计划");
        File file = ExcelFactory.getTaskFile(time);
        if (!file.exists()) {
            Log.w("想要获取的计划不存在！请检查是否已经超出已转储的计划范围.");
            return false;
        }
        List<Task> list = ExcelFactory.getTaskByFile(file);
        if (list == null || list.isEmpty()) {
            Log.e("无法显示该日期的滚动计划！因为该日期的滚动计划不存在！");
            return false;
        }
        tasks.clear();
        tasks.addAll(list);
        Log.i(ExcelFactory.Filename.format(new Date(time)) + "的滚动计划获取完毕！准备解析MES数据和关键零部件清单数据。");
        long endTime = System.currentTimeMillis();
        int timeLength = Math.min(3, Math.max(Main.getConfig().getInt("MES抓取距离", 1), 1));
        long startTime = time - (86400000L * 31 * timeLength);
        Map<Long, List<MesItem>> map = Serialization.getMesItemByTime(startTime, endTime);
        List<MesItem> mesItems = new ArrayList<>();
        while (mesItems.isEmpty())
            for (List<MesItem> list1 : map.values()) {
                if (list1 == null || list1.isEmpty()) continue;
                mesItems.addAll(list1);
            }
        this.mesItems.clear();
        this.time = time;
        this.mesItems.addAll(mesItems);
        for (MesItem mesItem : mesItems) {
            System.out.println(mesItem.getS1500()+" "+mesItem.getName()+" "+mesItem.getCode()+" "+mesItem.getImageCode());
        }
        TitleView.setTitleText(TaskTitle.format(new Date(time)));
        Log.i("各数据解析完毕！准备更新滚动计划表视图！总计划项数：" + tasks.size());
        this.UpdateAdapter();
        return true;
    }

    @Override
    public synchronized void UpdateAdapter() {
        this.itemViews.clear();
        this.AllViews.clear();
        super.UpdateAdapter();
    }

    public List<TaskItemView> getAllViews() {
        return AllViews;
    }

    /**
     * 执行上一页操作
     */
    public static void pageUp() {
        Mapclas mapclas = getTaskTimes();
        long time = main.getTime();
        String thisDate = TaskTitle.format(new Date(time));
        if (!mapclas.map.containsKey(thisDate) && TitleView.isLock()) {
            main.UpdateAdapter(mapclas.vlist.get(0));
            return;
        }
        int index;
        if (TitleView.isLock()) {
            int day = Math.max(3, Main.getConfig().getInt("显示计划天数", 3));
            index = mapclas.keylist.indexOf(thisDate);
            if (index == -1 || index > day) main.UpdateAdapter(mapclas.vlist.get(0));
            else if (index == 0)
                main.UpdateAdapter(mapclas.vlist.get(day >= mapclas.map.size() ? mapclas.keylist.size() - 1 : day));
            else {
                index -= 1;
                if (index <= 0) index = day >= mapclas.map.size() ? mapclas.keylist.size() - 1 : day;
                main.UpdateAdapter(mapclas.vlist.get(index));
            }
        } else {
            File file = ExcelFactory.getTaskFile(time -= 86400000L);
            int error = 0;
            while (!file.exists()) {
                file = ExcelFactory.getTaskFile(time -= 86400000L);
                if (error++ > 20) break;
            }
            if (file.exists())
                main.UpdateAdapter(time);
            else {
                JOptionPane.showMessageDialog(null, "已无更多计划！");
            }
        }
    }

    /**
     * 执行下一页操作
     */
    public static void pageDown() {
        Mapclas mapclas = getTaskTimes();
        long time = main.getTime();
        String thisDate = TaskTitle.format(new Date(time));
        if (!mapclas.map.containsKey(thisDate)) {
            if (mapclas.vlist.isEmpty()) {
                Log.e("程序运行异常(可能是运行在未初始化完成的程序上)！请尝试重启后使用。");
                JOptionPane.showMessageDialog(null, "程序运行异常！请尝试重启后使用。", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            main.UpdateAdapter(mapclas.vlist.get(0));
            return;
        }
        int index = mapclas.keylist.indexOf(thisDate) + 1;
        int day = Math.max(3, Main.getConfig().getInt("显示计划天数", 3));
        if ((day < index && TitleView.isLock()) || index >= mapclas.map.size()) index = 0;
        main.UpdateAdapter(mapclas.vlist.get(index));
    }

    /**
     * @return 返回从当天开始往后所有的计划时间表[格式化的时间文本：时间戳]
     */
    public static Mapclas getTaskTimes() {
        Mapclas mapclas = new Mapclas();
        long Time = System.currentTimeMillis();
        File file = ExcelFactory.getTaskFile(Time);
        int error = 0;
        while (Main.runing) {
            if (file.exists()) {
                error = 0;
                mapclas.map.put(TaskTitle.format(Time), Time);
                mapclas.keylist.add(TaskTitle.format(Time));
                mapclas.vlist.add(Time);
            }
            Time += 86400000L;
            file = ExcelFactory.getTaskFile(Time);
            if (error++ > 25) break;
        }
        return mapclas;
    }

    /**
     * @return 返回现在显示的滚动计划清单
     */
    public List<TaskItemView> getItemViews() {
        return itemViews;
    }

    /**
     * @param time 设置滚动计划显示时间
     */
    public void setTime(long time) {
        this.time = time;
    }

    /**
     * @return 返回当前滚动计划的时间
     */
    public long getTime() {
        return time;
    }

    @Override
    public void onCompleted(BaseAdapter adapter) {
        height = getItemHeight() * getSize();
        tasklist.getListView().setVerticalScrollBarPolicy(height > tasklist.getListView().getHeight() ?
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED : ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        tasklist.reloadAllTaskData((tasklist.getMaxHeight() > height + getItemHeight() + 11), height);
    }

    public int getViewHeight() {
        return height;
    }

    /**
     * 一个用来封装提取数据的类
     */
    public static class Mapclas {
        public final LinkedHashMap<String, Long> map = new LinkedHashMap<>();
        public final ArrayList<String> keylist = new ArrayList<>();
        public final ArrayList<Long> vlist = new ArrayList<>();
    }
}
