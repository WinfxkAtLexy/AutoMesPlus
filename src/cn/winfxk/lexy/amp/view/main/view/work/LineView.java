package cn.winfxk.lexy.amp.view.main.view.work;

import cn.winfxk.lexy.amp.mes.MesItem;
import cn.winfxk.lexy.amp.tool.Tool;
import cn.winfxk.lexy.amp.tool.view.JOptionPane;
import cn.winfxk.lexy.amp.tool.view.MyJPanel;
import cn.winfxk.lexy.amp.tool.view.list.adapter.ItemClickListener;
import cn.winfxk.lexy.amp.view.main.view.task.TipView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 时间线线条的实现类
 */
public class LineView extends MyJPanel implements Runnable, MouseListener {
    private static final Font font = new Font("黑体", Font.BOLD, 20);
    private final static int pand = 2;
    private final List<MesItem> items;
    private final int max, fontWidth;
    private final JLabel titleView;
    private final JLabel label;
    private final String title;

    /**
     * 创建一个单一时间线
     *
     * @param title 时间
     * @param items MES数量
     * @param max   最大数字
     */
    public LineView(String title, List<MesItem> items, int max) {
        super();
        this.max = max;
        this.items = items == null ? new ArrayList<>() : items;
        label = new JLabel("0");
        label.setLayout(null);
        label.setLocation(0, 0);
        label.setOpaque(true);
        label.setFont(font);
        label.setBackground(Color.yellow);
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.addMouseListener(this);
        add(label);
        fontWidth = font.getSize() * (title == null ? 0 : (title.length() - 4));
        titleView = new JLabel(this.title = title, SwingConstants.CENTER);
        titleView.setFont(font);
        titleView.setOpaque(false);
        titleView.addMouseListener(this);
        titleView.setVerticalAlignment(SwingConstants.CENTER);
        add(titleView);
    }

    @Override
    public void start() {
        label.setSize(0, getHeight());
        titleView.setSize(getWidth(), getHeight());
        titleView.setLocation(label.getX() + label.getWidth(), 0);
        new Thread(this).start();
    }

    @Override
    public void run() {
        int count = items == null ? 0 : items.size();
        double rait = (double) count / (double) max;
        int maxWidth = Math.max((int) (rait * getWidth()) - 8, font.getSize());
        titleView.setSize(fontWidth, getHeight());
        boolean isAdd = false;
        if (count == 0) {
            label.setBackground(Color.CYAN);
            label.setText(count + "");
            label.setHorizontalAlignment(SwingConstants.CENTER);
        } else label.setText(" " + count);
        for (int width = 0; width < maxWidth; width++) {
            Tool.sleep(Panel.sleep);
            label.setSize(width, getHeight());
            if (getWidth() - width - 50 > fontWidth) {
                titleView.setLocation(label.getX() + label.getWidth() + pand, 0);
            } else {
                if (!isAdd) {
                    isAdd = true;
                    remove(titleView);
                    label.add(titleView, CENTER_ALIGNMENT);
                    updateUI();
                }
                titleView.setLocation(label.getWidth() - pand - titleView.getWidth(), 0);
            }
            titleView.updateUI();
            label.updateUI();
        }
    }

    /**
     * 当电机按钮也时执行
     *
     * @param item
     */
    public void onClick(MesItem item) {
        JOptionPane.showMessageDialog(null, "订单号：" + item.getS1500()
                + "\n型号：" + item.getModel() + "\n物料名称：" + item.getName() + "\n客户：" + item.getCustom() +
                "\n供应商：" + item.getSuppliers() + "\n物料编码：" + item.getCode() + "\n录入时间：" + title, "提示", JOptionPane.PLAIN_MESSAGE);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (items.isEmpty()) return;
        Map<String, ItemClickListener> listenerMap = new HashMap<>();
        for (MesItem item : items)
            listenerMap.put(item.getName(), view -> onClick(item));
        new TipView(listenerMap, title + "日录入明细，当日已录入" + items.size() + "笔").start();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
