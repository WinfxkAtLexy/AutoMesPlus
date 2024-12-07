package cn.winfxk.lexy.amp.tool.view.image;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class ImageButton extends ImageView {
    private static final Border SuspendBorder = BorderFactory.createMatteBorder(2, 2, 2, 2, Color.GREEN);
    private static final Border ClickBorder = BorderFactory.createLoweredBevelBorder();
    private static final Border DefaultBorder = BorderFactory.createEtchedBorder();
    private volatile boolean isClick, isEnter;
    private Border borderCache;

    public ImageButton(BufferedImage image, Stretch stretch) {
        super(image, stretch);
        setBorder(DefaultBorder);
    }

    @Override
    public void setBorder(Border border) {
        super.setBorder(border);
        this.borderCache = border;
    }

    private void setbuttonBorder(Border border) {
        super.setBorder(border);
    }

    public ImageButton() {
        this(null);
    }

    public ImageButton(BufferedImage image) {
        this(image, Stretch.Fit);
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

}
