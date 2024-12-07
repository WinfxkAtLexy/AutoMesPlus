package cn.winfxk.lexy.amp.view.setting.view.setting.view;

import cn.winfxk.lexy.amp.Main;
import cn.winfxk.lexy.amp.view.setting.view.setting.BaseView;
import cn.winfxk.lexy.amp.view.setting.view.setting.Panel;

public class ShowTaskDay extends BaseView {

    public ShowTaskDay(Panel main) {
        super(main);
    }

    @Override
    public String getToolTipHint() {
        return "主页将会显示的计划天数";
    }

    @Override
    public Object getValue() {
        return Math.max(1, Main.getConfig().getInt(getKey(), 3));
    }

    @Override
    public String getKey() {
        return "显示计划天数";
    }

    @Override
    public String getHint() {
        return "主页滚动计划显示天数";
    }
}
