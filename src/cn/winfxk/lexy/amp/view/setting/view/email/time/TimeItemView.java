package cn.winfxk.lexy.amp.view.setting.view.email.time;

import cn.winfxk.lexy.amp.Log;
import cn.winfxk.lexy.amp.Main;
import cn.winfxk.lexy.amp.tool.view.JOptionPane;
import cn.winfxk.lexy.amp.tool.view.MyJPanel;
import cn.winfxk.lexy.amp.tool.view.OnclickListener;
import cn.winfxk.lexy.amp.tool.view.list.adapter.BaseItemView;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TimeItemView extends BaseItemView implements OnclickListener {
    private static final Font font = new Font("楷体", Font.PLAIN, 15);
    private final JLabel label;
    private final String item;

    public TimeItemView(String string) {
        super();
        this.item = string;
        label = new JLabel(string, SwingConstants.CENTER);
        label.setFont(font);
        label.setOpaque(false);
        label.setLocation(0, 0);
        add(label);
        setOnclickListener(this);
    }

    @Override
    public void start() {
        label.setSize(getSize());
    }

    @Override
    public void onClickView(MyJPanel view) {
        if (JOptionPane.showConfirmDialog(null, "确定要删除这个发送时间吗？", "提示", JOptionPane.OK_CANCEL_OPTION, cn.winfxk.lexy.amp.tool.view.JOptionPane.WARNING_MESSAGE) != 0)
            return;
        List<Object> array = Main.getConfig().getList("邮件发送时间时", new ArrayList<>());
        array.remove(item);
        array.remove(null);
        array.remove("");
        Main.getConfig().set("邮件发送时间时", array).save();
        TimeAdapter.getAdapter().UpdateAdapter();
        Log.i("删除邮件发送时间：" + item);
        JOptionPane.showMessageDialog(null, "操作成功！", "提示", JOptionPane.PLAIN_MESSAGE);
    }
}
