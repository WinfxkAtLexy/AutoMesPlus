package cn.winfxk.lexy.amp.view.main.view;

import cn.winfxk.lexy.amp.tool.view.MyJPanel;
import cn.winfxk.lexy.amp.tool.view.progress.Progres;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;


/**
 * 一个用于显示各种进度条的容器
 */
public class Progress extends MyJPanel {
    private final static Border border = BorderFactory.createLineBorder(Color.GRAY);
    private final Progres modelProgres;
    private final Progres taskProgres;
    private final Progres lineProgres;
    private final Progres mesProgres;
    private static Progress main;

    /**
     * 一个用于显示各种进度条的容器
     */
    public Progress() {
        super();
        main = this;
        modelProgres = new Progres();
        modelProgres.setToolTipText("型号明细更新进度");
        add(modelProgres);
        taskProgres = new Progres();
        taskProgres.setToolTipText("滚动计划明细更新进度");
        add(taskProgres);
        lineProgres = new Progres();
        lineProgres.setToolTipText("时间线更新进度");
        add(lineProgres);
        mesProgres = new Progres();
        mesProgres.setToolTipText("MES数据更新进度");
        add(mesProgres);
        setBorder(border);
    }

    public static Progres getLineProgres() {
        return main.lineProgres;
    }

    public static Progres getTaskProgres() {
        return main.taskProgres;
    }

    public static Progres getModelProgres() {
        return main.modelProgres;
    }

    public static Progres getMesProgres() {
        return main.mesProgres;
    }

    @Override
    public void start() {
        int width = getWidth() - 8;
        int height = 6;
        int margin = 5;
        modelProgres.setSize(width, height);
        modelProgres.setLocation(getWidth() / 2 - width / 2, margin);
        modelProgres.start();
        taskProgres.setSize(modelProgres.getSize());
        taskProgres.setLocation(modelProgres.getX(), modelProgres.getY() + modelProgres.getHeight() + margin);
        taskProgres.start();
        lineProgres.setSize(taskProgres.getSize());
        lineProgres.setLocation(taskProgres.getX(), taskProgres.getY() + taskProgres.getHeight() + margin);
        lineProgres.start();
        mesProgres.setSize(lineProgres.getSize());
        mesProgres.setLocation(lineProgres.getX(), lineProgres.getY() + lineProgres.getHeight() + margin);
        mesProgres.start();
        setSize(getWidth(), mesProgres.getY() + mesProgres.getHeight() + margin);
    }
}
