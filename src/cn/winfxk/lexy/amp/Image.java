package cn.winfxk.lexy.amp;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;

import static cn.winfxk.lexy.amp.Main.ConfigFile;
import static cn.winfxk.lexy.amp.Main.MainImageName;

public class Image {
    private static BufferedImage icon;
    private static BufferedImage close;
    private static BufferedImage mini;
    private static BufferedImage pageDown;
    private static BufferedImage pageUp;
    private static BufferedImage play;
    private static BufferedImage winfxk;
    private static BufferedImage setting;
    private static BufferedImage sleep;
    private static BufferedImage lock;
    private static BufferedImage unlock;
    private static BufferedImage log;
    private static BufferedImage email;
    private static BufferedImage search;
    private static BufferedImage stopSearch;

    /**
     * @return 获取休眠按钮的图标
     */
    public static BufferedImage getSleep() {
        return sleep;
    }

    /**
     * @return 获取设置按钮的图标
     */
    public static BufferedImage getSetting() {
        return setting;
    }

    /**
     * @return 获取作者图标
     */
    public static BufferedImage getWinfxk() {
        return winfxk;
    }

    /**
     * @return 获取继续运行图标
     */
    public static BufferedImage getPlay() {
        return play;
    }

    /**
     * @return 返回上一页图标
     */
    public static BufferedImage getPageUp() {
        return pageUp;
    }

    /**
     * @return 返回下一页图标
     */
    public static BufferedImage getPageDown() {
        return pageDown;
    }

    /**
     * @return 返回最小化应用程序的图标
     */
    public static BufferedImage getMini() {
        return mini;
    }

    /**
     * @return 返回关闭程序的图标
     */
    public static BufferedImage getClose() {
        return close;
    }

    /**
     * @return 获取应用程序主图标
     */
    public static BufferedImage getIcon() {
        return icon;
    }

    /**
     * @return 返回一个日志相关的图标
     */
    public static BufferedImage getLog() {
        return log;
    }

    /**
     * 初始化应用程序的图像资源
     */
    public static void load() {
        Log.i("开始初始化图像资源");
        icon = getImage(new File(ConfigFile, MainImageName));
        close = getImage("close.png");
        mini = getImage("min.png");
        pageDown = getImage("page_down.png");
        pageUp = getImage("page_up.png");
        play = getImage("play.png");
        winfxk = getImage("Winfxk.png");
        setting = getImage("setting.png");
        sleep = getImage("sleep.png");
        lock = getImage("lock.png");
        unlock = getImage("unlock.png");
        log = getImage("log.png");
        email = getImage("Email.png");
        search = getImage("search.png");
        stopSearch = getImage("stopSearch.png");
    }

    /**
     * @return 取消搜索的按钮图标
     */
    public static BufferedImage getStopSearch() {
        return stopSearch;
    }

    /**
     * @return 返回搜索图标
     */
    public static BufferedImage getSearch() {
        return search;
    }

    /**
     * @return 返回邮件图标
     */
    public static BufferedImage getEmail() {
        return email;
    }

    /**
     * @return 返回解锁按钮的图标
     */
    public static BufferedImage getUnlock() {
        return unlock;
    }

    /**
     * @return 返回锁定的图标
     */
    public static BufferedImage getLock() {
        return lock;
    }

    /**
     * 根据文件对象获取图像资源
     *
     * @param file 文件对象
     * @return 取得的图像
     */
    protected static BufferedImage getImage(File file) {
        if (file == null || !file.exists()) {
            Log.e("无法读取资源！获取文件不存在或为空");
            Main.close(-1);
            return null;
        }
        try {
            BufferedImage image = getImage(Files.newInputStream(file.toPath()));
            if (image == null) {
                Log.e("无法加载文件：" + file.getAbsolutePath());
                Main.close(-1);
                return null;
            }
            return image;
        } catch (Exception e) {
            Log.e("无法加载文件：" + file.getAbsolutePath(), e);
            Main.close(-1);
            return null;
        }
    }

    /**
     * 根据文件名获取图像资源
     *
     * @param name 文件名
     * @return 图像资源
     */
    protected static BufferedImage getImage(String name) {
        if (name == null || name.isEmpty()) {
            Log.e("无法读取资源！文件名为空");
            Main.close(-1);
            return null;
        }
        try {
            BufferedImage image = getImage(Main.getStream(name));
            if (image == null) {
                Log.e("无法读取资源：" + name);
                return null;
            }
            return image;
        } catch (Exception e) {
            Log.e("无法读取资源：" + name, e);
            return null;
        }
    }

    /**
     * 根据流对象获取图像资源
     *
     * @param inputStream 需要获取图像的流
     * @return 图像资源
     */
    protected static BufferedImage getImage(InputStream inputStream) throws Exception {
        if (inputStream == null) {
            Log.e("无法读取资源！获取的流为空");
            Main.close(-1);
            return null;
        }
        return ImageIO.read(inputStream);
    }
}
