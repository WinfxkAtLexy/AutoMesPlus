package cn.winfxk.lexy.amp.tool.view;

import cn.winfxk.lexy.amp.tool.view.image.ImageView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public abstract class MyJPanel extends JPanel implements ComponentListener, StartView {
    private final List<MyJPanel> startList = new ArrayList<>();
    private volatile boolean startOK = true;
    private OnclickListener listener;
    private boolean isInit = true;
    protected int width, height;
    private ImageView UpButton;
    private MyJPanel upPanel;
    private String Title;
    private Object NBT;

    public MyJPanel() {
        super();
        setOpaque(false);
        setLayout(null);
        addMouseListener(new Click(this));
    }

    private static class Click implements MouseListener {
        private final MyJPanel view;

        public Click(MyJPanel myJPanel) {
            this.view = myJPanel;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (view.getOnclickListener() != null) view.getOnclickListener().onClickView(view);
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

    public void setOnclickListener(OnclickListener listener) {
        this.listener = listener;
    }

    public OnclickListener getOnclickListener() {
        return listener;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        for (Component component : getComponents())
            component.setEnabled(enabled);
    }

    public Object getNBT() {
        return NBT;
    }

    public void setNBT(Object NBT) {
        this.NBT = NBT;
    }

    public void setUpButton(ImageView upButton) {
        UpButton = upButton;
    }

    public ImageView getUpButton() {
        return UpButton;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setUpPanel(MyJPanel upPanel) {
        this.upPanel = upPanel;
    }

    public MyJPanel getUpPanel() {
        return upPanel;
    }

    @Override
    public void setPreferredSize(Dimension preferredSize) {
        width = preferredSize.width;
        height = preferredSize.height;
        super.setPreferredSize(preferredSize);
    }

    public static ImageIcon getIcon(BufferedImage image) {
        return new ImageIcon(image.getScaledInstance(image.getWidth(), image.getHeight(), Image.SCALE_FAST));
    }

    public static ImageIcon getIcon(BufferedImage image, int width, int height) {
        return new ImageIcon(image.getScaledInstance(width, height, Image.SCALE_FAST));
    }

    public static ImageIcon getIcon(Image image, int width, int height) {
        return new ImageIcon(image.getScaledInstance(width, height, Image.SCALE_FAST));
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        this.width = width;
        this.height = height;
    }

    @Override
    public void setSize(Dimension d) {
        super.setSize(d);
        this.width = d.width;
        this.height = d.height;
    }

    public List<MyJPanel> getStartList() {
        return startList;
    }

    /**
     * 添加一个需要同步刷新的控件
     *
     * @param panel
     */
    protected void addStart(MyJPanel panel) {
        startList.add(panel);
    }

    @Override
    public void componentResized(ComponentEvent e) {
        if (!startOK) return;
        startOK = false;
        width = getWidth();
        height = getHeight();
        if (!isInit) new Thread(() -> {
            start();
            for (MyJPanel panel : startList)
                panel.start();
            startOK = true;
        }).start();
        else startOK = true;
        isInit = false;
    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }
}
