package cn.winfxk.lexy.amp.view.setting.view.setting;

import cn.winfxk.lexy.amp.tool.view.MyJPanel;
import cn.winfxk.lexy.amp.view.setting.view.setting.view.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Panel extends MyJPanel {
    protected static final Font labelFont = new Font("楷体", Font.BOLD, 15);
    protected static final Font editFont = new Font("楷体", Font.PLAIN, 15);
    protected final List<BaseView> list = new ArrayList<>();
    protected final static int ItemHeight = 50;
    private int labelWidth;

    public List<BaseView> getList() {
        return list;
    }

    public Panel() {
        super();
        list.add(new TaskDay(this));
        list.add(new ShowTaskDay(this));
        list.add(new Sleep(this));
        list.add(new AllMes(this));
        list.add(new DayMes(this));
        list.add(new MesLength(this));
        list.add(new Keylist(this));
        list.add(new Modellist(this));
        list.add(new Linetime(this));
        list.add(new LineDay(this));
        for (BaseView view : list) add(view);
    }

    @Override
    public void start() {
        labelWidth = 0;
        for (BaseView view : list)
            labelWidth = Math.max((view.getHint().length() + 4) * labelFont.getSize(), labelWidth);
        int y = 0;
        for (BaseView view : list) {
            view.setLocation(0, y);
            view.setSize(getWidth(), ItemHeight);
            view.start();
            y += ItemHeight;
        }
    }

    /**
     * @return 返回设置项目标题的宽度
     */
    public int getLabelWidth() {
        return labelWidth;
    }

    public JTextField getJTextField() {
        JTextField edit = new JTextField();
        edit.setFont(editFont);
        edit.setOpaque(false);
        return edit;
    }

    public JLabel getLabel(String s) {
        JLabel label = new JLabel(s + "  ");
        label.setFont(labelFont);
        label.setOpaque(false);
        return label;
    }
}
