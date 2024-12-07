package cn.winfxk.lexy.amp;

import cn.winfxk.lexy.amp.tool.Config;
import cn.winfxk.lexy.amp.tool.Tool;
import cn.winfxk.lexy.amp.tool.Utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static cn.winfxk.lexy.amp.Main.*;

public class All extends JFileChooser {
    /**
     * 程序的数据路径
     */
    public final static File baseFile = new File(System.getProperty("user.home"), "Winfxk/AutoMes");
    public  final static File baseConfigFile = new File(baseFile, "Config.yml");
    public static volatile boolean runing = true;
    public static Config config;
    public static File DataFile;
    public static int Codeindex;
    public static boolean isAll = false;

    public static void main(String[] args) {
        isAll = true;
        Log.i("程序加载...");
        runing = true;
        config = new Config(baseConfigFile);
        String Path = config.getString("BaseConfig", null);
        if (Path == null || Path.isEmpty()) {
            Log.e("配置不存在！准备配置应用程序。");
            new All().start();
            return;
        }
        Codeindex = config.getInt("Code", 5);
        DataFile = new File(Path);
        Input();
    }

    private All() {
        super();
        JFrame frame = new JFrame();
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int width = (int) dimension.getWidth();
        int height = (int) dimension.getHeight();
        try {
            BufferedImage image = (ImageIO.read(getStream("Icon.jpg")));
            frame.setIconImage(image);
        } catch (Exception e) {
            Log.e("无法加载图标！", e);
        }
        setSize(Math.min(1000, width), Math.min(800, height));
        setLocation(Math.max(0, width / 2 - frame.getWidth() / 2), Math.max(0, height / 2 - frame.getHeight() / 2));
        setVisible(true);
        setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        setDialogTitle("请选择记录保存的路径");
        Log.i("显示分厂选择窗口");
        int result = showOpenDialog(frame);
        if (result == JFileChooser.APPROVE_OPTION) {
            File DataFile = getSelectedFile();
            Log.i("已选择数据路径：" + DataFile.getAbsolutePath() + "  准备选择分厂。");
            Object obj = JOptionPane.showInputDialog(null, "请选择分厂", "提示", JOptionPane.WARNING_MESSAGE, null,
                    BranchFactory.Keys, BranchFactory.Factory5.getName());
            if (obj == null) {
                JOptionPane.showMessageDialog(null, "请选择分厂后运行！！", "错误", JOptionPane.ERROR_MESSAGE);
                Log.e("未正确选择分厂,程序退出");
                System.exit(0);
                return;
            }
            BranchFactory magFactory = BranchFactory.getFactory(Tool.objToString(obj));
            Log.i("已选择分厂： " + magFactory.getName());
            Codeindex = magFactory.getID();
            try {
                frame.dispose();
            } catch (Exception e) {
                Log.e("出现异常！", e);
            }
            All.DataFile = DataFile;
            config.set("BaseConfig", DataFile.getAbsolutePath());
            config.set("Code", Codeindex);
            config.save();
            Input();
        } else {
            JOptionPane.showMessageDialog(null, "需选择路径后运行！", "错误", JOptionPane.ERROR_MESSAGE);
            Log.e("未正常选择数据保存路径！程序即将退出。");
            System.exit(0);
        }
    }

    public static void Input() {
        Main.ConfigFile = new File(All.DataFile, "Config");
        Main.IgnoreFile = new File(ConfigFile, "ignore.yml");
        Main.TaskDataDir = new File(All.baseFile, All.Codeindex + "/TaskData");
        Main.MesDataDir = new File(All.baseFile, All.Codeindex + "/MesData");
        Log.i("使用分厂： " + Codeindex);
        File file = new File(ConfigFile, KeylistName);
        if (!file.exists()) {
            try {
                InputStream stream = getStream(Codeindex == 5 ? KeylistName : ("list/" + All.Codeindex + ".yml"));
                Utils.writeFile(file, Utils.readFile(stream));
            } catch (Exception e) {
                Log.e("无法初始化关键零部件清单文件！", e);
            }
        }
        Main.input();
    }

    public void start() {

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
}
