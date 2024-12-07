package cn.winfxk.lexy.amp.view.setting.view.setting.view;

import cn.winfxk.lexy.amp.Main;
import cn.winfxk.lexy.amp.view.setting.view.setting.BaseView;
import cn.winfxk.lexy.amp.view.setting.view.setting.Panel;

public class AllMes extends BaseView {
    public AllMes(Panel main) {
        super(main);
    }

    @Override
    public String getToolTipHint() {
        return "将会在此时间后更新近期的MES数据";
    }

    @Override
    public Object getValue() {
        return Math.max(2000, Main.getConfig().getInt(getKey(), 2000));
    }

    @Override
    public String getKey() {
        return "全局MES刷新间隔";
    }

    @Override
    public String getHint() {
        return "近期MES刷新间隔(秒)";
    }
}
