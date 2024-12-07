package cn.winfxk.lexy.amp.view.update;

import cn.winfxk.lexy.amp.tool.Tool;
import cn.winfxk.lexy.amp.tool.view.MyJPanel;
import cn.winfxk.lexy.amp.tool.view.button.Button;

import java.awt.*;

public class BottomView extends MyJPanel {
    public static final Font Openfont = new Font("楷体", Font.BOLD, 15);
    public static final Font font = new Font("楷体", Font.BOLD, 20);
    private final Button download, exit;

    public BottomView() {
        super();
        download = new Button("更新");
        download.setFont(font);
        download.setOnClickListener(event -> Update.getMain().onClickDownload());
        add(download);
        exit = new Button("打开文件所在位置");
        exit.setFont(Openfont);
        exit.setOnClickListener(event -> Update.getMain().onClickOpen());
        add(exit);
    }

    @Override
    public void start() {
        int paddingHeight = Tool.getMath(10, 2, getHeight() / 5);
        int height = getHeight() - paddingHeight * 2;
        int paddintWidth = Tool.getMath(50, 10, getWidth() / 23);
        int width = Tool.getMath(250, 150, (getWidth() - paddintWidth - 100) / 2);
        int x = getWidth() / 2 - (width * 2 + paddintWidth) / 2;
        download.setLocation(x, paddingHeight);
        download.setSize(width, height);
        download.start();
        exit.setSize(download.getSize());
        exit.setLocation(download.getX() + download.getWidth() + paddintWidth, download.getY());
        exit.start();
    }
}
