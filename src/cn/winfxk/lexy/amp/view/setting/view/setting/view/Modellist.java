package cn.winfxk.lexy.amp.view.setting.view.setting.view;

import cn.winfxk.lexy.amp.Main;
import cn.winfxk.lexy.amp.view.setting.view.setting.BaseView;
import cn.winfxk.lexy.amp.view.setting.view.setting.Panel;

public class Modellist extends BaseView {
    public Modellist(Panel main) {
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
        return "型号明细更新间隔";
    }

    @Override
    public String getHint() {
        return "主页显示各型号更新的间隔";
    }
}
