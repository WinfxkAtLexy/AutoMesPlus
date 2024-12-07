package cn.winfxk.lexy.amp.view.main.view;

import cn.winfxk.lexy.amp.crucial.CrucialThread;
import cn.winfxk.lexy.amp.excel.ExcelFactory;
import cn.winfxk.lexy.amp.tool.Tool;
import cn.winfxk.lexy.amp.tool.view.MyJPanel;
import cn.winfxk.lexy.amp.tool.view.list.ListView;
import cn.winfxk.lexy.amp.view.AutoMes;
import cn.winfxk.lexy.amp.view.main.view.task.SearchAdapter;
import cn.winfxk.lexy.amp.view.main.view.task.TaskAdapter;
import cn.winfxk.lexy.amp.view.main.view.task.TitleView;

import javax.swing.*;
import java.io.File;

/**
 * 一个用于显示滚动计划的容器
 */
public class Tasklist extends MyJPanel {
    private volatile transient boolean isInit = true;
    public final SearchAdapter searchAdapter;
    private final AllTaskData allTaskData;
    private final TitleView titleView;
    public final TaskAdapter adapter;
    private final ListView listView;
    private static Tasklist main;
    private int MaxHeight;
    private boolean isMax;

    public static Tasklist getMain() {
        return main;
    }

    /**
     * 一个用于显示滚动计划的容器
     */
    public Tasklist() {
        super();
        main = this;
        titleView = new TitleView();
        titleView.setLocation(0, 0);
        add(titleView);
        add(listView = new ListView(adapter = new TaskAdapter(this)));
        listView.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        listView.setBorder(BorderFactory.createEmptyBorder());
        allTaskData = new AllTaskData();
        adapter.setUpdateCompletedListener(adapter1 -> {
            titleView.pageUp.setEnabled(true);
            titleView.pageDown.setEnabled(true);
            allTaskData.onCompleted(adapter);
        });
        adapter.setStartUpdatingListener(adapter1 -> {
            titleView.pageUp.setEnabled(false);
            titleView.pageDown.setEnabled(false);
        });
        listView.setOpaque(false);
        searchAdapter = new SearchAdapter();
    }

    public ListView getListView() {
        return listView;
    }

    @Override
    public void setSize(int width, int height) {
        MaxHeight = height;
        super.setSize(width, height);
    }

    public int getMaxHeight() {
        return MaxHeight;
    }

    public void reloadAllTaskData(boolean isMax, int height) {
        AllTaskData data = AllTaskData.getInstance();
        Tool.sleep(100);
        if (isMax != this.isMax) {
            if (this.isMax) remove(data);
            else AutoMes.removeView(data);
            this.isMax = isMax;
        }
        Tool.sleep(100);
        if (isMax) {
            add(data);
            data.setLocation(getWidth() / 300, height + adapter.getItemHeight() + 10);
            data.setSize(listView.getWidth(), adapter.getItemHeight());
            data.start();
        } else AutoMes.reload(data);
    }
    @Override
    public void start() {
        titleView.setSize(getWidth(), 50);
        titleView.start();
        listView.setLocation(0, titleView.getY() + titleView.getHeight());
        listView.setSize(getWidth(), getHeight() - titleView.getHeight());
        titleView.start();
        adapter.setItemHeight(Math.min(50, Math.max(getHeight() / 24, 40)));
        searchAdapter.setItemHeight(adapter.getItemHeight());
        if (isInit) new Thread(() -> {
            Tool.sleep(1200);
            updateAdapter();
            isInit = false;
        }).start();
        else updateAdapter();
    }

    private void updateAdapter() {
        long time = System.currentTimeMillis();
        File file = ExcelFactory.getTaskFile(time);
        if (file.exists() && !CrucialThread.isEmpty()) {
            long whileTime = time;
            int index = 0;
            while (!adapter.UpdateAdapter(whileTime)) {
                Tool.sleep(100);
                whileTime += 86400000L;
                if (index++ > 15) {
                    JOptionPane.showMessageDialog(null, "无法显示近期计划！因为最近所有的计划都读取失败！请检查。", "错误", JOptionPane.ERROR_MESSAGE);
                    break;
                }
            }
            adapter.UpdateAdapter(whileTime);
        } else new Thread(() -> {
            while (!file.exists() || CrucialThread.isEmpty()) Tool.sleep(100);
            long whileTime = time;
            int index = 0;
            while (!adapter.UpdateAdapter(whileTime)) {
                Tool.sleep(100);
                whileTime += 86400000L;
                if (index++ > 15) {
                    JOptionPane.showMessageDialog(null, "无法显示近期计划！因为最近所有的计划都读取失败！请检查。", "错误", JOptionPane.ERROR_MESSAGE);
                    break;
                }
            }
            adapter.UpdateAdapter(whileTime);
        }).start();
    }
}
