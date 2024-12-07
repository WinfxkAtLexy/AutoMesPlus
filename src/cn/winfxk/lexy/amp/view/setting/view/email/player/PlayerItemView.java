package cn.winfxk.lexy.amp.view.setting.view.email.player;

import cn.winfxk.lexy.amp.Log;
import cn.winfxk.lexy.amp.Main;
import cn.winfxk.lexy.amp.tool.view.MyJPanel;
import cn.winfxk.lexy.amp.tool.view.OnclickListener;
import cn.winfxk.lexy.amp.tool.view.list.adapter.BaseItemView;
import cn.winfxk.lexy.amp.tool.view.JOptionPane;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerItemView extends BaseItemView implements OnclickListener {
    private static final Font font = new Font("楷体", Font.BOLD, 20);
    private final String item;
    private final JLabel label;

    public PlayerItemView(String item) {
        super();
        this.item = item;
        label = new JLabel(item, SwingConstants.CENTER);
        label.setOpaque(false);
        label.setLocation(0, 0);
        label.setFont(font);
        add(label);
        setOnclickListener(this);
    }

    @Override
    public void start() {
        label.setSize(getSize());
    }

    @Override
    public void onClickView(MyJPanel view) {
        if (JOptionPane.showConfirmDialog(null, "确定要删除这个接收人吗？", "提示", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE) != 0)
            return;
        List<Object> array = Main.getConfig().getList("预警邮件接收清单", new ArrayList<>());
        array.remove(item);
        array.remove(null);
        array.remove("");
        Log.i("删除邮件接受人：" + item);
        Main.getConfig().set("预警邮件接收清单", array).save();
        PlayerAdapter.getAdapter().UpdateAdapter();
        JOptionPane.showMessageDialog(null, "操作成功！", "提示", JOptionPane.PLAIN_MESSAGE);
    }
}
