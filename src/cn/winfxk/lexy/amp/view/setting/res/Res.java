package cn.winfxk.lexy.amp.view.setting.res;

import cn.winfxk.lexy.amp.Image;
import cn.winfxk.lexy.amp.Log;
import cn.winfxk.lexy.amp.Main;
import cn.winfxk.lexy.amp.tool.Tool;
import cn.winfxk.lexy.amp.tool.view.JOptionPane;
import cn.winfxk.lexy.amp.tool.view.MyJPanel;
import cn.winfxk.lexy.amp.tool.view.OnclickListener;
import cn.winfxk.lexy.amp.tool.view.list.ListView;
import cn.winfxk.lexy.amp.view.setting.TitleView;
import cn.winfxk.lexy.amp.view.setting.res.view.Meslist;
import cn.winfxk.lexy.amp.view.setting.res.view.Task;

import javax.swing.*;
import java.util.LinkedHashMap;

import static cn.winfxk.lexy.amp.Main.ScreenHeight;
import static cn.winfxk.lexy.amp.Main.ScreenWidth;

public class Res extends MyJPanel {
    private static final int defWidth = Math.min(1000, ScreenWidth), defHeight = Math.min(800, ScreenHeight);
    public static final LinkedHashMap<String, OnclickListener> map = new LinkedHashMap<>();
    private static final JFrame frame = new JFrame();
    private final TitleView titleView;
    private final ResAdapter adapter;
    private final ListView listView;
    private static Res main;

    static {
        map.put("滚动计划", view -> Task.setVisible());
        map.put("MES数据", view -> Meslist.setVisible());
        map.put("清除缓存", view -> {
            if (JOptionPane.showConfirmDialog(null, "确定要清除缓存吗？这样的操作可能会导致您无法查看往日滚动计划何MES数据。",
                    "警告", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) != 0) return;
            if (Tool.delete(Main.TaskDataDir, Main.MesDataDir)) {
                Log.w("已成功删除缓存数据，包括缓存的MES数据和滚动计划");
                JOptionPane.showMessageDialog(null, "删除成功！应用程序即将退出，请手动再次启动！", "提示", JOptionPane.PLAIN_MESSAGE);
            } else {
                Log.w("缓存清除可能失败！但为了保险起见依然准备关闭应用程序");
                JOptionPane.showMessageDialog(null, "缓存清除可能失败！请检查应用程序运行日志", "错误", JOptionPane.WARNING_MESSAGE);
            }
        });
        map.put("关闭", view -> frame.dispose());
    }

    public Res() {
        super();
        main = this;
        frame.setTitle("资源管理");
        frame.setIconImage(Image.getIcon());
        frame.addComponentListener(this);
        frame.setSize(defWidth, defHeight);
        frame.setLocation(ScreenWidth / 2 - frame.getWidth() / 2, ScreenHeight / 2 - frame.getHeight() / 2);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setContentPane(this);
        setSize(frame.getSize());
        titleView = new TitleView();
        titleView.setLocation(0, 0);
        add(titleView);
        add(listView = new ListView(adapter = new ResAdapter()));
    }

    @Override
    public void start() {
        titleView.setSize(getWidth(), Tool.getMath(200, 100, getHeight() / 7));
        titleView.start();
        listView.setLocation(0, titleView.getY() + titleView.getHeight());
        listView.setSize(getWidth(), getHeight() - 10 - titleView.getHeight());
        new Thread(() -> {
            Tool.sleep(100);
            adapter.UpdateAdapter();
        }).start();
    }

    public static void setVisible() {
        if (main == null) new Res();
        if (!frame.isVisible()) frame.setVisible(true);
        frame.setFocusable(true);
        main.start();
    }

    public static Res getMain() {
        return main;
    }
}
