package cn.winfxk.lexy.amp.view.setting.res.view.task;

import cn.winfxk.lexy.amp.Log;
import cn.winfxk.lexy.amp.excel.factory.Task;
import cn.winfxk.lexy.amp.tool.Tool;
import cn.winfxk.lexy.amp.tool.view.JOptionPane;
import cn.winfxk.lexy.amp.tool.view.OnclickListener;
import cn.winfxk.lexy.amp.tool.view.list.adapter.BaseItemView;
import cn.winfxk.lexy.amp.view.setting.res.view.make.MakeTip;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class ItemView extends BaseItemView {
    private static final SimpleDateFormat hintFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat TitleFormat = new SimpleDateFormat("yyyy年MM月dd日");
    private final static Font titleFont = new Font("楷体", Font.BOLD, 20);
    private final static Font hintFont = new Font("楷体", Font.BOLD, 15);
    private final JLabel title, hint;

    public ItemView(Data data) {
        super();
        title = new JLabel(TitleFormat.format(data.time) + "滚动计划", SwingConstants.CENTER);
        title.setOpaque(false);
        title.setLocation(0, 0);
        title.setFont(titleFont);
        add(title);
        hint = new JLabel("计划更新日期：" + hintFormat.format(data.file.lastModified()) + "   共计" + data.list.size() + "笔",SwingConstants.CENTER);
        hint.setFont(hintFont);
        hint.setOpaque(false);
        add(hint);
        setOnclickListener(view -> {
            Map<String, OnclickListener> map = new HashMap<>();
            for (Task task : data.list)
                map.put(task.S1500 + "/" + task.Model + "  " + task.Count + "台", view1 -> {
                });
            new MakeTip(map, title.getText(), event -> {
                if (JOptionPane.showConfirmDialog(null, "您确定删除这个文件吗？如果这个文件确实是错误文件删除后无影响\n但若此文件正在倍应用程序使用，此操作可能会导致应用程序出现异常！",
                        "警告", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) != 0) return;
                if (Tool.delete(data.file)) {
                    Log.i("已删除缓存文件：" + data.file.getAbsolutePath());
                    JOptionPane.showMessageDialog(null, "操作完成！", "提示", JOptionPane.PLAIN_MESSAGE);
                } else {
                    Log.w("已执行删除缓存操作，但是操作可能没有完成。File: " + data.file.getAbsolutePath());
                    JOptionPane.showMessageDialog(null, "已执行删除操作，但是操作可能并未成功！请稍后重试");
                }
            }).start();
        });
    }

    @Override
    public void start() {
        title.setSize(getWidth(), (int) (getHeight() * 0.6));
        hint.setSize(getWidth(), getHeight() - title.getHeight() - 1);
        hint.setLocation(0, title.getHeight());
    }
}
