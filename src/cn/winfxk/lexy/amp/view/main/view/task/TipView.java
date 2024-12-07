package cn.winfxk.lexy.amp.view.main.view.task;

import cn.winfxk.lexy.amp.Main;
import cn.winfxk.lexy.amp.tool.Tool;
import cn.winfxk.lexy.amp.tool.view.MyJPanel;
import cn.winfxk.lexy.amp.tool.view.button.Button;
import cn.winfxk.lexy.amp.tool.view.list.ListView;
import cn.winfxk.lexy.amp.tool.view.list.adapter.ItemClickListener;
import cn.winfxk.lexy.amp.view.AutoMes;
import cn.winfxk.lexy.amp.view.main.view.task.click.ClickAdapter;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Map;

public class TipView extends MyJPanel implements FocusListener {
    private static final int frameWidth = Math.min(500, Main.ScreenWidth);
    private final Map<String, ItemClickListener> onClickListeners;
    private final JFrame frame = new JFrame();
    private final ClickAdapter adapter;
    private final ListView listView;
    private boolean isInit = false;
    private final Button button;

    public TipView(Map<String, ItemClickListener> onClickListeners, String Title) {
        this.onClickListeners = onClickListeners;
        frame.setSize(frameWidth, 500);
        frame.setTitle(Title);
        frame.setIconImage(AutoMes.frame.getIconImage());
        frame.setContentPane(this);
        frame.addFocusListener(this);
        frame.addComponentListener(this);
        listView = new ListView(adapter = new ClickAdapter(this));
        listView.setLocation(0, 0);
        add(listView);
        button = new Button("关闭");
        button.setOnClickListener(event -> frame.dispose());
        add(button);
    }

    @Override
    public void start() {
        if (!isInit) {
            int height = Math.max(500, 20 + Math.min((adapter.getSize() + 2) * adapter.getItemHeight(), Main.ScreenHeight - adapter.getItemHeight()));
            frame.setSize(frameWidth, height);
            frame.setLocation(Main.ScreenWidth / 2 - frame.getWidth() / 2, Main.ScreenHeight / 2 - frame.getHeight() / 2);
            listView.setSize(getWidth(), height - adapter.getItemHeight() * 2);
            isInit = true;
        } else listView.setSize(getWidth(), frame.getHeight() - adapter.getItemHeight() * 2);
        setSize(frame.getSize());
        setSize(frame.getSize());
        adapter.UpdateAdapter();
        new Thread(() -> {
            Tool.sleep(300);
            adapter.UpdateAdapter();
        }).start();
        button.setSize(Math.max(100, Math.min(200, getWidth() / 2)), adapter.getItemHeight());
        button.setLocation(getWidth() / 2 - button.getWidth() / 2, listView.getY() + listView.getHeight() + 10);
        button.start();
        if (!frame.isVisible()) frame.setVisible(true);
    }

    public Map<String, ItemClickListener> getOnClickListeners() {
        return onClickListeners;
    }

    @Override
    public void focusGained(FocusEvent e) {

    }

    @Override
    public void focusLost(FocusEvent e) {
        frame.dispose();
    }
}
