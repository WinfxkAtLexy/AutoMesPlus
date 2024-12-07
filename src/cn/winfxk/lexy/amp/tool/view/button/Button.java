package cn.winfxk.lexy.amp.tool.view.button;

import cn.winfxk.lexy.amp.tool.Tool;
import cn.winfxk.lexy.amp.tool.view.StartView;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class Button extends JLabel implements MouseListener, StartView {
    private static final Border SuspendBorder = BorderFactory.createMatteBorder(2, 2, 2, 2, Color.GREEN);
    private static final Border ClickBorder = BorderFactory.createLoweredBevelBorder();
    private static final Border Border = BorderFactory.createEtchedBorder();
    private volatile boolean isClick, isEnter;
    private OnClickListener listener;
    private Border borderCache;

    /**
     * 创建一个按钮
     */
    public Button() {
        this("");
    }

    /**
     * 创建一个按钮
     *
     * @param text 按钮上想要显示的文本
     */
    public Button(Object text) {
        this(Tool.objToString(text, ""), CENTER);
    }

    /**
     * 创建一个按钮
     *
     * @param text                按钮上想要显示的文本
     * @param horizontalAlignment 布局
     */
    public Button(Object text, int horizontalAlignment) {
        this(Tool.objToString(text, ""), (Icon) null, horizontalAlignment);
    }

    /**
     * 创建一个按钮
     *
     * @param text                按钮上想要显示的文本
     * @param image               按钮上想要显示的图标
     * @param horizontalAlignment 布局
     */
    public Button(Object text, BufferedImage image, int horizontalAlignment) {
        this(Tool.objToString(text, ""), image == null ? null : new ImageIcon(image.getScaledInstance(image.getWidth(), image.getHeight(), java.awt.Image.SCALE_DEFAULT)), horizontalAlignment);
    }

    /**
     * 创建一个按钮
     *
     * @param text                按钮上想要显示的文本
     * @param image               按钮上想要显示的图标
     * @param horizontalAlignment 布局
     */
    public Button(Object text, Icon image, int horizontalAlignment) {
        super(Tool.objToString(text, ""), image, horizontalAlignment);
        setOpaque(false);
        addMouseListener(this);
        setBorder(Border);
        setLayout(null);
    }

    public OnClickListener getOnClickListener() {
        return listener;
    }

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (listener != null && isEnabled()) listener.onClick(new ClickEvent(this, e));
    }


    @Override
    public void mousePressed(MouseEvent e) {
        if (!isEnabled()) return;
        setbuttonBorder(isEnter() ? BorderFactory.createCompoundBorder(SuspendBorder, SuspendBorder) : ClickBorder);
        setClick(true);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        setClick(false);
        setbuttonBorder(isEnter() ? SuspendBorder : borderCache);
    }

    @Override
    public void setBorder(Border border) {
        super.setBorder(border);
        this.borderCache = border;
    }

    private void setbuttonBorder(Border border) {
        super.setBorder(border);
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (!isEnabled()) return;
        setEnter(true);
        setbuttonBorder(isClick() ? BorderFactory.createCompoundBorder(SuspendBorder, SuspendBorder) : SuspendBorder);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        setEnter(false);
        setbuttonBorder(isClick() ? ClickBorder : borderCache);
    }

    /**
     * 鼠标是否悬停
     *
     * @return
     */
    private boolean isEnter() {
        return isEnter;
    }

    /**
     * 鼠标是否点击
     *
     * @return
     */
    private boolean isClick() {
        return isClick;
    }

    /**
     * 设置是否是鼠标悬停
     *
     * @param enter
     */
    private void setEnter(boolean enter) {
        isEnter = enter;
    }

    /**
     * 设置是否被点击
     *
     * @param click
     */
    private void setClick(boolean click) {
        isClick = click;
    }

    @Override
    public void start() {

    }
}
