package cn.winfxk.lexy.amp.view.setting.view.setting.view;

import cn.winfxk.lexy.amp.Main;
import cn.winfxk.lexy.amp.view.setting.view.setting.BaseView;
import cn.winfxk.lexy.amp.view.setting.view.setting.Panel;

public class DayMes extends BaseView {
    public DayMes(Panel main) {
        super(main);
    }

    @Override
    public String getToolTipHint() {
        return "将会在以下时间后更新当日已录入的MES数据";
    }

    @Override
    public Object getValue() {
        return Math.max(600, Main.getConfig().getInt(getKey(), 600));
    }

    @Override
    public String getKey() {
        return "当日MES刷新间隔";
    }

    @Override
    public String getHint() {
        return getKey();
    }
}
