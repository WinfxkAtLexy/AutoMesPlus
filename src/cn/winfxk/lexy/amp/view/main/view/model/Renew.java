package cn.winfxk.lexy.amp.view.main.view.model;

import cn.winfxk.lexy.amp.Log;
import cn.winfxk.lexy.amp.Main;
import cn.winfxk.lexy.amp.tool.Tool;
import cn.winfxk.lexy.amp.view.AutoMes;
import cn.winfxk.lexy.amp.view.main.view.Progress;
import cn.winfxk.lexy.amp.view.main.view.task.TaskAdapter;
import cn.winfxk.lexy.amp.view.main.view.task.TaskItemView;
import cn.winfxk.lexy.amp.view.main.view.task.TitleView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Renew extends Thread {
    private final OkItemAdapter okItemAdapter;
    private final NgItemAdapter ngItemAdapter;
    private List<TaskItemView> list;
    private boolean isInit = false;
    private TaskItemView view;
    private static Renew main;
    private int whileTime = 0;
    private long time = 0;
    private int index = 0;

    public Renew(OkItemAdapter okItemAdapter, NgItemAdapter ngItemAdapter) {
        super();
        main = this;
        this.okItemAdapter = okItemAdapter;
        this.ngItemAdapter = ngItemAdapter;
    }

    /**
     * 执行更新操作
     */
    private void reload() {
        if (time != TaskAdapter.getInstance().getTime()) {
            list = new ArrayList<>(TaskAdapter.getInstance().getItemViews());
            time = TaskAdapter.getInstance().getTime();
            index = 0;
        }
        if (time == 0) return;
        if (list.isEmpty()) {
            time = 0;
            return;
        }
        if (index >= list.size()) {
            TaskAdapter.pageDown();
            return;
        }
        TaskItemView view = list.get(index);
        Log.i("更新型号明细" + view.getTask().S1500 + " / " + view.getTask().Model);
        cn.winfxk.lexy.amp.view.main.view.model.TitleView.setTitleString(view.getTask().S1500 + " / " + view.getTask().Model);
        okItemAdapter.UpdateAdapter(view.getInspectionl());
        ngItemAdapter.UpdateAdapter(view.getInspectionl());
        if (this.view != null) {
            this.view.getS1500().setOpaque(false);
            this.view.getS1500().updateUI();
        }
        isInit = true;
        view.getS1500().setOpaque(true);
        view.getS1500().setBackground(Color.GRAY);
        view.getS1500().updateUI();
        this.view = view;
        ++index;
    }

    /**
     * 点击下一页要执行的操作
     */
    protected static void clickPageDown() {
        main.whileTime = Math.max(5, Main.getConfig().getInt("型号明细更新间隔", 5));
        main.reload();
    }

    /**
     * 点击上一页要执行的操作
     */
    protected static void clickPageUp() {
        main.index = main.index - 2;
        main.whileTime = Math.max(5, Main.getConfig().getInt("型号明细更新间隔", 5));
        if (main.index <= 0) TaskAdapter.pageUp();
        main.reload();
    }

    @Override
    public void run() {
        int maxTIme = Math.max(5, Main.getConfig().getInt("型号明细更新间隔", 5));
        Progress.getModelProgres().setMax(maxTIme * 1000);
        Progress.getTaskProgres().setMax((list == null ? 0 : list.size()) * maxTIme * 1000);
        while (Main.runing) {
            for (int i = 0; i < 1000; i++) {
                Tool.sleep(1);
                if (!Main.runing) break;
                if (!TitleView.isLock()) continue;
                Progress.getModelProgres().setCurrent(((maxTIme - whileTime) * 1000) + i);
                Progress.getTaskProgres().setCurrent((index * maxTIme) * 1000 + Progress.getModelProgres().getCurrent());
            }
            if (!Main.runing) break;
            if (AutoMes.isIsSleep() || !TitleView.isLock()) continue;
            if (isInit && whileTime-- > 0) continue;
            maxTIme = Math.max(5, Main.getConfig().getInt("型号明细更新间隔", 5));
            Progress.getModelProgres().setMax((whileTime = maxTIme) * 1000);
            Progress.getTaskProgres().setMax((list == null ? 0 : list.size()) * maxTIme * 1000);
            reload();
        }
        super.run();
    }
}
