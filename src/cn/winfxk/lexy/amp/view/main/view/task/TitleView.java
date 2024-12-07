package cn.winfxk.lexy.amp.view.main.view.task;

import cn.winfxk.lexy.amp.Image;
import cn.winfxk.lexy.amp.tool.view.JOptionPane;
import cn.winfxk.lexy.amp.tool.view.MyJPanel;
import cn.winfxk.lexy.amp.tool.view.image.ImageButton;
import cn.winfxk.lexy.amp.view.main.view.Tasklist;

import javax.swing.*;
import java.awt.*;

public class TitleView extends MyJPanel {
    public final static Font font = new Font("楷体", Font.BOLD, 30);
    public final ImageButton pageUp, pageDown, lockButton, searchButton;
    private volatile transient boolean isSearch = false;
    private volatile boolean lock = true;
    private static TitleView main;
    private final JLabel title;

    public TitleView() {
        super();
        main = this;
        title = new JLabel("", SwingConstants.CENTER);
        title.setOpaque(false);
        title.setFont(font);
        add(title, CENTER_ALIGNMENT);
        pageUp = new ImageButton(Image.getPageUp());
        pageUp.setLocation(0, 0);
        pageUp.setOnClick(imageButton -> TaskAdapter.pageUp());
        add(pageUp);
        pageDown = new ImageButton(Image.getPageDown());
        pageDown.setOnClick(imageButton -> TaskAdapter.pageDown());
        add(pageDown);
        lockButton = new ImageButton(Image.getLock());
        lockButton.setOnClick(imageButton -> this.clickLock());
        add(lockButton);
        searchButton = new ImageButton(Image.getSearch());
        searchButton.setOnClick(imageButton -> {
            if (isSearch) closeSearch();
            else this.clickSearch();
        });
        add(searchButton);
    }

    private void closeSearch() {
        isSearch = false;
        pageUp.setEnabled(true);
        pageDown.setEnabled(true);
        lockButton.setEnabled(true);
        cn.winfxk.lexy.amp.view.main.view.model.TitleView.getMain().setImageButtonEnabled(true);
        searchButton.setImage(Image.getSearch());
        searchButton.setEnabled(true);
        searchButton.start();
        lock = true;
        lockButton.setImage(lock ? Image.getLock() : Image.getUnlock());
        lockButton.start();
        Tasklist.getMain().getListView().setAdapter(Tasklist.getMain().adapter);
    }

    public static TitleView getMain() {
        return main;
    }

    public void setSearchButtonEnabled(boolean enabled) {
        searchButton.setEnabled(enabled);
    }

    private void clickSearch() {
        String key = JOptionPane.showInputDialog(null, "请输入想要搜索的内容", "提示", JOptionPane.PLAIN_MESSAGE);
        if (key == null) return;
        if (key.isEmpty()) {
            JOptionPane.showMessageDialog(null, "请输入想要搜索的内容", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        isSearch = true;
        pageUp.setEnabled(false);
        pageDown.setEnabled(false);
        lockButton.setEnabled(false);
        cn.winfxk.lexy.amp.view.main.view.model.TitleView.getMain().setImageButtonEnabled(false);
        lock = false;
        lockButton.setImage(lock ? Image.getLock() : Image.getUnlock());
        searchButton.setImage(Image.getStopSearch());
        searchButton.start();
        if (SearchAdapter.reload(key)) {
            Tasklist.getMain().getListView().setAdapter(Tasklist.getMain().searchAdapter);
        } else closeSearch();
    }

    public boolean isSearch() {
        return isSearch;
    }

    @Override
    public void start() {
        title.setSize(getWidth() / 2, getHeight());
        title.setLocation(getWidth() / 2 - title.getWidth() / 2, 0);
        pageUp.setSize(getHeight(), getHeight());
        pageUp.start();
        pageUp.setPadding(pageUp.getWidth() / 5);
        pageDown.setSize(getHeight(), getHeight());
        pageDown.setLocation(getWidth() - pageDown.getWidth(), 0);
        pageDown.setPadding(pageDown.getWidth() / 5);
        pageDown.start();
        lockButton.setSize(getHeight(), getHeight());
        lockButton.setLocation(pageDown.getX() - lockButton.getWidth() - 10, 0);
        lockButton.setPadding(lockButton.getWidth() / 3);
        lockButton.start();
        searchButton.setSize(lockButton.getSize());
        searchButton.setPadding(lockButton.getPadding());
        searchButton.setLocation(pageUp.getX() + pageUp.getWidth() + 10, 0);
        searchButton.start();
    }

    /**
     * 更新接触日期锁定的按钮图标
     *
     * @param lock 锁定状态
     */
    public void clickLock() {
        boolean newLock = lock;
        lock = !newLock;
        lockButton.setImage(lock ? Image.getLock() : Image.getUnlock());
        lockButton.start();
    }

    /**
     * @return 是否是已锁定状态
     */
    public static boolean isLock() {
        return main != null && main.lock;
    }

    /**
     * @param title 设置要显示的日期文本
     */
    public static void setTitleText(String title) {
        main.title.setText(title);
    }
}
