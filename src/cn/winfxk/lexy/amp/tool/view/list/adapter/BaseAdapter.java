package cn.winfxk.lexy.amp.tool.view.list.adapter;

import cn.winfxk.lexy.amp.tool.view.list.ListView;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter {
    private final List<UpdateCompletedListener> updateCompletedListener = new ArrayList<>();
    private final List<StartUpdatingListener> startUpdatingListener = new ArrayList<>();
    private volatile transient boolean isLoad = false;
    private static final int ItemHeight = 50;
    private int itemHeight = ItemHeight;
    private ListView listView;
    private JPanel panel;

    public void setItemHeight(int itemHeight) {
        this.itemHeight = itemHeight;
    }

    public int getItemHeight() {
        return itemHeight;
    }

    public boolean isLoad() {
        return isLoad;
    }

    public synchronized void UpdateAdapter() {
        if (isLoad) return;
        isLoad = true;
        for (StartUpdatingListener listener : startUpdatingListener)
            if (listener != null) listener.onStartUpdating(this);
        new Thread(() -> {
            panel.removeAll();
            BaseItemView view;
            int height = 0;
            for (int i = 0; i < getSize(); i++) {
                view = getView(i);
                view.setID(getID(i));
                view.setItem(getItem(i));
                if (!view.isCustomSize()) view.setSize(panel.getWidth() - 2, getItemHeight());
                view.start();
                if (listView.getItemClickListener() != null && (view.getItemClickListener() == null || listView.isEnforce()))
                    view.setItemClickListener(listView.getItemClickListener());
                view.setLocation(2, height);
                height += view.getHeight();
                panel.add(view);
                panel.updateUI();
            }
            panel.setPreferredSize(new Dimension(listView.getWidth(), Math.max(height, listView.getHeight()-10)));
            panel.updateUI();
            listView.updateUI();
            isLoad = false;
            for (UpdateCompletedListener listener : updateCompletedListener)
                if (listener != null) listener.onCompleted(BaseAdapter.this);
        }).start();
    }

    public List<StartUpdatingListener> getStartUpdatingListener() {
        return startUpdatingListener;
    }

    public void setStartUpdatingListener(StartUpdatingListener listener) {
        this.startUpdatingListener.add(listener);
    }

    public List<UpdateCompletedListener> getUpdateCompletedListener() {
        return updateCompletedListener;
    }

    public void setUpdateCompletedListener(UpdateCompletedListener listener) {
        this.updateCompletedListener.add(listener);
    }

    public void setMainView(ListView listView, JPanel panel) {
        this.listView = listView;
        this.panel = panel;
    }

    public abstract int getSize();

    public int getID(int location) {
        return location;
    }

    public abstract Object getItem(int location);

    public abstract BaseItemView getView(int location);

}
