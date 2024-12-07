package cn.winfxk.lexy.amp.tool.view.progress;

import cn.winfxk.lexy.amp.tool.view.MyJPanel;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class Progres extends MyJPanel {
    private final static Border defViewDorder = BorderFactory.createLineBorder(Color.black);
    private final static Border defLineDorder = BorderFactory.createLineBorder(Color.GRAY);
    private final JLabel label;
    private String toolTipText;
    private int margin = 1;
    /**
     * 进度条的当前值
     */
    private int current;
    /**
     * 进度条的最大值
     */
    private int max;

    /**
     * 进度条
     */
    public Progres() {
        this("", 0);
    }

    /**
     * 进度条
     *
     * @param max 进度条的最大值
     */
    public Progres(int max) {
        this("", max);
    }

    /**
     * 进度条
     *
     * @param title 悬浮于进度条上会显示的文本
     * @param max   进度条的最大值
     */
    public Progres(String toolTipText, int max) {
        super();
        this.max = max;
        this.current = 0;
        this.toolTipText = toolTipText;
        setOpaque(true);
        setBorder(defViewDorder);
        setToolTipText(toolTipText);
        setBackground(Color.WHITE);
        label = new JLabel("", SwingConstants.CENTER);
        label.setBorder(defLineDorder);
        label.setBackground(Color.black);
        label.setOpaque(true);
        add(label);
    }

    /**
     * @return 鼠标悬停会显示的文本
     */
    @Override
    public String getToolTipText() {
        return toolTipText;
    }

    /**
     * 设置鼠标悬停会显示的文本
     *
     * @param toolTipText
     */
    @Override
    public void setToolTipText(String toolTipText) {
        this.toolTipText = toolTipText;
        super.setToolTipText(toolTipText);
        if (label != null) label.setToolTipText(toolTipText);
    }

    /**
     * +
     * 获取当前的外边距
     *
     * @return
     */
    public int getMargin() {
        return margin;
    }

    /**
     * 获取进度条的当前值
     *
     * @return
     */
    public int getCurrent() {
        return current;
    }

    /**
     * 设置进度条的当前进度
     *
     * @param current
     */
    public void getCurrent(int current) {
        this.current = current;
        start();
    }

    /**
     * 获取当前的最大值
     *
     * @return
     */
    public int getMax() {
        return max;
    }

    /**
     * 设置内边距
     *
     * @param margin
     */
    public void setMargin(int margin) {
        this.margin = margin;
    }

    /**
     * 设置进度条的当前值
     *
     * @param current
     */
    public void setCurrent(int current) {
        this.current = current;
        start();
    }

    /**
     * 设置进度条的最大值
     *
     * @param max
     */
    public void setMax(int max) {
        this.max = max;
        start();
    }

    /**
     * 设置进度条的颜色
     *
     * @param color 需要设置的颜色
     */
    public void setProgressColor(Color color) {
        label.setBackground(color);
    }

    @Override
    public void start() {
        int width = getWidth();
        int height = getHeight();
        double rait = (double) Math.min(current, max) / (double) max;
        label.setSize((int) (width * rait), (height - margin * 2));
        label.setLocation(0, height / 2 - label.getHeight() / 2);
    }
}
