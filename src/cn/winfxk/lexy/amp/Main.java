package cn.winfxk.lexy.amp;

import cn.winfxk.lexy.amp.tool.Config;
import cn.winfxk.lexy.amp.tool.Tool;
import cn.winfxk.lexy.amp.tool.Utils;
import cn.winfxk.lexy.amp.view.AutoMes;
import cn.winfxk.lexy.amp.view.log.LogView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {
    public static final Gson gson = new GsonBuilder().setLenient().enableComplexMapKeySerialization().serializeNulls().setPrettyPrinting().disableHtmlEscaping().create();
    public static final String MainImageName = "Icon.jpg", ConfigName = "System.yml", KeylistName = "Keylist.yml";
    public static File TaskDataDir = new File(All.baseFile, "TaskData");
    public static File MesDataDir = new File(All.baseFile, "MesData");
    public static final Toolkit toolkit = Toolkit.getDefaultToolkit();
    public static final String[] Reslist = {ConfigName};
    public static final int ScreenWidth, ScreenHeight;
    public static volatile boolean runing = true;
    public static final String Username, PCName, UserHome;
    public static final boolean isTest = false;
    public static final String Version = "2.0.24.04";
    public static final int VersionID = 3;
    public static File KeylistFile;
    /**
     * 用来存储需要忽略的物料清单
     */
    public static File IgnoreFile;
    /**
     * 程序的配置存放路径"/Config文件夹"
     */
    public static File ConfigFile;
    public static Config config;

    static {
        ScreenWidth = (int) toolkit.getScreenSize().getWidth();
        ScreenHeight = (int) toolkit.getScreenSize().getHeight();
        Username = System.getProperty("user.name");
        UserHome = System.getProperty("user.home");
        Map<String, String> map = System.getenv();
        PCName = map.get("COMPUTERNAME");
        if (Username == null || PCName == null) throw new RuntimeException("无法获取用户名和计算机名！");
    }

    public static void input() {
        runing = true;
        try {
            init();
            LogView logView = new LogView();
            logView.start();
            Log.addListener(logView);
            new AutoMes().start();
        } catch (Exception e) {
            Log.e("程序出现异常！即将退出.", e);
            JOptionPane.showMessageDialog(null, "程序出现异常！即将退出。" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            close(-1);
        }
    }

    public static void main(String[] args) {
        Log.i("程序加载...");
        All.DataFile = new File("\\\\kc2268\\AutoMes");
        All.Codeindex = 5;
        Main.ConfigFile = new File(All.DataFile, "Config");
        Main.IgnoreFile = new File(ConfigFile, "ignore.yml");
        File file = new File(ConfigFile, KeylistName);
        if (!file.exists()) try {
            InputStream stream = getStream(KeylistName);
            Utils.writeFile(file, Utils.readFile(stream));
        } catch (Exception e) {
            Log.e("无法初始化关键零部件清单文件！", e);
        }
        input();
    }

    /**
     * 用于执行程序被关闭时需要处理的一些内容
     *
     * @param status 状态码
     */
    public static void close(int status) {
        runing = false;
        Log.i("程序关闭");
        System.exit(status);
    }


    /**
     * 应用程序初始化入口F
     */
    public static void init() {
        Log.i("初始化应用程序中。。");
        File file;
        Log.i("使用数据路径： " + All.DataFile);
        if (!ConfigFile.exists()) if (!ConfigFile.mkdirs()) {
            Log.w("创建数据路径时失败！");
        }
        for (String name : Reslist) {
            file = new File(ConfigFile, name);
            if (!file.exists())
                try {
                    Utils.writeFile(file, Utils.readFile(getStream(name)));
                } catch (Exception e) {
                    if (e.getClass().equals(IOException.class))
                        JOptionPane.showMessageDialog(null, "无法连接至数据路径！请检查。", "错误", JOptionPane.ERROR_MESSAGE);
                    Log.e("初始化文件配置失败！", e);
                    close(-1);
                    return;
                }
            Tool.sleep(100);
        }
        file = new File(ConfigFile, MainImageName);
        try {
            if (!file.exists()) ImageIO.write(ImageIO.read(getStream(MainImageName)), "JPG", file);
        } catch (Exception e) {
            Log.i("释放程序应用图标时出现问题！", e);
            close(-1);
        }
        Image.load();
        config = new Config(new File(ConfigFile, ConfigName));
        KeylistFile = new File(ConfigFile, KeylistName);
    }

    /**
     * @return 返回系统配置对象
     */
    public static Config getConfig() {
        if (config == null) config = new Config(new File(ConfigFile, ConfigName));
        return config;
    }

    /**
     * @return 判断是否有忽略权限
     */
    public static boolean isIgnore() {
        List<String> list = getConfig().getList("忽略权限", new ArrayList<>());
        return isAdmin() || list.contains(Username) || list.contains(PCName);
    }

    /**
     * @return 判断是否有管理员权限
     */
    public static boolean isAdmin() {
        List<String> list = getConfig().getList("管理员", new ArrayList<>());
        return list.contains(Username) || list.contains(PCName);
    }

    /**
     * 根据文件名从应用程序包内提取资源，该资源位于应用程序包'res'文件夹下
     *
     * @param name 需要提取资源的文件名
     * @return 文件流
     * @throws Exception 可能的异常
     */
    public static InputStream getStream(String name) throws Exception {
        String path = "res/" + name;
        InputStream stream = Main.class.getResourceAsStream("/" + path);
        if (stream == null) stream = Files.newInputStream(Paths.get(path));
        return stream;
    }

    public static void reloadCOnfig() {
        if (!config.getFile().delete())
            Log.w("文件删除失败！");
        config = new Config(new File(ConfigFile, ConfigName));
    }
}
