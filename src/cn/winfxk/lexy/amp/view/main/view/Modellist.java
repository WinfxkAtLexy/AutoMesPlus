package cn.winfxk.lexy.amp.view.main.view;

import cn.winfxk.lexy.amp.tool.view.MyJPanel;
import cn.winfxk.lexy.amp.tool.view.list.ListView;
import cn.winfxk.lexy.amp.view.main.view.model.NgItemAdapter;
import cn.winfxk.lexy.amp.view.main.view.model.OkItemAdapter;
import cn.winfxk.lexy.amp.view.main.view.model.Renew;
import cn.winfxk.lexy.amp.view.main.view.model.TitleView;

import javax.swing.*;

/**
 * 一个用于显示某个型号零部件信息的容器
 */
public class Modellist extends MyJPanel {
    private final ListView okItemView, ngItemView;
    private final OkItemAdapter okItemAdapter;
    private final NgItemAdapter ngItemAdapter;
    private final TitleView titleView;

    /**
     * 一个用于显示某个型号零部件信息的容器
     */
    public Modellist() {
        super();
        titleView = new TitleView();
        titleView.setLocation(0, 0);
        add(titleView);
        add(okItemView = new ListView(okItemAdapter = new OkItemAdapter()));
        add(ngItemView = new ListView(ngItemAdapter = new NgItemAdapter()));
        okItemView.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        ngItemView.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        okItemView.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        ngItemView.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        ngItemView.setBorder(BorderFactory.createEmptyBorder());
        okItemView.setBorder(BorderFactory.createEmptyBorder());
        new Renew(okItemAdapter, ngItemAdapter).start();
    }

    @Override
    public void start() {
        titleView.setSize(getWidth(), 50);
        titleView.start();
        okItemView.setLocation(0, titleView.getHeight());
        okItemView.setSize(getWidth() / 2, getHeight() - titleView.getHeight());
        okItemAdapter.UpdateAdapter();
        ngItemView.setLocation(okItemView.getWidth(), okItemView.getY());
        ngItemView.setSize(okItemView.getSize());
        ngItemAdapter.UpdateAdapter();
    }
}
