package cn.winfxk.lexy.amp.excel;

import cn.winfxk.lexy.amp.All;
import cn.winfxk.lexy.amp.Log;
import cn.winfxk.lexy.amp.Main;
import cn.winfxk.lexy.amp.excel.factory.ExcelFile;
import cn.winfxk.lexy.amp.excel.factory.Keymap;
import cn.winfxk.lexy.amp.excel.factory.ReadExcel;
import cn.winfxk.lexy.amp.excel.factory.Task;
import cn.winfxk.lexy.amp.tool.Config;
import cn.winfxk.lexy.amp.tool.Tool;
import cn.winfxk.lexy.amp.tool.view.JOptionPane;
import cn.winfxk.lexy.amp.tool.view.StartView;
import cn.winfxk.lexy.amp.view.AutoMes;

import java.io.File;
import java.io.FileFilter;
import java.text.SimpleDateFormat;
import java.util.*;

public abstract class ExcelFactory implements StartView, Runnable, FileFilter {
    /**
     * 所有可能的日期格式
     */
    public static final String[] DateFormat = {"M/d", "M/dd", "MM/d", "MM/dd", "MM月d日", "M月dd日", "M月d日", "MM月dd日", "M-d", "MM-dd", "MM-d", "M-dd", "M/dd日", "MM/dd日", "M/d日", "MM/d日"};
    public static final SimpleDateFormat Filename = new SimpleDateFormat("yyyy/MM/dd");
    /**
     * 获取最新每日计划文件的封装类
     */
    protected final ExcelFile excelFile;
    /**
     * 读取每日计划文件内所有计划明细的封装类
     */
    private final ReadExcel readExcel;
    /**
     * 计划存放的路径，这个值由继承类提供
     */
    protected File ExcelPath = null;
    /**
     * 本地计划文件在本地存放的路径
     */
    public File Excelfile;


    public ExcelFactory() {
        excelFile = new ExcelFile(this);
        readExcel = new ReadExcel(this);
    }

    /**
     * 将已读取的所有计划都转储到本地
     *
     * @param list 需要转储的计划
     */
    private void SaveAllTask(List<Task> list) {
        Log.i("正在将所有已读取的滚动计划表转储到本地");
        Map<Long, List<Map<Integer, Object>>> map = new HashMap<>();
        String dateString;
        List<Map<Integer, Object>> taskList;
        long time;
        for (Task task : list) {
            for (Integer i = Task.Keymap.StartTime; i < task.map.size(); i++) {
                dateString = Tool.objToString(task.map.get(i));
                if (dateString == null || dateString.isEmpty() || !Task.allTaskTimes.containsKey(i)) continue;
                time = Task.allTaskTimes.get(i);
                if (!map.containsKey(time)) map.put(time, new ArrayList<>());
                taskList = map.get(time);
                taskList.add(task.map);
            }
        }
        Log.i("已获取" + map.size() + "天的计划，正在进行转储操作。");
        Config config;
        File file;
        int error = map.size();
        for (Map.Entry<Long, List<Map<Integer, Object>>> entry : map.entrySet())
            try {
                file = getTaskFile(entry.getKey());
                if (!file.getParentFile().exists()) if (!file.getParentFile().mkdirs()) continue;
                config = new Config(file);
                config.set("Keymap", Keymap.getData(Task.Keymap));
                config.set("Task", entry.getValue()).save();
                error--;
            } catch (Exception e) {
                Log.e("在转储滚动计划时出现异常!", e);
            }
        if (error > 0)
            Log.w("滚动计划转储已完成！但是仍有" + error + "笔转储失败！请检查配置。");
        else Log.i("滚动计划转储完毕！");
    }

    /**
     * 更新计划内容
     */
    public void ReloadTask() {
        try {
            SaveAllTask(getAllTasks());
        } catch (Exception e) {
            Log.e("无法获取计划文件内的数据！", e);
        }
    }

    /**
     * 根据提供的文件对象获取计划的详细数据
     *
     * @param file 需要提取数据的滚动计划转储文件
     * @return 滚动计划清单
     */
    public static List<Task> getTaskByFile(File file) {
        if (!file.exists()) {
            Log.e("想要提取数据的计划文件不存在！" + file.getAbsolutePath());
            return null;
        }
        try {
            Config config = new Config(file);
            List<Object> objList = config.getList("Task");
            Keymap keymap = Keymap.getKeymap(config.getMap("Keymap"));
            if (objList == null || objList.isEmpty()) {
                Log.w("从文件" + file.getAbsolutePath() + "读取的滚动计划数据为空！请检查文件是否被人为删除或应用程序出现异常。");
                return null;
            }
            List<Task> list = new ArrayList<>();
            List<String> taskS1500Model = new ArrayList<>();
            Object o;
            Task task;
            String S1500Model;
            for (int i = 0; i < objList.size(); i++) {
                o = objList.get(i);
                if (!(o instanceof Map)) {
                    Log.w("在从文件" + file.getAbsolutePath() + "提取滚动计划时解析到一条非序列化数据！请检查转储的滚动计划数据是否被人为篡改！Key:" + i);
                    continue;
                }
                try {
                    task = new Task((Map<Integer, Object>) o, keymap);
                } catch (Exception e1) {
                    Log.w("在从文件" + file.getAbsolutePath() + "提取滚动计划时反序列化失败！请检查被转储的滚动计划数据是否被人为篡改,Key:" + i);
                    continue;
                }
                S1500Model = task.S1500 + task.Model;
                if (!taskS1500Model.contains(S1500Model)) {
                    taskS1500Model.add(S1500Model);
                    list.add(task);
                }
            }
            return list;
        } catch (Exception e) {
            Log.e("在读取滚动计划时出现异常！请检查文件是否被占用或其它问题。", e);
            return null;
        }
    }

    /**
     * 根据提供的时间返回滚动计划
     *
     * @param time 需要提取计划的日期
     * @return 计划的文件对象
     */
    public static File getTaskFile(long time) {
        return new File(Main.TaskDataDir, Filename.format(new Date(time)) + ".xk");
    }

    /**
     * @return 获取计划文件内的所有数据清单
     */
    public List<Task> getAllTasks() {
        List<Task> list = new ArrayList<>();
        List<Object> allTask = readExcel.getAllTask();
        Task.reloadKey(allTask);
        Task task;
        if (allTask != null) for (Object o : allTask)
            if (o instanceof Map) {
                task = new Task((Map<Integer, Object>) o);
                if (task.isLoad() && isS1500(task)) list.add(task);
            }
        return list;
    }

    /**
     * @param task 需要判断的计划
     * @return 用于判断是否是一个正常的计划，判断依据是根据订单号前缀进行判断
     */
    protected boolean isS1500(Task task) {
        for (String s : getS1500Key()) {
            if (task.S1500 == null || task.S1500.isEmpty()) continue;
            if (task.S1500.equals("试产")) return true;
            if (task.S1500.toLowerCase(Locale.ROOT).startsWith(s.toLowerCase(Locale.ROOT))) return true;
        }
        return false;
    }

    /**
     * @return 返回计划所在的路径
     */
    public abstract File getExcelPath();

    /**
     * @return 返回订单号的前缀，用于区分主线计划和一堆无用数据
     */
    public abstract String[] getS1500Key();

    /**
     * @return 计划关键词，如'制造五部'
     */
    public abstract String getKey();

    @Override
    public void start() {
        Log.i("启动任务：计划更新器；关键词：" + getKey());
        new Thread(this).start();
    }

    /**
     * 循环计时器，用于定时更新计划
     */
    @Override
    public void run() {
        int Time = 0;
        File file;
        while (Main.runing) {
            Tool.sleep(1000);
            if (AutoMes.isIsSleep()) continue;
            if (Time-- > 0) continue;
            if (!Main.runing) break;
            Log.i("准备更新生产计划...");
            Time = Math.max(600, Main.getConfig().getInt("计划更新间隔", 3600));
            file = excelFile.getExfel(null);
            if (Excelfile == null && file == null) {
                if (Main.getConfig().getString("计划文件名称") == null) {
                    Log.e("无法通过任何方式获取滚动计划文件！无论是本地亦或是数据交换均获取失败。");
                } else {
                    Excelfile = new File(All.DataFile, Main.getConfig().getString("计划文件名称"));
                    if (!Excelfile.exists())
                        Log.e("无法通过任何方式获取滚动计划文件！无论是本地亦或是数据交换均获取失败。");
                    if (!Excelfile.exists()) Excelfile = null;
                }
                if (Excelfile == null) {
                    File xlsxFile = new File(All.DataFile, "excel.xlsx");
                    File xlsFile = new File(All.DataFile, "excel.xls");
                    if (xlsFile.exists() && xlsxFile.exists()) {
                        Excelfile = xlsxFile.lastModified() > xlsFile.lastModified() ? xlsxFile : xlsFile;
                    } else if (xlsFile.exists()) Excelfile = xlsFile;
                    else if (xlsxFile.exists()) Excelfile = xlsxFile;
                    if (Excelfile != null) Log.i("所有获取滚动计划的方式均失败！使用缓存滚动计划文件。");
                    else {
                        Log.e("无法获取滚动计划文件！请检查是否能成功连接至新数据交换且已输入密码。");
                        JOptionPane.showMessageDialog(null, "无法获取滚动计划文件，请检查计算机是否能连接至新数据交换。", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else if (file != null) Excelfile = file;
            if (Excelfile != null) {
                Log.i("使用文件：" + Excelfile.getAbsolutePath());
                ReloadTask();
            } else Log.w("找不到滚动计划Excel文件！请检查。");
            Log.i("生产计划更新完毕！");
        }
    }

    @Override
    public boolean accept(File pathname) {
        String Filename = pathname.getName().toLowerCase(Locale.ROOT);
        return pathname.isFile() && (Filename.endsWith(".xlsx") || Filename.endsWith(".xls"));
    }
}
