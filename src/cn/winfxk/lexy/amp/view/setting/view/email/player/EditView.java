package cn.winfxk.lexy.amp.view.setting.view.email.player;

import cn.winfxk.lexy.amp.Log;
import cn.winfxk.lexy.amp.Main;
import cn.winfxk.lexy.amp.tool.Tool;
import cn.winfxk.lexy.amp.tool.view.JOptionPane;
import cn.winfxk.lexy.amp.tool.view.MyJPanel;
import cn.winfxk.lexy.amp.tool.view.button.Button;
import cn.winfxk.lexy.amp.tool.view.button.ClickEvent;
import cn.winfxk.lexy.amp.tool.view.button.OnClickListener;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EditView extends MyJPanel implements OnClickListener {
    private static final Font font = new Font("楷体", Font.BOLD, 20);
    private final JTextField edit;
    private final Button button;

    public EditView() {
        super();
        edit = new JTextField();
        edit.setOpaque(false);
        edit.setFont(font);
        edit.setLocation(0, 0);
        add(edit);
        button = new Button("添加");
        button.setFont(font);
        button.setOnClickListener(this);
        add(button);
    }

    @Override
    public void start() {
        button.setSize(Tool.getMath(150, 100, getWidth() / 7), getHeight());
        button.setLocation(getWidth() - button.getWidth() - 10, 0);
        button.start();
        edit.setSize(button.getX() - 10, getHeight());
    }

    @Override
    public void onClick(ClickEvent event) {
        String item = edit.getText();
        if (item == null || item.isEmpty()) {
            edit.setFocusable(true);
            JOptionPane.showMessageDialog(null, "请输入接收人的邮件", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!item.contains("@") || !item.contains(".")) {
            edit.setFocusable(true);
            JOptionPane.showMessageDialog(null, "输入的接收人邮件地址不合法！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        List<Object> array = Main.getConfig().getList("预警邮件接收清单", new ArrayList<>());
        if (array.contains(item)) {
            edit.setFocusable(true);
            JOptionPane.showMessageDialog(null, "输入的接收人邮件地址已存在！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        array.remove(null);
        array.remove("");
        array.add(item);
        Log.i("新增邮件接受人：" + item);
        Main.getConfig().set("预警邮件接收清单", array).save();
        PlayerAdapter.getAdapter().UpdateAdapter();
        edit.setText("");
        cn.winfxk.lexy.amp.tool.view.JOptionPane.showMessageDialog(null, "操作成功！", "提示", JOptionPane.PLAIN_MESSAGE);
    }
}
