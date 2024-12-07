package cn.winfxk.lexy.amp.view.main.view;

import cn.winfxk.lexy.amp.tool.view.MyJPanel;
import cn.winfxk.lexy.amp.view.main.Panel;
import cn.winfxk.lexy.amp.view.main.view.task.TaskAdapter;
import cn.winfxk.lexy.amp.view.main.view.task.TaskItemView;

import javax.swing.*;

import java.awt.*;

import static cn.winfxk.lexy.amp.view.main.view.task.TaskItemView.TitleFont;
import static cn.winfxk.lexy.amp.view.main.view.task.TaskItemView.buttonFont;

public class AllTaskData extends MyJPanel {
    private final JButton S1500, OkCount, NgCount, AllCount, perature;
    public static AllTaskData main;

    public static AllTaskData getInstance() {
        return main;
    }

    public AllTaskData() {
        super();
        setOpaque(true);
        main = this;
        S1500 = new JButton();
        S1500.setLocation(0, 0);
        S1500.setFont(buttonFont);
        S1500.setBackground(Panel.Background);
        add(S1500);
        OkCount = new JButton();
        OkCount.setFont(buttonFont);
        add(OkCount);
        NgCount = new JButton();
        NgCount.setFont(buttonFont);
        add(NgCount);
        AllCount = new JButton();
        AllCount.setFont(buttonFont);
        add(AllCount);
        perature = new JButton();
        perature.setFont(TitleFont);
        add(perature);
    }

    @Override
    public void start() {
        TaskItemView.ItemData data = TaskItemView.itemData;
        S1500.setSize(data.okX, getHeight());
        OkCount.setSize(data.okSize);
        OkCount.setLocation(data.okX, 0);
        NgCount.setSize(data.ngSize);
        NgCount.setLocation(data.ngX, 0);
        AllCount.setSize(data.allSize);
        AllCount.setLocation(data.allX, 0);
        perature.setSize(data.perSize);
        perature.setLocation(data.perX, 0);
    }

    public void onCompleted(TaskAdapter adapter) {
        start();
        int allItem = 0, ngitem = 0, okitem = 0;
        int s1500Count = 0;
        for (TaskItemView view : adapter.getItemViews()) {
            allItem += (view.getInspectionl().getCrucial() != null ? view.getInspectionl().getCrucial().getAllItems().size() : 0);
            ngitem += view.getInspectionl().getNgItem().size();
            okitem += view.getInspectionl().getOkItem().size();
            s1500Count++;
        }
        S1500.setText("汇总：共计" + s1500Count + "单");
        OkCount.setBackground(Panel.Background);
        OkCount.setText(okitem + "");
        NgCount.setText(ngitem + "");
        NgCount.setBackground(ngitem < 1 ? Panel.Background : Color.yellow);
        AllCount.setText(allItem + "");
        AllCount.setBackground(Panel.Background);
        perature.setText(allItem == 0 ? "/" : TaskItemView.getPer(allItem, okitem));
        perature.setBackground(NgCount.getBackground());
        updateUI();
    }

    @Override
    public void updateUI() {
        if (S1500 != null) S1500.updateUI();
        if (OkCount != null) OkCount.updateUI();
        if (NgCount != null) NgCount.updateUI();
        if (AllCount != null) AllCount.updateUI();
        if (perature != null) perature.updateUI();
        super.updateUI();
    }
}
