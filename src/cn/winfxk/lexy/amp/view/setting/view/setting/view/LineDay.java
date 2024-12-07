package cn.winfxk.lexy.amp.view.setting.view.setting.view;

import cn.winfxk.lexy.amp.Main;
import cn.winfxk.lexy.amp.view.setting.view.setting.BaseView;
import cn.winfxk.lexy.amp.view.setting.view.setting.Panel;

public class LineDay extends BaseView {
    public LineDay(Panel main) {
        super(main);
    }

    @Override
    public String getToolTipHint() {
        return getHint();
    }

    @Override
    public Object getValue() {
        return Math.max(1, Main.getConfig().getInt(getKey(), 10));
    }

    @Override
    public String getKey() {
        return "时间线显示天数";
    }

    @Override
    public String getHint() {
        return "近日录入笔数将会显示的天数";
    }
}
