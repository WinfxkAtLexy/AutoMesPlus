package cn.winfxk.lexy.amp.view.setting.view.setting.view;

import cn.winfxk.lexy.amp.Main;
import cn.winfxk.lexy.amp.view.setting.view.setting.BaseView;
import cn.winfxk.lexy.amp.view.setting.view.setting.Panel;

public class Keylist extends BaseView {
    public Keylist(Panel main) {
        super(main);
    }

    @Override
    public String getToolTipHint() {
        return "检查关键零部件清单的间隔";
    }

    @Override
    public Object getValue() {
        return Math.max(1200,Main.getConfig().getInt(getKey(),1200));
    }

    @Override
    public String getKey() {
        return "关键零部件清单更新间隔";
    }

    @Override
    public String getHint() {
        return getKey();
    }
}
