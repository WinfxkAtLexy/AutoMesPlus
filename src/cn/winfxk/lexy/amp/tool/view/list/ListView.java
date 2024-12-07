package cn.winfxk.lexy.amp.tool.view.list;

import cn.winfxk.lexy.amp.tool.view.list.adapter.BaseAdapter;
import cn.winfxk.lexy.amp.tool.view.list.adapter.BaseItemView;
import cn.winfxk.lexy.amp.tool.view.list.adapter.ItemClickListener;

import javax.swing.*;
import java.awt.*;

public class ListView extends JScrollPane {
    private ItemClickListener itemClickListener;
    private final JPanel panel = new JPanel();
    private MyScrollBarUI scrollBarUI;
    private BaseAdapter adapter;
    private boolean enforce;

    public ListView() {
        this(null);
    }

    @Override
    public void setOpaque(boolean isOpaque) {
        super.setOpaque(isOpaque);
        if (panel != null) panel.setOpaque(isOpaque);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (adapter != null)
            for (Component component : panel.getComponents()) component.setEnabled(enabled);
    }

    public ListView(BaseAdapter adapter) {
        panel.setLayout(null);
        getVerticalScrollBar().setUI(scrollBarUI = new ScrollBarUI());
        setViewportView(panel);
        setAdapter(adapter);
        setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
    }

    public MyScrollBarUI getScrollBarUI() {
        return scrollBarUI;
    }

    public void setScrollBarUI(MyScrollBarUI scrollBarUI) {
        getVerticalScrollBar().setUI(this.scrollBarUI = scrollBarUI);
    }

    /**
     * 更改点击监听器
     *
     * @param enforce
     */
    public void setEnforce(boolean enforce) {
        this.enforce = enforce;
    }

    /**
     * @return 是否覆盖原有监听器，若值为真，这不会执行Item View原有的监听器，否则都会执行
     */
    public boolean isEnforce() {
        return enforce;
    }

    /**
     * 获取按钮监听事件
     *
     * @return
     */
    public ItemClickListener getItemClickListener() {
        return itemClickListener;
    }

    /**
     * 设置按钮的点击事件
     *
     * @param itemClickListener 监听器
     */
    public void setItemClickListener(ItemClickListener itemClickListener) {
        setItemClickListener(itemClickListener, false);
    }

    /**
     * 设置按钮的点击事件
     *
     * @param itemClickListener 监听器
     * @param enforce           是否覆盖原有监听器，若值为真，这不会执行Item View原有的监听器，否则都会执行
     */
    public void setItemClickListener(ItemClickListener itemClickListener, boolean enforce) {
        this.itemClickListener = itemClickListener;
        this.enforce = enforce;
        BaseItemView view;
        for (Component component : panel.getComponents()) {
            if (!(component instanceof BaseItemView)) continue;
            view = ((BaseItemView) component);
            if (view.getItemClickListener() != null && !isEnforce()) continue;
            view.setItemClickListener(itemClickListener);
        }
    }


    @Override
    public void setSize(Dimension d) {
        setSize((int) d.getWidth(), (int) d.getHeight());
    }

    @Override
    public void setSize(int width, int height) {
        int newWidth = width - 15;
        if (newWidth != getWidth()) {
            panel.setPreferredSize(new Dimension(newWidth, panel.getHeight()));
            new Thread(() -> {
                for (Component itemView : panel.getComponents()) {
                    if (itemView == null) continue;
                    itemView.setSize(newWidth, itemView.getHeight());
                    if (itemView instanceof BaseItemView) ((BaseItemView) itemView).start();
                }
            }).start();
        }
        super.setSize(newWidth, height);
    }

    public BaseAdapter getAdapter() {
        return adapter;
    }

    /**
     * 设置列表适配器
     *
     * @param adapter
     */
    public void setAdapter(BaseAdapter adapter) {
        this.adapter = adapter;
        if (adapter != null) {
            adapter.setMainView(this, panel);
            adapter.UpdateAdapter();
        }
    }
}
