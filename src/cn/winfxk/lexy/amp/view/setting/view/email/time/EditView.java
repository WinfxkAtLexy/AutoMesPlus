package cn.winfxk.lexy.amp.view.setting.view.email.time;

import cn.winfxk.lexy.amp.Log;
import cn.winfxk.lexy.amp.Main;
import cn.winfxk.lexy.amp.email.EmailThread;
import cn.winfxk.lexy.amp.tool.Tool;
import cn.winfxk.lexy.amp.tool.view.JOptionPane;
import cn.winfxk.lexy.amp.tool.view.MyJPanel;
import cn.winfxk.lexy.amp.tool.view.button.Button;
import cn.winfxk.lexy.amp.tool.view.button.ClickEvent;
import cn.winfxk.lexy.amp.tool.view.button.OnClickListener;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
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
            JOptionPane.showMessageDialog(null, "请输入邮件发送时间！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!item.contains(":")) {
            edit.setFocusable(true);
            JOptionPane.showMessageDialog(null, "输入的邮件发送时间不合法！！\n请使用格式：时:分，例如：16:00", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            EmailThread.format.parse(item);
        } catch (ParseException e) {
            edit.setFocusable(true);
            JOptionPane.showMessageDialog(null, "输入的邮件发送时间不合法！！\n请使用格式：时:分，例如：16:00", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        List<Object> list1 = Main.getConfig().getList("邮件发送时间时", new ArrayList<>());
        if (list1.contains(item)) {
            edit.setFocusable(true);
            JOptionPane.showMessageDialog(null, "输入的发送时间已存在！！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        list1.remove(null);
        list1.remove("");
        list1.add(item);
        Log.i("新增邮件发送时间:" + item);
        Main.getConfig().set("邮件发送时间时", list1).save();
        TimeAdapter.getAdapter().UpdateAdapter();
        edit.setText("");
        JOptionPane.showMessageDialog(null, "操作成功！", "提示", JOptionPane.PLAIN_MESSAGE);
    }
}
