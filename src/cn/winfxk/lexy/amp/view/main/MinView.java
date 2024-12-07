package cn.winfxk.lexy.amp.view.main;

import cn.winfxk.lexy.amp.Image;
import cn.winfxk.lexy.amp.Log;
import cn.winfxk.lexy.amp.Main;
import cn.winfxk.lexy.amp.tool.view.MyJPanel;
import cn.winfxk.lexy.amp.tool.view.image.ImageButton;
import cn.winfxk.lexy.amp.tool.view.image.ImageView;
import cn.winfxk.lexy.amp.tool.view.image.OnClickListener;
import cn.winfxk.lexy.amp.view.AutoMes;

import javax.swing.*;
import java.awt.event.*;
import java.time.Duration;
import java.time.Instant;

public class MinView extends MyJPanel implements WindowListener, OnClickListener, MouseListener, MouseMotionListener {
    private static final JFrame frame = new JFrame();
    private int ClickX, ClickY, frameX, frameY;
    private volatile boolean mousePressed;
    private static MinView main = null;
    private final ImageButton image;
    private int frameS = 0;
    private Instant instant;

    private MinView() {
        super();
        frame.setSize(50, 50);
        frame.setTitle("单击最大化。");
        frame.setIconImage(AutoMes.frame.getIconImage());
        frame.setLocation(Main.ScreenWidth / 2 - frame.getWidth() / 2, Main.ScreenHeight / 2 - frame.getHeight() / 2);
        frame.setUndecorated(true);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setContentPane(this);
        frame.addWindowListener(this);
        image = new ImageButton(Image.getIcon());
        image.setLocation(0, 0);
        image.addMouseListener(this);
        image.addMouseMotionListener(this);
        add(image);
    }

    @Override
    public void start() {
        main.setSize(frame.getSize());
        image.setSize(main.getSize());
        image.start();
    }

    @Override
    public void onClick(ImageView imageButton) {
        Log.i("最大化程序.");
        frame.setVisible(false);
        AutoMes.frame.setVisible(true);
    }

    public void min() {
        Log.i("最小化程序.");
        frame.setVisible(true);
        AutoMes.frame.setVisible(false);
    }

    /**
     * @return 获取最小化视图的对象，若这个对象不存在会创建一个
     */
    protected static MinView getMain() {
        if (main == null) {
            main = new MinView();
            main.start();
        }
        return main;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        ClickX = e.getX();
        ClickY = e.getY();
        frameX = frame.getX();
        frameY = frame.getY();
        instant = Instant.now();
        frameS = 0;
        mousePressed = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (Duration.between(instant, Instant.now()).toMillis() < 300)
            if (frameS < 100) onClick(null);
        mousePressed = false;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (mousePressed) {
            frameS += Math.abs(frame.getX() - frameX) + Math.abs(frame.getY() - frameY);
            frameX = frame.getX();
            frameY = frame.getY();
            frame.setLocation(e.getXOnScreen() - ClickX, e.getYOnScreen() - ClickY);
        }
    }

    @Override
    public void windowClosing(WindowEvent e) {
        Main.close(0);
    }

    @Override
    public void windowClosed(WindowEvent e) {
        Main.close(0);
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

}
