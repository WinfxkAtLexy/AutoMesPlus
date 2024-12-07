package cn.winfxk.lexy.amp.view.update;

import cn.winfxk.lexy.amp.All;
import cn.winfxk.lexy.amp.Image;
import cn.winfxk.lexy.amp.Log;
import cn.winfxk.lexy.amp.Main;
import cn.winfxk.lexy.amp.tool.Config;
import cn.winfxk.lexy.amp.tool.Tool;
import cn.winfxk.lexy.amp.tool.view.JOptionPane;
import cn.winfxk.lexy.amp.tool.view.MyJPanel;
import cn.winfxk.lexy.amp.view.setting.TitleView;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Locale;

import static cn.winfxk.lexy.amp.Main.ScreenHeight;
import static cn.winfxk.lexy.amp.Main.ScreenWidth;

public class Update extends MyJPanel implements Runnable {
    private static final int defWidth = Math.min(700, ScreenWidth), defHeight = Math.min(500, ScreenHeight);
    public static final String UpdateHost = "\\\\192.168.0.115\\数据交换\\总装五厂\\罗继\\AutoMes";
    public static final File UpdateFile = new File(UpdateHost);
    public static final String UpdateConfig = "config.yml";
    public final static JFrame frame = new JFrame();
    private final BottomView bottomView;
    private final TitleView titleView;
    private static Update main;
    private final Panel panel;
    private String version;
    private String Host;

    public Update() {
        super();
        new Thread(this::JarFileUpdate).start();
        main = this;
        frame.setTitle("更新管理");
        frame.setIconImage(Image.getIcon());
        frame.setSize(defWidth, defHeight);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addComponentListener(this);
        frame.setLocation(ScreenWidth / 2 - frame.getWidth() / 2, ScreenHeight / 2 - frame.getHeight() / 2);
        frame.setContentPane(this);
        setSize(frame.getSize());
        titleView = new TitleView();
        titleView.setLocation(0, 0);
        add(titleView);
        add(panel = new Panel());
        add(bottomView = new BottomView());
    }

    private void JarFileUpdate() {
        Log.i("文件变动更新服务加载中。");
        File file;
        try {
            String path = getClass().getProtectionDomain().getCodeSource().getLocation().getFile();
            if (path == null || path.isEmpty() || !new File(path).isFile()) {
                Log.e("无法获取当前应用程序路径！文件变动更新服务不可用！");
                return;
            } else file = new File(URLDecoder.decode(path, "UTF-8"));
        } catch (Exception e) {
            Log.e("无法获取当前应用程序路径！文件变动更新服务不可用！", e);
            return;
        }
        if (!file.exists()) {
            Log.e("无法获取当前应用程序路径！文件变动更新服务不可用！");
            return;
        }
        long lastModified = file.lastModified();
        while (Main.runing) {
            Tool.sleep(10000);
            if (lastModified == file.lastModified()) continue;
            try {
                Runtime.getRuntime().exec("java -Dfile.encoding=utf-8 -jar \"" + file.getAbsolutePath() + "\"");
                Main.close(0);
                break;
            } catch (IOException e) {
                Log.e("无法重启应用程序！请手动重启应用。", e);
            }
        }
    }

    @Override
    public void run() {
        if (!All.isAll) return;
        File file = new File(UpdateFile, UpdateConfig);
        Log.i("更新检测中...");
        Tool.sleep(Tool.getRand(10000, 30000));
        if (!file.exists()) {
            Log.w("无法检测更新，无法连接至服务器。");
            return;
        }
        Config config = new Config(file);
        if (!config.containsKey("VersionID")) {
            Log.w("无法检测更新，更新配置不存在。F");
            return;
        }
        int ID = config.getInt("VersionID", -1);
        if (ID <= Main.VersionID) {
            Log.i("暂无新版本。");
            return;
        }
        int versionID;
        panel.setDate(config.getString("Title"), version = config.getString("Version"),
                versionID = config.getInt("VersionID", -1), Host = config.getString("Host"), config.getString("Hint"));
        Log.w("发现新版本！版本：" + Main.Version + "(" + Main.VersionID + ")->" + version + "(" + versionID + ")" + "  下载地址：" + Host);
        start();
    }

    public void onClickDownload() {
        new Thread(() -> {
            frame.dispose();
            if (Host == null || Host.isEmpty()) {
                Log.e("无法识别新版本下载地址！获取到的地址为空！");
                JOptionPane.showMessageDialog(null, "无法识别新版本下载地址！获取到的地址为空！", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            File downloadFIle, thisJarFile;
            String newVersionName = "AutoMes_v" + version + ".jar";
            try {
                String path = getClass().getProtectionDomain().getCodeSource().getLocation().getFile();
                if (path == null || path.isEmpty() || !new File(path).isFile()) {
                    Log.e("无法获取当前应用程序路径！已将下载路径切换为桌面。");
                    downloadFIle = new File(new File(Main.UserHome), newVersionName);
                } else {
                    thisJarFile = new File(URLDecoder.decode(path, "UTF-8"));
                    downloadFIle = new File(thisJarFile.getParent(), newVersionName);
                }
            } catch (Exception e) {
                Log.e("无法生成下载地址！请检查。", e);
                JOptionPane.showMessageDialog(null, "无法生成下载地址！请检查程序是否运行正常。\n" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (Host.toLowerCase().startsWith("http") || Host.toLowerCase().startsWith("ftp")) {
                try {
                    if (Host.toLowerCase(Locale.ROOT).startsWith("https"))
                        Tool.downLoadFromUrlHttps(Host, downloadFIle.getName(), downloadFIle.getPath());
                    else Tool.DownFile(Host, downloadFIle.getName(), downloadFIle.getPath());
                    Log.i("更新完成！准备使用新版本进行启动。");
                    Runtime.getRuntime().exec("java -Dfile.encoding=utf-8 -jar \"" + downloadFIle + "\"");
                    Main.close(0);
                } catch (Exception e) {
                    Log.e("在更新新版本时出现异常！请检查，", e);
                    JOptionPane.showMessageDialog(null, "更新失败！请检查程序运行是否正常。", "错误", JOptionPane.ERROR_MESSAGE);
                }
            } else try {
                File hostFile = new File(Host);
                if (!hostFile.exists()) {
                    Log.e("更新失败！服务器上的新版本不存在！！");
                    JOptionPane.showMessageDialog(null, "更新失败！服务器上的新版本不存在！！", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!downloadFIle.getParentFile().exists()) if (!downloadFIle.getParentFile().mkdirs()) {
                    Log.e("更新失败！无法创建下载路径的目录！");
                    JOptionPane.showMessageDialog(null, "更新失败！创建下载路径时出现异常！", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                byte[] bytes = new byte[1024];
                FileInputStream inputStream = new FileInputStream(hostFile);
                FileOutputStream output = new FileOutputStream(downloadFIle);
                if (!downloadFIle.exists()) if (!downloadFIle.createNewFile()) Log.w("无法创建下载文件！");
                while (inputStream.read(bytes) != -1) {
                    output.write(bytes);
                    bytes = new byte[1024];
                }
                inputStream.close();
                output.close();
                String cmd = "java -Dfile.encoding=utf-8 -jar \"" + downloadFIle + "\"";
                Runtime.getRuntime().exec(cmd);
                Log.i("下载完成！准备执行命令：" + cmd);
                Tool.sleep(500);
                Main.close(0);
            } catch (Exception e) {
                Log.e("在下载新版本时出现异常！请检查程序是否运行正常。", e);
                JOptionPane.showMessageDialog(null, "更新失败！请检查程序运行是否正常。\n" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        }).start();
    }

    public static Update getMain() {
        return main;
    }

    @Override
    public void start() {
        titleView.setSize(getWidth(), Tool.getMath(200, 100, getHeight() / 5));
        titleView.start();
        bottomView.setSize(getWidth(), Tool.getMath(100, 45, getHeight() / 100));
        bottomView.setLocation(0, getHeight() - bottomView.getHeight() - 10);
        bottomView.start();
        panel.setLocation(titleView.getX(), titleView.getY() + titleView.getHeight());
        panel.setSize(getWidth(), bottomView.getY() - panel.getY());
        panel.start();
        if (!frame.isVisible()) frame.setVisible(true);
    }

    /**
     * 打开文件所在位置
     */
    public void onClickOpen() {
        try {
            Runtime.getRuntime().exec("explorer /select,\"" + Host + "\"");
        } catch (IOException e) {
            Log.e("无法打开文件所在位置！", e);
            JOptionPane.showMessageDialog(null, "无法打开文件所在位置！", "错误", JOptionPane.ERROR_MESSAGE);
        }
        frame.dispose();
    }
}
