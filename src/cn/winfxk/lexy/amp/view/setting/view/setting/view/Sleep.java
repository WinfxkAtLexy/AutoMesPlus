package cn.winfxk.lexy.amp.view.setting.view.setting.view;

import cn.winfxk.lexy.amp.Main;
import cn.winfxk.lexy.amp.view.setting.view.setting.BaseView;
import cn.winfxk.lexy.amp.view.setting.view.setting.Panel;

public class Sleep extends BaseView {

    public Sleep(Panel main) {
        super(main);
    }

    @Override
    public String getToolTipHint() {
        return "程序将会在此时间之前自动停止运行已节约性能";
    }

    @Override
    public Object getValue() {
        return Math.min(6, Main.getConfig().getInt(getKey(), 6));
    }

    @Override
    public String getKey() {
        return "休眠时间";
    }

    @Override
    public String getHint() {
        return "程序休眠时间点";
    }
}
