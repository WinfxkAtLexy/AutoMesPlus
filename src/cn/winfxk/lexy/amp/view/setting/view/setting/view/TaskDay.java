package cn.winfxk.lexy.amp.view.setting.view.setting.view;

import cn.winfxk.lexy.amp.Main;
import cn.winfxk.lexy.amp.view.setting.view.setting.BaseView;
import cn.winfxk.lexy.amp.view.setting.view.setting.Panel;

public class TaskDay extends BaseView {

    public TaskDay(Panel main) {
        super(main);
    }

    @Override
    public String getToolTipHint() {
        return "滚动计划将会在以下时间后更新基础数据";
    }

    @Override
    public Object getValue() {
        return Math.max(Main.getConfig().getInt(getKey(), 3600), 10);
    }

    @Override
    public String getKey() {
        return "计划更新间隔";
    }

    @Override
    public String getHint() {
        return "滚动计划更新间隔";
    }
}
