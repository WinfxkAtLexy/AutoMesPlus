package cn.winfxk.lexy.amp.view;

import cn.winfxk.lexy.amp.*;
import cn.winfxk.lexy.amp.crucial.CrucialThread;
import cn.winfxk.lexy.amp.email.EmailThread;
import cn.winfxk.lexy.amp.excel.ExcelFactory;
import cn.winfxk.lexy.amp.mes.WebThread;
import cn.winfxk.lexy.amp.tool.Tool;
import cn.winfxk.lexy.amp.tool.view.MyJPanel;
import cn.winfxk.lexy.amp.view.main.CloseView;
import cn.winfxk.lexy.amp.view.main.Panel;
import cn.winfxk.lexy.amp.view.main.TitleView;
import cn.winfxk.lexy.amp.view.main.view.AllTaskData;
import cn.winfxk.lexy.amp.view.update.Update;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AutoMes extends MyJPanel implements WindowListener, Runnable {
    public static final JFrame frame = new JFrame();
    public static volatile boolean isSleep = false;
    private static BranchFactory magFactory;
    private final TitleView titleView;
    private final CloseView closeView;
    private ExcelFactory factory;
    private static AutoMes main;
    public static int SleepTime;
    private final Panel panel;

    public static BranchFactory getBranchFactory() {
        return magFactory;
    }

    /**
     * 界面构建
     */
    public AutoMes() {
        super();
        magFactory = BranchFactory.getFactory(All.Codeindex);
        main = this;
        SleepTime = Main.getConfig().getInt("休眠时间", 6);
        new Thread(this).start();
        frame.setSize(Main.ScreenWidth, Main.ScreenHeight);
        frame.setLocation(0, 0);
        frame.setTitle("AutoMes");
        frame.setIconImage(Image.getIcon());
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setUndecorated(true);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setContentPane(this);
        frame.addWindowListener(this);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(frame.getSize());
        closeView = new CloseView();
        add(closeView);
        titleView = new TitleView();
        titleView.setLocation(0, 0);
        add(titleView, CENTER_ALIGNMENT);
        panel = new Panel();
        add(panel);
    }

    /**
     * 对外开放的接口
     */
    public static AutoMes getMain() {
        return main;
    }

    @Override
    public void start() {
        try {
            new WebThread().start();
            new CrucialThread().start();
            new Thread(new Update()).start();
            CrucialThread.reload();
            closeView.setSize(150, 30);
            closeView.setLocation(getWidth() - closeView.getWidth(), 0);
            closeView.start();
            titleView.setSize(getWidth(), 100);
            titleView.start();
            panel.setLocation(0, titleView.getHeight());
            panel.setSize(getWidth() - 10, getHeight() - titleView.getHeight() - 50);
            panel.start();
            factory = magFactory.start();
            frame.setVisible(true);
            Log.i("程序启动！");
            if (Main.getConfig().getBoolean("启用邮件系统", false)) {
                String pcName = Main.getConfig().getString("邮件发送主机", "kc2268");
                if ((Main.Username != null && Main.Username.equalsIgnoreCase(pcName)) ||
                        (Main.PCName != null && Main.PCName.equalsIgnoreCase(pcName)))
                    new Thread(new EmailThread()).start();
            }
        } catch (Exception e) {
            Log.w("应用程序启动失败！", e);
            Main.close(-1);
        }
    }

    /**
     * 从新加载底部信息栏
     *
     * @param view
     */
    public static void reload(AllTaskData view) {
        if (AutoMes.getMain() == null) return;
        AutoMes.getMain().add(view);
        view.setSize(Panel.getMain().getTasklist().getWidth(), Panel.getMain().getTasklist().getListView().getAdapter().getItemHeight());
        view.setLocation(main.getWidth() / 800, main.getHeight() - 10 - view.getHeight());
        view.start();
        view.updateUI();
    }

    public static void removeView(AllTaskData view) {
        AutoMes.getMain().remove(view);
        AutoMes.getMain().updateUI();
    }

    /**
     * @return 返回滚动计划管理器
     */
    public static ExcelFactory getFactory() {
        return main.factory;
    }

    @Override
    public void windowOpened(WindowEvent e) {

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

    /**
     * 应用程序休眠管理器，用于管理应用程序的休眠状态，如在到达早上6点后才开始运行以节约性能
     */
    @Override
    public void run() {
        int hour;
        SimpleDateFormat format = new SimpleDateFormat("HH");
        Log.i("应用程序休眠管理器已启动！");
        while (Main.runing) {
            Tool.sleep(10000);
            hour = Tool.ObjToInt(format.format(new Date()));
            if (isSleep) {
                if (hour < AutoMes.SleepTime) continue;
                Log.i("程序已被唤醒！准备执行相关内容。");
                isSleep = false;
            } else if (hour < AutoMes.SleepTime) {
                isSleep = true;
                Log.i("程序已进入休眠模式。将在" + AutoMes.SleepTime + "时唤醒。");
            }
        }
    }

    /**
     * @return 判断程序是否是休眠状态F
     */
    public static boolean isIsSleep() {
        return isSleep;
    }
}
