package cn.winfxk.lexy.amp.excel.factory;

import cn.winfxk.lexy.amp.All;
import cn.winfxk.lexy.amp.Log;
import cn.winfxk.lexy.amp.Main;
import cn.winfxk.lexy.amp.excel.ExcelFactory;
import cn.winfxk.lexy.amp.tool.Config;
import cn.winfxk.lexy.amp.tool.Tool;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 一个用于获取到最新Excel文件路径的封装类
 */

public class ExcelFile implements FileFilter {
    private final ExcelFactory excelFactory;
    public static File NetFile = null;
    private static final Config config = Main.getConfig();
    private static final SimpleDateFormat yyyy = new SimpleDateFormat("yyyy");
    private static final SimpleDateFormat MM = new SimpleDateFormat("MM");
    private static final SimpleDateFormat DD = new SimpleDateFormat("dd");

    /**
     * 一个用于获取到最新Excel文件路径的封装类
     */
    public ExcelFile(ExcelFactory excelFactory) {
        this.excelFactory = excelFactory;
    }

    public File getExfel(File file) {
        if (excelFactory.Excelfile != null) {
            String time = config.getString("计划更新时间", null);
            if (time != null && time.equals(Tool.getDate())) return null;
        }
        if (file == null) file = excelFactory.getExcelPath();
        File[] files = file.listFiles(this);
        if (files == null) {
            Log.w("无法获取计划清单！获取的根文件目录为空！");
            return null;
        }
        return getNewExcelFile(files);
    }

    /**
     * @param year 计划所在的文件夹
     * @return 获取一个最新的计划文件
     */
    protected File getNewExcelFile(File[] year) {
        File yearFile, monthFile, dayFile, Task = null;
        List<File> monthNotFilelist = new ArrayList<>();
        List<File> yearNotFilelist = new ArrayList<>();
        List<File> daysNotFilelist = new ArrayList<>();
        File[] month, days, TaskFiles;
        String Filename;
        boolean isClose;
        String yy = yyyy.format(new Date());
        String mm = MM.format(new Date());
        String dd = DD.format(new Date());
        while (Main.runing) {
            yearFile = null;
            monthFile = null;
            dayFile = null;
            isClose = true;
            for (File f : year) isClose = isClose && yearNotFilelist.contains(f);
            if (isClose) {
                Log.i("所有日期均无法获取有效计划！");
                return null;
            }
            for (File f : year) {
                if ((yearFile == null || yearFile.lastModified() < f.lastModified()) && !yearNotFilelist.contains(f))
                    yearFile = f;
            }
            if (yearFile == null) {
                Log.i("无法获取年度文件夹对象(year)");
                return null;
            }
            month = yearFile.listFiles(this);
            if (month == null) {
                Log.w("该年度无法获取有效月份计划，准备移动至其它年份(" + yearFile.getName() + ")");
                yearNotFilelist.add(yearFile);
                continue;
            }
            for (File f : month) {
                Filename = f.getName();
                if ((monthFile == null || monthFile.lastModified() < f.lastModified()) && !monthNotFilelist.contains(f) && (Filename.contains(mm) || Filename.contains(Tool.ObjToInt(mm) + "")))
                    monthFile = f;
            }
            if (monthFile == null) {
                Log.w("该年度无法获取有效月份计划，准备移动至其它年份(" + yearFile.getName() + ")");
                monthNotFilelist.add(yearFile);
                continue;
            }
            days = monthFile.listFiles(this);
            if (days == null) {
                Log.w("该月度无法获取有效日期计划，准备移动至其它月份(" + monthFile.getName() + ")");
                monthNotFilelist.add(monthFile);
                continue;
            }
            for (File f : days) {
                Filename = f.getName();
                if ((dayFile == null || dayFile.lastModified() < f.lastModified()) && !daysNotFilelist.contains(f) && (Filename.contains(mm) || Filename.contains(Tool.ObjToInt(mm) + "")) && (Filename.contains(dd) || Filename.contains(Tool.ObjToInt(dd) + "")))
                    dayFile = f;
            }
            if (dayFile == null) for (File f : days) {
                if ((dayFile == null || dayFile.lastModified() < f.lastModified()) && !daysNotFilelist.contains(f))
                    dayFile = f;
            }
            if (dayFile == null) {
                Log.w("该月度无法获取有效日期计划，准备移动至其它月份(" + monthFile.getName() + ")");
                monthNotFilelist.add(monthFile);
                continue;
            }
            TaskFiles = dayFile.listFiles(excelFactory);
            if (TaskFiles == null) {
                Log.w("当日无有效计划，准备切换到其他日期(" + dayFile.getName() + ")");
                daysNotFilelist.add(dayFile);
                continue;
            }
            for (File f : TaskFiles) {
                Filename = f.getName();
                if (!Filename.contains(excelFactory.getKey()) || f.length() < 102400) continue;
                Task = f;
            }
            if (Task == null) {
                Log.w("当日无有效计划，准备切换到其他日期(" + dayFile.getName() + ")");
                daysNotFilelist.add(dayFile);
                continue;
            }
            break;
        }
        if (Task == null) {
            Log.e("无法获取生产计划！最终获取的文件为空！");
        } else {
            Log.i("已获取计划：" + (Filename = Task.getName().toLowerCase(Locale.ROOT)));
            NetFile = Task;
            SimpleDateFormat format;
            boolean isThisTime = false;
            Date date = new Date();
            for (String mat : ExcelFactory.DateFormat) {
                format = new SimpleDateFormat(mat);
                if (Filename.contains(excelFactory.getKey()) && Filename.contains(format.format(date))) {
                    isThisTime = true;
                    Log.i("已将正在使用的滚动计划表同步至最新计划！");
                    break;
                }
            }
            if (isThisTime) try {
                File file = new File(All.DataFile, "excel" + Filename.substring(Filename.lastIndexOf(".")));
                if (!file.exists() || file.lastModified() != Task.lastModified()) {
                    Log.i("正在备份滚动计划Excel文件。");
                    Copy(Task, file);
                    if (!file.setLastModified(Task.lastModified()))
                        Log.w("在下载滚动计划时出现问题，无法将下载的文件修改日期与源文件同步。");
                    config.set("计划更新时间", Tool.getDate()).set("计划文件名称", Task.getName()).save();
                }
                Task = file;
            } catch (Exception e) {
                Log.w("无法下载计划文件到本地！", e);
            }
        }
        return Task;
    }

    /**
     * 将滚动计划放至本地备份使用已节约性能
     *
     * @param task 滚动计划源文件
     * @param file 需要存放的位置
     * @throws Exception 可能的异常
     */
    private void Copy(File task, File file) throws Exception {
        if (!file.getParentFile().exists())
            if (!file.getParentFile().mkdirs()) Log.w("创建文件夹" + file.getParent() + "时失败！");
        FileInputStream input = new FileInputStream(task);
        FileOutputStream output = new FileOutputStream(file);
        byte[] bytes = new byte[1024];
        long surplusSize = task.length();
        while (input.read(bytes) != -1) {
            output.write(bytes);
            surplusSize -= bytes.length;
            if (surplusSize <= 0) break;
            bytes = new byte[(int) Math.min(surplusSize, 1024)];
        }
        Log.i("滚动计划下载完成！");
        input.close();
        output.close();
    }

    @Override
    public boolean accept(File pathname) {
        return pathname.isDirectory();
    }
}
