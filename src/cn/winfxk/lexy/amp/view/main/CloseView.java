package cn.winfxk.lexy.amp.view.main;

import cn.winfxk.lexy.amp.Image;
import cn.winfxk.lexy.amp.Main;
import cn.winfxk.lexy.amp.tool.view.MyJPanel;
import cn.winfxk.lexy.amp.tool.view.image.ImageButton;
import cn.winfxk.lexy.amp.view.log.LogView;
import cn.winfxk.lexy.amp.view.setting.Settinglist;

public class CloseView extends MyJPanel {
    /**
     * 关闭按钮
     */
    private final ImageButton closeView;
    /**
     * 最小化按钮
     */
    private final ImageButton minView;
    /**
     * 设置按钮
     */
    private final ImageButton settingView;
    /**
     * 日志按钮
     */
    private final ImageButton logButton;

    /**
     * 角标管理器View
     */
    public CloseView() {
        super();
        closeView = new ImageButton(Image.getClose());
        closeView.setOnClick(imageButton -> Main.close(0));
        closeView.setOpaque(false);
        add(closeView);
        minView = new ImageButton(Image.getMini());
        minView.setOnClick(imageButton -> MinView.getMain().min());
        minView.setOpaque(false);
        add(minView);
        settingView = new ImageButton(Image.getSetting());
        settingView.setOnClick(imageButton -> Settinglist.getMain().setVisible());
        settingView.setOpaque(false);
        add(settingView);
        logButton = new ImageButton(Image.getLog());
        logButton.setOnClick(imageButton -> LogView.getMain().setVisible());
        logButton.setOpaque(true);
        LogView.setLogButton(logButton);
        add(logButton);
    }

    @Override
    public void start() {
        closeView.setSize(getHeight(), getHeight());
        closeView.setLocation(getWidth() - closeView.getWidth(), 0);
        closeView.setPadding(10);
        closeView.start();
        minView.setSize(closeView.getSize());
        minView.setLocation(closeView.getX() - minView.getWidth() - 5, 0);
        minView.start();
        settingView.setSize(closeView.getSize());
        settingView.setLocation(minView.getX() - settingView.getWidth() - 5, 0);
        settingView.start();
        logButton.setSize(settingView.getSize());
        logButton.setLocation(settingView.getX() - logButton.getWidth() - 5, 0);
        logButton.start();
    }
}
