package cn.winfxk.lexy.amp.tool.view;


import cn.winfxk.lexy.amp.Main;
import cn.winfxk.lexy.amp.tool.Tool;
import cn.winfxk.lexy.amp.tool.view.image.ImageView;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class MyJFrame extends JFrame implements ComponentListener, StartView {
    private MyJPanel panel;
    public static final BufferedImage back;
    public static final List<BufferedImage> Images = new ArrayList<>();
    private boolean initialization = true;

    static {
        BufferedImage image;
        try {
            image = ImageIO.read(Main.getStream("close.png"));
            Images.add(image);
        } catch (Exception e) {
            image = null;
        }
        back = image;
    }

    public MyJFrame() {
        super();
        addComponentListener(this);
    }

    public static BufferedImage getBack() {
        return Images.get(Tool.getRand(0, Images.size() - 1));
    }

    public MyJFrame setUpButton(ImageView button) {
        button.setOnClick(imageButton -> upPanel());
        return this;
    }

    public MyJFrame setContentPane(MyJPanel contentPane) {
        contentPane.setUpPanel(panel);
        panel = contentPane;
        contentPane.setSize(getSize());
        if (contentPane.getUpButton() != null) setUpButton(contentPane.getUpButton());
        super.setContentPane(contentPane);
        setTitle(panel.getTitle() == null ? "" : panel.getTitle());
        if (!initialization)
            contentPane.start();
        return this;
    }

    public MyJFrame upPanel() {
        if (panel.getUpPanel() == null) return this;
        super.setContentPane(panel = panel.getUpPanel());
        panel.setSize(getSize());
        panel.start();
        setTitle(panel.getTitle() == null ? "" : panel.getTitle());
        return this;
    }

    @Override
    public void start() {
        panel.start();
    }

    @Override
    public void componentResized(ComponentEvent e) {
        if (initialization) return;
        panel.setSize(getSize());
        start();
    }

    public void setInitialization() {
        this.initialization = false;
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
