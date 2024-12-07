package cn.winfxk.lexy.amp.view.setting.view.email;

import cn.winfxk.lexy.amp.tool.view.MyJPanel;
import cn.winfxk.lexy.amp.view.setting.view.email.player.Playerlist;

public class SubPanel extends MyJPanel {
    private final Playerlist playerlist;
    private final ButtonView buttonView;

    public SubPanel() {
        super();
        playerlist = new Playerlist();
        playerlist.setLocation(0, 0);
        add(playerlist);
        add(buttonView = new ButtonView());
    }

    @Override
    public void start() {
        buttonView.setSize(getWidth(), 50);
        buttonView.setLocation(0, getHeight() - buttonView.getHeight() - 10);
        buttonView.start();
        playerlist.setSize(getWidth(), buttonView.getY() - 10);
        playerlist.start();
    }
}
