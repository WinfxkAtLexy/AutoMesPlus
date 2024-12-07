package cn.winfxk.lexy.amp.view.setting.res.view.make;

import cn.winfxk.lexy.amp.Image;
import cn.winfxk.lexy.amp.tool.Tool;
import cn.winfxk.lexy.amp.tool.view.MyJPanel;
import cn.winfxk.lexy.amp.tool.view.OnclickListener;
import cn.winfxk.lexy.amp.tool.view.button.Button;
import cn.winfxk.lexy.amp.tool.view.button.OnClickListener;
import cn.winfxk.lexy.amp.tool.view.list.ListView;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

import static cn.winfxk.lexy.amp.Main.ScreenHeight;
import static cn.winfxk.lexy.amp.Main.ScreenWidth;

public class MakeTip extends MyJPanel {
    private static final int defWidth = Math.min(1000, ScreenWidth), defHeight = Math.min(800, ScreenHeight);
    private static final Font buttonFont = new Font("楷体", Font.BOLD, 20);
    private final Map<String, OnclickListener> map;
    private final JFrame frame = new JFrame();
    private final OnClickListener listener;
    private final ButtonView buttonView;
    private final MakeAdapter adapter;
    private final ListView listView;

    public MakeTip(Map<String, OnclickListener> map, String title, OnClickListener listener) {
        super();
        this.map = map;
        this.listener = listener;
        frame.setTitle(title);
        frame.setIconImage(Image.getIcon());
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setLocation(ScreenWidth / 2 - frame.getWidth() / 2, ScreenHeight / 2 - frame.getHeight() / 2);
        frame.setSize(defWidth, defHeight);
        frame.setContentPane(this);
        setSize(frame.getSize());
        listView = new ListView(adapter = new MakeAdapter(this));
        listView.setLocation(0, 0);
        add(listView);
        add(buttonView = new ButtonView(this));
    }

    public Map<String, OnclickListener> getMap() {
        return map;
    }

    @Override
    public void start() {
        buttonView.setSize(getWidth(), 50);
        buttonView.setLocation(0, getHeight() - buttonView.getHeight() - 10);
        buttonView.start();
        listView.setSize(getWidth(), buttonView.getY());
        adapter.UpdateAdapter();
    }

    public static class ButtonView extends MyJPanel {
        private final Button exit, remove;

        private ButtonView(MakeTip main) {
            super();
            remove = new Button("删除");
            remove.setFont(buttonFont);
            remove.setOnClickListener(main.listener);
            add(remove);
            exit = new Button("关闭");
            exit.setFont(buttonFont);
            exit.setOnClickListener(event -> main.frame.dispose());
            add(remove);
        }

        @Override
        public void start() {
            int width = Tool.getMath(200, 100, getWidth() / 7);
            int padding = Tool.getMath(100, 50, getWidth() / 50);
            int x = getWidth() / 2 - (width * 2 + padding) / 2;
            remove.setSize(width, getHeight());
            remove.setLocation(x, 0);
            remove.start();
            exit.setSize(remove.getSize());
            exit.setLocation(remove.getX() + remove.getWidth() + padding, 0);
            exit.start();
        }
    }
}
