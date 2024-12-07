package cn.winfxk.lexy.amp.view.setting.res.view;

import cn.winfxk.lexy.amp.Image;
import cn.winfxk.lexy.amp.tool.Tool;
import cn.winfxk.lexy.amp.tool.view.MyJPanel;
import cn.winfxk.lexy.amp.tool.view.image.ImageView;
import cn.winfxk.lexy.amp.tool.view.list.ListView;
import cn.winfxk.lexy.amp.view.setting.TitleView;
import cn.winfxk.lexy.amp.view.setting.res.view.task.PageView;
import cn.winfxk.lexy.amp.view.setting.res.view.task.TaskAdapter;

import javax.swing.*;

import static cn.winfxk.lexy.amp.Main.ScreenHeight;
import static cn.winfxk.lexy.amp.Main.ScreenWidth;

public class Task extends MyJPanel {
    private static final int defWidth = Math.min(1000, ScreenWidth), defHeight = Math.min(800, ScreenHeight);
    private static final JFrame frame = new JFrame();
    private final TaskAdapter adapter;
    private final TitleView titleView;
    private final PageView pageView;
    private final ListView listView;
    private static Task main;

    public Task() {
        super();
        main = this;
        frame.setTitle("资源管理");
        frame.setIconImage(Image.getIcon());
        frame.setSize(defWidth, defHeight);
        frame.addComponentListener(this);
        frame.setLocation(ScreenWidth / 2 - frame.getWidth() / 2, ScreenHeight / 2 - frame.getHeight() / 2);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setContentPane(this);
        setSize(frame.getSize());
        titleView = new TitleView();
        titleView.setLocation(0, 0);
        add(titleView);
        add(pageView = new PageView(this));
        add(listView = new ListView(adapter = new TaskAdapter(this)));
    }

    @Override
    public void start() {
        titleView.setSize(getWidth(), Tool.getMath(200, 100, getHeight() / 7));
        titleView.start();
        pageView.setSize(getWidth(), 40);
        pageView.setLocation(0, titleView.getY() + titleView.getHeight());
        pageView.start();
        listView.setLocation(0, pageView.getHeight() + pageView.getY());
        listView.setSize(getWidth(), getHeight() - listView.getY() - 10);
        adapter.UpdateAdapter(System.currentTimeMillis());
    }

    public static void setVisible() {
        if (main == null) new Task();
        if (!frame.isVisible()) frame.setVisible(true);
        frame.setFocusable(true);
        main.start();
    }

    public static Task getMain() {
        return main;
    }

    public void onClickUp(ImageView imageView) {
    }

    public void onClickDown(ImageView imageView) {
    }

    public PageView getPageView() {
        return pageView;
    }
}
