package cn.winfxk.lexy.amp;

import cn.winfxk.lexy.amp.tool.Tool;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Log {
    public static final List<LogListener> LogListenerList = new ArrayList<>();
    private static final SimpleDateFormat date = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]");

    public static void i(String str) {
        onListener(str, Type.Info, null);
    }

    public static void i(Exception exc) {
        onListener(null, Type.Info, exc);
    }

    public static void i(String str, Exception exc) {
        onListener(str, Type.Info, exc);
    }

    public static void w(String str) {
        onListener(str, Type.Warning, null);
    }

    public static void w(Exception exc) {
        onListener(null, Type.Warning, exc);
    }

    public static void w(String str, Exception exc) {
        onListener(str, Type.Warning, exc);
    }

    public static void e(String str) {
        onListener(str, Type.Error, null);
    }

    public static void e(Exception exc) {
        onListener(null, Type.Error, exc);
    }

    public static void e(String str, Exception exc) {
        onListener(str, Type.Error, exc);
    }

    /**
     * @return 返回输出日志时使用的日期
     */
    public static String getTime() {
        return date.format(new Date());
    }

    private static void onListener(Object obj, Type type, Exception exc) {
        String str;
        String format = getTime();
        if (exc != null) {
            StringWriter stringWriter = new StringWriter();
            exc.printStackTrace(new PrintWriter(stringWriter));
            str = stringWriter.toString();
        } else str = null;
        String objToString = Tool.objToString(obj, null);
        String log = (objToString == null ? date : "") + (str == null ? "" : str + "\n") + (objToString != null ? "\u001b[30;0m" + format + type.getTitle() + type.getColor() + obj : "") + "\u001b[30;0m";
        System.out.println(log);
        new Thread(() -> {
            if (!LogListenerList.isEmpty())
                for (LogListener logListener : new ArrayList<>(LogListenerList))
                    if (logListener != null) logListener.onLogListener(format, obj, type, exc);
        }).start();
    }

    public static boolean removeListener(LogListener logListener) {
        if (logListener == null || !LogListenerList.contains(logListener)) return false;
        LogListenerList.remove(logListener);
        return true;
    }

    public static boolean removeListener(int i) {
        if (i < 0 || i >= LogListenerList.size()) return false;
        LogListenerList.remove(i);
        return true;
    }

    public static void addListener(LogListener logListener) {
        if (logListener == null || LogListenerList.contains(logListener)) return;
        LogListenerList.add(logListener);
    }

    public interface LogListener {
        void onLogListener(String str, Object obj, Log.Type type, Exception exc);
    }

    public enum Type {
        Error("\u001b[31;1m", "Error: ", "red"),
        Warning("\u001b[33;1m", "Warn: ", "yellow"),
        Info("\u001b[0m", "Info: ", "black");
        public static final String defColor = "\u001b[30;0m";
        private final String Color;
        private final String Title;
        private final String c;

        Type(String str, String str2, String c) {
            this.Color = str;
            this.c = c;
            this.Title = str2;
        }


        public String getHtmlColor() {
            return c;
        }

        public String getTitle() {
            return this.Title;
        }

        public String getColor() {
            return this.Color;
        }
    }
}
