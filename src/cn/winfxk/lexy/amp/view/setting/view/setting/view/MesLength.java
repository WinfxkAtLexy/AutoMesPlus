package cn.winfxk.lexy.amp.view.setting.view.setting.view;

import cn.winfxk.lexy.amp.Main;
import cn.winfxk.lexy.amp.view.setting.view.setting.BaseView;
import cn.winfxk.lexy.amp.view.setting.view.setting.Panel;

public class MesLength extends BaseView {
    public MesLength(Panel main) {
        super(main);
    }

    @Override
    public String getToolTipHint() {
        return "这个值将会决定程序运行时会从几个月前MES数据检查覆盖率";
    }

    @Override
    public Object getValue() {
        return Math.max(1, Main.getConfig().getInt(getKey(), 1));
    }

    @Override
    public String getKey() {
        return "MES抓取距离";
    }

    @Override
    public String getHint() {
        return "获取MES几月前的数据";
    }
}
