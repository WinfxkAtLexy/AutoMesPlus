package cn.winfxk.lexy.amp.tool.view.list.adapter;

import cn.winfxk.lexy.amp.tool.view.MyJPanel;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public abstract class BaseItemView extends MyJPanel implements MouseListener, Cloneable {
    private static final Border SuspendBorder = BorderFactory.createMatteBorder(2, 2, 2, 2, Color.DARK_GRAY);
    private static final Border ClickBorder = BorderFactory.createLoweredBevelBorder();
    private static final Border Border = BorderFactory.createEtchedBorder();
    private ItemClickListener itemClickListener;
    private volatile boolean isClick, isEnter;
    private Border borderCache;
    private Object Tag;
    private Object Item;
    private int ID;

    public BaseItemView() {
        super();
        addMouseListener(this);
        setBorder(Border);
    }

    public boolean isCustomSize() {
        return false;
    }

    @Override
    public BaseItemView clone() throws CloneNotSupportedException {
        return (BaseItemView) super.clone();
    }

    /**
     * 获取点击事件
     *
     * @return
     */
    public ItemClickListener getItemClickListener() {
        return itemClickListener;
    }

    /**
     * 设置点击事件
     *
     * @param itemClickListener
     */
    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
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


    /**
     * 获取ItemView ID
     *
     * @return
     */
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Object getItem() {
        return Item;
    }

    public void setItem(Object item) {
        Item = item;
    }

    public Object getTag() {
        return Tag;
    }

    public void setTag(Object tag) {
        Tag = tag;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (itemClickListener != null && isEnabled()) itemClickListener.onClick(this);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!isEnabled()) return;
        setbuttonBorder(isEnter() ? BorderFactory.createCompoundBorder(SuspendBorder, SuspendBorder) : ClickBorder);
        setClick(true);
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
    public void mouseReleased(MouseEvent e) {
        setClick(false);
        setbuttonBorder(isEnter() ? SuspendBorder : borderCache);
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
}
