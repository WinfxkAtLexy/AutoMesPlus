package cn.winfxk.lexy.amp.mes;

import cn.winfxk.lexy.amp.Log;
import cn.winfxk.lexy.amp.Main;
import cn.winfxk.lexy.amp.tool.Tool;
import cn.winfxk.lexy.amp.tool.Utils;
import cn.winfxk.lexy.amp.view.AutoMes;
import cn.winfxk.lexy.amp.view.main.view.Progress;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 用于从MES服务器抓取数据的线程
 */
public class WebThread extends Thread {
    /**
     * 用于从MES中反向出时间戳的格式
     */
    public static final SimpleDateFormat mesDate = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    public static final SimpleDateFormat PostDate = new SimpleDateFormat("yyyy/MM/dd");
    public static final SimpleDateFormat Date = new SimpleDateFormat("yyyy/MM");
    private volatile boolean runing = false;
    private static final String Postkey;
    private static WebThread main;

    static {
        String string = null;
        try {
            string = Utils.readFile(Main.getStream("mainPost"));
            string = string.replace("{Code}", AutoMes.getBranchFactory().getCode()).replace("\n", "");
            string = "mygo=IQC_lotsql&myto=" + string + "&myto1=" + AutoMes.getBranchFactory().getCode() + "&myt=1";
        } catch (Exception e) {
            Log.e("无法加载MES抓取关键词(mainPost)！", e);
            Main.close(-1);
        }
        Postkey = string;
    }

    /**
     * 用于从MES服务器抓取数据的线程
     */
    public WebThread() {
        super();
        main = this;
        Log.i("MES数据爬虫已启动！");
    }

    public static WebThread getMain() {
        return main;
    }

    /**
     * 更新全局MES数据
     */
    public void reloadAll() {
        int TiQianLiang = Main.getConfig().getInt("MES抓取距离", 1);
        if (TiQianLiang < 1) TiQianLiang = 1;
        Log.i("准备从MES服务器抓取" + TiQianLiang + "月前的全局数据.");
        String TimeStart = Date.format(System.currentTimeMillis() - (86400000L * (TiQianLiang * 30L))) + "/01";
        String web;
        try {
            String post = Postkey.replace("{TimeStart}", TimeStart)
                    .replace("{TimeEnd}", PostDate.format(new Date()));
            web = Tool.getHttp(AutoMes.getBranchFactory().getHost(), "POST", post);
        } catch (Exception e) {
            Log.e("无法从MES服务器抓取全局数据！请检查！", e);
            return;
        }
        if (web == null || web.isEmpty()) {
            Log.e("从MES抓取的全局数据为空！");
            return;
        }
        Log.i("已成功获取" + TimeStart + "至今的MES数据！准备解析数据。");
        HashMap<Object, Object> host = Main.gson.fromJson(web, HashMap.class);
        Map<String, String> data = new HashMap<>();
        String Content, string, dateString, name;
        String[] strings;
        Date parseMesDate;
        for (Map.Entry<Object, Object> entry : host.entrySet()) {
            if (entry.getKey().equals("'0'")) continue;
            string = Tool.objToString(entry.getValue(), null);
            if (string == null || !string.contains(",")) continue;
            strings = string.split(",");
            dateString = strings[strings.length - 2];
            try {
                parseMesDate = mesDate.parse(dateString);
            } catch (Exception e) {
                Log.w("无法解析全局数据！请检查。Date：" + dateString, e);
                continue;
            }
            if (parseMesDate == null) {
                Log.w("无法将取得的全局MES项目日期(" + dateString + ")转换为Date对象！");
                continue;
            }
            name = Serialization.Filename.format(parseMesDate);
            if (!data.containsKey(name)) data.put(name, "");
            Content = data.get(name);
            Content += (Content.isEmpty() ? "" : "\n") + entry.getValue();
            data.put(name, Content);
        }
        Log.i("全局MES数据解析完毕！准备将已解析的数据保存至本地。");
        File file;
        int error = data.size();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            file = new File(Main.MesDataDir, entry.getKey() + ".xk");
            if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
            try {
                Utils.writeFile(file, entry.getValue());
                error--;
            } catch (Exception e) {
                Log.w("无法将解析全局MES数据保存至本地！数据Key:" + entry.getKey(), e);
            }
        }
        if (error == 0) Log.i("已将解析的全局MES数据保存至本地。");
        else Log.w("已将解析的部分全局MES数据保存至本地，其中" + error + "条数据保存失败！");
    }

    /**
     * 更新当日MES数据
     */
    public void reloadDay() {
        Log.i("准备更新当日MES数据.");
        String web;
        try {
            String post = Postkey.replace("{TimeStart}", PostDate.format(new Date()))
                    .replace("{TimeEnd}", PostDate.format(new Date()));
            web = Tool.getHttp(AutoMes.getBranchFactory().getHost(), "POST", post);
        } catch (Exception e) {
            Log.e("无法从MES服务器抓取当日数据！请检查！", e);
            return;
        }
        if (web == null || web.isEmpty()) {
            Log.e("从MES抓取的当日数据为空！");
            return;
        }
        Log.i("已成功获取当日MES数据！准备解析并保存！");
        HashMap<Object, Object> host = Main.gson.fromJson(web, HashMap.class);
        StringBuilder Content = new StringBuilder();
        for (Map.Entry<Object, Object> entry : host.entrySet())
            if (!entry.getKey().equals("'0'")) Content.append((Content.length() == 0) ? "" : "\n").append(entry.getValue());
        try {
            File file = Serialization.getMesDataByTime(new Date());
            if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
            Utils.writeFile(file, Content.toString());
            Log.i("已检解析完毕的当日MES数据保存至本地！");
        } catch (IOException e) {
            Log.w("无法检已解析的当日MES数据保存至本地！", e);
        }
    }

    /**
     * 抓取全局数据的线程
     */
    @Override
    public void run() {
        super.run();
        int time = 0;
        new Thread(this::day).start();
        int Maxtime = Math.max(Main.getConfig().getInt("全局MES刷新间隔", 1200), 500);
        Progress.getMesProgres().setMax(Maxtime * 1000);
        while (Main.runing) {
            for (int i = 0; i < 1000; i++) {
                Tool.sleep(1);
                Progress.getMesProgres().setCurrent(((Maxtime - time) * 1000) + i);
            }
            if (AutoMes.isIsSleep() || time-- > 0) continue;
            if (!Main.runing) break;
            runing = true;
            Maxtime = Math.max(Main.getConfig().getInt("全局MES刷新间隔", 1200), 500);
            Progress.getMesProgres().setMax((time = Maxtime) * 1000);
            try {
                reloadAll();
            } catch (Exception e) {
                Log.e("全局MES数据抓取异常！", e);
            }
            runing = false;
        }
    }

    /**
     * 更新当天的MES数据的线程
     */
    private void day() {
        int time = 0;
        while (Main.runing) {
            Tool.sleep(1000);
            if (AutoMes.isIsSleep() || time-- > 0) continue;
            while (runing) Tool.sleep(1000);
            if (!Main.runing) break;
            runing = true;
            time = Math.max(Main.getConfig().getInt("当日MES刷新间隔", 1200), 500);
            try {
                reloadDay();
            } catch (Exception e) {
                Log.e("当日数据抓取异常！", e);
            }
            runing = false;
        }
    }
}
