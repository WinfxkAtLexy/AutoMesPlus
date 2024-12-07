package cn.winfxk.lexy.amp.view.log;

import cn.winfxk.lexy.amp.Image;
import cn.winfxk.lexy.amp.Log;
import cn.winfxk.lexy.amp.Main;
import cn.winfxk.lexy.amp.tool.Tool;
import cn.winfxk.lexy.amp.tool.view.JOptionPane;
import cn.winfxk.lexy.amp.tool.view.MyJPanel;
import cn.winfxk.lexy.amp.tool.view.OnclickListener;
import cn.winfxk.lexy.amp.tool.view.image.ImageButton;
import cn.winfxk.lexy.amp.tool.view.list.adapter.ItemClickListener;
import cn.winfxk.lexy.amp.view.main.view.task.TipView;

import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.winfxk.lexy.amp.Main.ScreenHeight;
import static cn.winfxk.lexy.amp.Main.ScreenWidth;

public class LogView extends MyJPanel implements Log.LogListener, Runnable, OnclickListener {
    private static final int defWidth = Math.min(1000, ScreenWidth), defHeight = Math.min(800, ScreenHeight);
    private static final SimpleDateFormat date = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]");
    private final static Font font = new Font("楷体", Font.PLAIN, 13);
    private static final Color defColor = new Color(0xee, 0xee, 0xee);
    private final List<LogText> list = new ArrayList<>();
    public static final JFrame frame = new JFrame();
    private static volatile int errorID = 0;
    private static ImageButton logButton;
    private static String Content = "";
    private final TitleView titleView;
    private static JScrollPane pane;
    private final JTextArea area;
    private static LogView main;

    public static void setLogButton(ImageButton logButton) {
        LogView.logButton = logButton;
    }

    public LogView() {
        super();
        frame.setTitle("应用日志");
        frame.setIconImage(Image.getIcon());
        frame.setSize(defWidth, defHeight);
        frame.setLocation(ScreenWidth / 2 - frame.getWidth() / 2, Main.ScreenHeight / 2 - frame.getHeight() / 2);
        frame.addComponentListener(this);
        frame.setContentPane(this);
        main = this;
        titleView = new TitleView();
        titleView.setLocation(0, 0);
        titleView.setOnclickListener(this);
        add(titleView);
        pane = new JScrollPane(area = new JTextArea());
        area.setFont(font);
        area.setLocation(0, 0);
        add(pane);
        new Thread(this).start();
    }

    public void setVisible() {
        if (!frame.isVisible()) frame.setVisible(true);
        errorID = 0;
        start();
    }

    public static LogView getMain() {
        return main;
    }

    @Override
    public void start() {
        titleView.setSize(getWidth(), list.isEmpty() ? 0 : Tool.getMath(120, 80, getHeight() / 11));
        titleView.start();
        pane.setLocation(titleView.getX(), titleView.getY() + titleView.getHeight());
        pane.setSize(getWidth(), getHeight() - titleView.getHeight() - 10);
        area.setSize(pane.getSize());
    }

    @Override
    public void onLogListener(String str, Object obj, Log.Type type, Exception exc) {
        String format = Log.getTime();
        if (exc != null) {
            StringWriter stringWriter = new StringWriter();
            exc.printStackTrace(new PrintWriter(stringWriter));
            str = stringWriter.toString();
        } else str = null;
        String objToString = Tool.objToString(obj, null);
        String log = (objToString == null ? date : "") + (str == null ? "" : (str + "\n")) + (objToString != null ? format + type.getTitle() + obj : "");
        Content = log + "\n" + Content;
        if (Content.length() > 10000)
            Content = Content.substring(0, 10000);
        if (area != null) {
            area.setText(Content);
        }
        int ID = type.equals(Log.Type.Error) ? 2 : type.equals(Log.Type.Warning) ? 1 : 0;
        if (ID != 0) {
            LogText logText = new LogText();
            logText.log = log;
            logText.type = type;
            list.add(logText);
            start();
        }
        int EID = errorID;
        errorID = Math.max(ID, EID);
    }

    @Override
    public void run() {
        Log.i("日志监控系统启动！");
        int is = 0;
        boolean isAdd = true;
        while (Main.runing) {
            Tool.sleep(1);
            if (logButton == null) {
                Tool.sleep(1000);
                continue;
            }
            if (errorID == 0) {
                is = 0;
                isAdd = true;
                logButton.setBackground(defColor);
                continue;
            }
            if (errorID == 1) {
                if (isAdd) {
                    if (++is >= 255) {
                        isAdd = false;
                        continue;
                    }
                    logButton.setBackground(new Color(0xff, 0xff, is));
                } else {
                    if (--is <= 0) {
                        isAdd = true;
                        continue;
                    }
                    logButton.setBackground(new Color(0xff, 0xff, is));
                }
                continue;
            }
            if (errorID == 2) {
                if (isAdd) {
                    if (++is >= 0xee) {
                        isAdd = false;
                        continue;
                    }
                    logButton.setBackground(new Color(0xfe, is, is));
                } else {
                    if (--is <= 0) {
                        isAdd = true;
                        continue;
                    }
                    logButton.setBackground(new Color(0xfe, is, is));
                }
            }
        }
    }

    @Override
    public void onClickView(MyJPanel view) {
        Map<String, ItemClickListener> map = new HashMap<>();
        for (LogText text : new ArrayList<>(list))
            map.put(text.log, view1 -> JOptionPane.showMessageDialog(null, "日志类型："
                    + (text.type.equals(Log.Type.Error) ? "错误" : text.type.equals(Log.Type.Warning) ? "警告" : "其它")
                    + "\n日志内容：\n" + text.log));
        list.clear();
        errorID = 0;
        titleView.setSize(getWidth(), Tool.getMath(120, 80, getHeight() / 11));
        start();
        new TipView(map, "近期异常日志").start();
    }

    private static class LogText {
        private Log.Type type;
        private String log;
    }
}
