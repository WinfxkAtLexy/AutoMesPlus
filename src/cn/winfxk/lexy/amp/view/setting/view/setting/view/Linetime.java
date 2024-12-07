package cn.winfxk.lexy.amp.view.setting.view.setting.view;

import cn.winfxk.lexy.amp.Main;
import cn.winfxk.lexy.amp.view.setting.view.setting.BaseView;
import cn.winfxk.lexy.amp.view.setting.view.setting.Panel;

public class Linetime extends BaseView {
    public Linetime(Panel main) {
        super(main);
    }

    @Override
    public String getToolTipHint() {
        return getHint();
    }

    @Override
    public Object getValue() {
        return Math.max(1, Main.getConfig().getInt(getKey(), 600));
    }

    @Override
    public String getKey() {
        return "时间线更新间隔";
    }

    @Override
    public String getHint() {
        return "近日录入笔数更新间隔";
    }
}
