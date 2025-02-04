package cn.winfxk.lexy.amp.tool;

import com.alibaba.excel.util.StringUtils;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Winfxk
 */
public class Tool implements X509TrustManager, HostnameVerifier {
    private static final String colorKeyString = "123456789abcdef";
    private static final String randString = "-+abcdefghijklmnopqrstuvwxyz_";
    private static final SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
    private static final SimpleDateFormat data = new SimpleDateFormat("yyyy-MM-dd");
    private final static char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
            'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '+', '/'};
    private static final String[] FileSizeUnit = {"B", "KB", "MB", "GB", "TB", "PB", "EB"};
    private static final String isDigit = ".*\\d+.*";

    /**
     * @param obj 需要判断的内容
     * @return 判断一个字符串时候包含数值
     */
    public static boolean isDigit(Object obj) {
        if (obj == null) return false;
        String string = Tool.objToString(obj, null);
        if (string == null || string.isEmpty()) return false;
        return Pattern.matches(isDigit, string);
    }

    public static boolean sleep(long time) {
        try {
            Thread.sleep(time);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 从两个数据中间获取合理的活动数
     *
     * @param max  最大值
     * @param min  最小值
     * @param live 可能活动的值
     * @return 适合的值
     */
    public static int getMath(int max, int min, int live) {
        return Math.min(min, Math.max(max, live));
    }

    /**
     * 判断是否包含中文
     *
     * @param str
     * @return
     */
    public static boolean isChinese(String str) {
        if (StringUtils.isEmpty(str)) return false;
        Matcher m = Pattern.compile("[一-龥|\\！|\\，|\\。|\\（|\\）|\\《|\\》|\\“|\\”|\\？|\\：|\\；|\\【|\\】]").matcher(str);
        return m.find();
    }

    public static <V, T> V[] getArray(List<T> list, V[] v) {
        if (list != null)
            try {
                return list.toArray(v);
            } catch (Exception e) {
                e.printStackTrace();
            }
        return v;
    }

    /**
     * 返回文件大小
     *
     * @param file 要获取大小的文件对象
     * @return
     */
    public static String getSize(File file) {
        return getSize(file.length());
    }

    /**
     * 获取文件大小
     *
     * @param size
     * @return
     */
    public static String getSize(long size) {
        if (size < 1024)
            return size + "B";
        int index = 1;
        float Size = size / (float) 1024;
        while (Size > 1024) {
            index++;
            Size /= 1024;
        }
        return Tool.Double2(Size) + FileSizeUnit[Math.min(index, FileSizeUnit.length - 1)];
    }

    /**
     * 读取包内文件
     *
     * @param FileName
     * @return
     */
    public static InputStream getResourceStream(String FileName) {
        return Tool.class.getResourceAsStream("/res/" + FileName);
    }

    /**
     * 读取包内文本
     *
     * @param FileName
     * @return
     * @throws IOException
     */
    public static String getResource(String FileName) throws IOException {
        return Utils.readFile(Tool.class.getResourceAsStream("/res/" + FileName));
    }

    /**
     * 把10进制的数字转换成64进制
     *
     * @param number
     * @return
     */
    public static String CompressNumber(long number) {
        char[] buf = new char[64];
        int charPos = 64;
        int radix = 1 << 6;
        long mask = radix - 1;
        do {
            buf[--charPos] = digits[(int) (number & mask)];
            number >>>= 6;
        } while (number != 0);
        return new String(buf, charPos, (64 - charPos));
    }

    /**
     * 把64进制的字符串转换成10进制
     *
     * @param decompStr
     * @return
     */
    public static long UnCompressNumber(String decompStr) {
        long result = 0;
        for (int i = decompStr.length() - 1; i >= 0; i--) {
            for (int j = 0; j < digits.length; j++) {
                if (decompStr.charAt(i) == digits[j]) {
                    result += ((long) j) << 6 * (decompStr.length() - 1 - i);
                }
            }
        }
        return result;
    }

    /**
     * 计算两个日期之间相隔多少天
     *
     * @param date1 第一个日期字符串
     * @param date2 第二个日期字符串
     * @return
     */
    public static long getDay(String date1, String date2) {
        return getDay(date1, "yyyy-MM-dd", date2, "yyyy-MM-dd");
    }

    /**
     * 计算两个日期之间相隔多少天
     *
     * @param date1format 第一个日期字符串的格式<yyyy-MM-dd>
     * @param date2format 第二个日期字符串<yyyy-MM-dd>
     */
    public static long getDay(String date1sStr, String date1format, String date2Str, String date2format) {
        Date date1 = parseDate(date1sStr, date1format);
        Date date2 = parseDate(date2Str, date2format);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        long timeInMillis1 = calendar.getTimeInMillis();
        calendar.setTime(date2);
        long timeInMillis2 = calendar.getTimeInMillis();
        return (timeInMillis2 - timeInMillis1) / (1000L * 3600L * 24L);
    }

    /**
     * 将指定的日期字符串转换成日期
     *
     * @param dateStr 日期字符串
     * @return 日期对象
     */
    public static Date parseDate(String dateStr) {
        return parseDate(dateStr, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 将指定的日期字符串转换成日期
     *
     * @param dateStr 日期字符串
     * @param pattern 格式
     * @return 日期对象
     */
    public static Date parseDate(String dateStr, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date date;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            throw new RuntimeException("日期转化错误");
        }
        return date;
    }

    /**
     * 数组相加
     *
     * @param <T>    数组的类型
     * @param arrays 要先加的数组内容
     * @return
     */
    @SafeVarargs
    public static <T> T[] Arrays(T[]... arrays) {
        List<T> list = new ArrayList<>();
        for (T[] t : arrays)
            Collections.addAll(list, t);
        return (T[]) list.toArray();
    }

    /**
     * 将一个不知道什么玩意转换为Long
     *
     * @param obj
     * @return
     */
    public static long objToLong(Object obj) {
        return objToLong(obj, 0L);
    }

    /**
     * 将一个不知道什么玩意转换为Long
     *
     * @param obj
     * @param d
     * @return
     */
    public static long objToLong(Object obj, long d) {
        String string = String.valueOf(obj);
        if (obj == null || string.isEmpty())
            return d;
        try {
            return Long.parseLong(string);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d;
    }

    /**
     * 将一个不知道什么玩意转换为双精
     *
     * @param obj
     * @return
     */
    public static float objToFloat(Object obj) {
        return objToFloat(obj, 0f);
    }

    /**
     * 将一个不知道什么玩意转换为双精
     *
     * @param obj
     * @param d
     * @return
     */
    public static float objToFloat(Object obj, Float d) {
        String string = String.valueOf(obj);
        if (obj == null || string.isEmpty())
            return d;
        try {
            return Float.parseFloat(string);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d;
    }

    /**
     * 将一个不知道什么玩意转换为双精
     *
     * @param obj
     * @return
     */
    public static double objToDouble(Object obj) {
        return objToDouble(obj, 0d);
    }

    /**
     * 将一个不知道什么玩意转换为双精
     *
     * @param obj
     * @param d
     * @return
     */
    public static double objToDouble(Object obj, Double d) {
        String string = String.valueOf(obj);
        if (obj == null || string.isEmpty())
            return d;
        try {
            return Double.parseDouble(string);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return d;
    }

    /**
     * 求最大公约数
     *
     * @param num1
     * @param num2
     * @return
     */
    public static long getGys(long num1, long num2) {
        num1 = Math.abs(num1);
        num2 = Math.abs(num2);
        while (num2 != 0) {
            long remainder = num1 % num2;
            num1 = num2;
            num2 = remainder;
        }
        return num1;
    }

    /**
     * 获取小数长度
     *
     * @param f
     * @return
     */
    public static int getDecimalsLength(float f) {
        String string = String.valueOf(f);
        if (f == 0 || !string.contains("."))
            return 0;
        return string.substring(string.indexOf(".") + 1).length();
    }

    /**
     * 获取字符真实长度
     *
     * @param value
     * @return
     */
    public static int String_length(String value) {
        int valueLength = 0;
        String chinese = "[一-龥]";
        for (int i = 0; i < value.length(); i++) {
            String temp = value.substring(i, i + 1);
            if (temp.matches(chinese))
                valueLength += 2;
            else
                valueLength += 1;
        }
        return valueLength;
    }

    /**
     * 小数转分数
     *
     * @param f int[分子,分母]
     * @return
     */
    public static long[] getGrade(float f, int floatLength) {
        if (f == 0)
            return new long[]{0, 0};
        String string = String.valueOf(f);
        if (!string.contains("."))
            return new long[]{(long) f, 1};
        String sint = string.substring(0, string.indexOf("."));
        String sfloat = string.substring(string.indexOf(".") + 1);
        long Fenmu = 1;
        for (int k = 0; k < floatLength; k++)
            Fenmu *= 10;
        long Fenzi = Long.parseLong(sint + sfloat);
        long lXs = Math.min(Fenzi, Fenmu), j;
        for (j = lXs; j > 1; j--)
            if (Fenzi % j == 0 && Fenmu % j == 0)
                break;
        Fenzi = Fenzi / j;
        Fenmu = Fenmu / j;
        return new long[]{Fenzi, Fenmu};
    }

    /**
     * Object对象转换为String
     *
     * @param obj
     * @return
     */
    public static String objToString(Object obj) {
        return objToString(obj, null);
    }

    /**
     * Object对象转换为String
     *
     * @param obj
     * @param string
     * @return
     */
    public static String objToString(Object obj, String string) {
        if (obj == null)
            return string;
        try {
            return String.valueOf(obj);
        } catch (Exception e) {
            return string;
        }
    }

    /**
     * 将秒长度转换为日期长度
     *
     * @param time 秒长度
     * @return
     */
    public static String getTimeBy(double time) {
        int y = (int) (time / 31556926);
        time = time % 31556926;
        int d = (int) (time / 86400);
        time = time % 86400;
        int h = (int) (time / 3600);
        time = time % 3600;
        int i = (int) (time / 60);
        double s = time % 60;
        return (y > 0 ? y + "年" : "") + (d > 0 ? d + "天" : "") + (h > 0 ? h + "小时" : "") + (i > 0 ? i + "分钟" : "") + (s > 0 ? Double2(s, 1) + "秒" : "");
    }

    /**
     * 判断两个ID是否匹配，x忽略匹配
     *
     * @return
     */
    public static boolean isMateID(Object id1, Object id2) {
        String ID1 = String.valueOf(id1), ID2 = String.valueOf(id2);
        if (ID1 == null || ID2 == null)
            return false;
        if (!ID1.contains(":"))
            ID1 += ":0";
        if (!ID2.contains(":"))
            ID2 += ":0";
        String[] ID1s = ID1.split(":"), ID2s = ID2.split(":");
        if (ID1s[0].equals("x") || ID2s[0].equals("x") || ID1s[0].equals(ID2s[0]))
            return ID1s[1].equals("x") || ID2s[1].equals("x") || ID2s[1].equals(ID1s[1]);
        return false;
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getTime() {
        return time.format(new Date());
    }

    /**
     * 文件复制
     *
     * @param file1 源文件
     * @param file2 目标文件
     */
    public static void Copy(String file1, String file2) throws Exception {
        Copy(new File(file1), new File(file2));
    }

    /**
     * 文件复制
     *
     * @param file1 源文件
     * @param file2 目标文件
     */
    public static void Copy(File file1, File file2) throws Exception {
        if (!file1.exists()) return;
        File Parent = file2.getParentFile();
        if (Parent != null && !Parent.exists()) Parent.mkdirs();
        InputStream fileInputStream = Files.newInputStream(file1.toPath());
        OutputStream fileOutputStream = new FileOutputStream(file2, true);
        int temp;
        while ((temp = fileInputStream.read()) != -1)
            fileOutputStream.write(temp);
        fileInputStream.close();
        fileOutputStream.close();
    }

    /**
     * 返回当前时间 <年-月-日>
     *
     * @return
     */
    public static String getDate() {
        return data.format(new Date());
    }

    /**
     * 自动检查ID是否包含特殊值，若不包含则默认特殊值为0后返回数组
     *
     * @param ID 要检查分解的ID
     * @return int[]{ID, Damage}
     */
    public static int[] IDtoFullID(Object ID) {
        return IDtoFullID(ID, 0);
    }

    /**
     * 自动检查ID是否包含特殊值，若不包含则设置特殊值为用户定义值后返回数组
     *
     * @param Damage 要默认设置的特殊值
     * @return int[]{ID, Damage}
     */
    public static int[] IDtoFullID(Object obj, int Damage) {
        String ID = "0";
        if (obj != null && !String.valueOf(obj).isEmpty())
            ID = String.valueOf(obj);
        if (!ID.contains(":"))
            ID += ":" + Damage;
        String[] strings = ID.split(":");
        try {
            return new int[]{Integer.parseInt(strings[0]), Integer.parseInt(strings[1])};
        } catch (Exception e) {
            return new int[]{0, 0};
        }
    }

    /**
     * 获取随机数
     *
     * @return
     */
    public static int getRand(int Min, int Max) {
        int Rand, Fn = Min;
        Min = Math.max(Min, 0);
        Rand = (int) (Min + Math.random() * (Max - Min + 1));
        if (Fn < 0) {
            int x = (int) (Math.random() * (Fn * -1 + 1));
            Rand = (int) (Math.random() * 2) == 1 ? Rand : x * -1;
        }
        return Rand;
    }

    /**
     * 获取随机数
     *
     * @return
     */
    public static int getRand() {
        return getRand(Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    /**
     * 返回一个随机颜色代码
     *
     * @return
     */
    public static String getRandColor() {
        return getRandColor(colorKeyString);
    }

    /**
     * 返回一个随机颜色代码
     *
     * @param ColorFont 可以随机到的颜色代码
     * @return
     */
    public static String getRandColor(String ColorFont) {
        int rand = Tool.getRand(0, ColorFont.length() - 1);
        return "§" + ColorFont.charAt(rand);
    }

    /**
     * 将字符串染上随机颜色
     *
     * @param Font 要染色的字符串
     * @return
     */
    public static String getColorFont(String Font) {
        return getColorFont(Font, colorKeyString);
    }

    /**
     * 返回一个随机字符
     *
     * @return 随机字符
     */
    public static String getRandString() {
        return getRandString(randString);
    }

    /**
     * 返回一个随机字符
     *
     * @param string 要随机字符的范围
     * @return 随机字符
     */
    public static String getRandString(String string) {
        int r1 = getRand(0, string.length() - 1);
        return string.substring(r1, r1 + 1);
    }

    /**
     * 将字符串染上随机颜色
     *
     * @param Font      要染色的字符串
     * @param ColorFont 随机染色的颜色代码
     * @return
     */
    public static String getColorFont(String Font, String ColorFont) {
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < Font.length(); i++) {
            int rand = Tool.getRand(0, ColorFont.length() - 1);
            text.append("§").append(ColorFont.charAt(rand)).append(Font.charAt(i));
        }
        return text.toString();
    }

    /**
     * 判断字符串是否是整数型
     *
     * @param str
     * @return
     */
    public static boolean isInteger(Object str) {
        try {
            Float.valueOf(String.valueOf(str)).intValue();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断一段字符串中是否只为纯数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9.]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 字符串转换Unicode
     *
     * @param string 要转换的字符串
     * @return
     */
    public static String StringToUnicode(String string) {
        StringBuilder unicode = new StringBuilder();
        for (int i = 0; i < string.length(); i++)
            unicode.append("\\u").append(Integer.toHexString(string.charAt(i)));
        return unicode.toString();
    }

    /**
     * unicode 转字符串
     *
     * @param unicode 全为 Unicode 的字符串
     * @return
     */
    public static String UnicodeToString(String unicode) {
        StringBuilder string = new StringBuilder();
        String[] hex = unicode.split("\\\\u");
        for (int i = 1; i < hex.length; i++)
            string.append((char) Integer.parseInt(hex[i], 16));
        return string.toString();
    }

    /**
     * 设置小数长度</br>
     * 默认保留两位小数</br>
     *
     * @param d 要设置的数值
     * @return
     */
    public static double Double2(double d) {
        return Double2(d, 2);
    }

    /**
     * 设置小数长度</br>
     *
     * @param d      要设置的数
     * @param length 要保留的小数的
     * @return
     */
    public static double Double2(double d, int length) {
        if (d == 0)
            return 0;
        StringBuilder s = new StringBuilder("#.0");
        for (int i = 1; i < length; i++)
            s.append("0");
        DecimalFormat df = new DecimalFormat(s.toString());
        return Double.parseDouble(df.format(d));
    }

    /**
     * 根据提供的文件列表删除
     *
     * @param files 需要删除的文件列表
     * @return 删除结果
     */
    public static boolean delete(File... files) {
        boolean isSu = true;
        for (File file : files) {
            if (file == null || !file.exists()) continue;
            isSu = isSu && delete(files);
        }
        return isSu;
    }

    /**
     * 删除文件或者文件夹
     *
     * @param file 文件或文件夹对象
     * @return 删除结果
     */
    public static boolean delete(File file) {
        if (file == null || !file.exists()) return false;
        if (file.isFile()) return file.delete();
        else {
            File[] files = file.listFiles();
            if (files == null) return false;
            boolean delete = true;
            for (File item : files) {
                if (item == null || !item.exists()) continue;
                if (item.isFile()) delete = delete && item.delete();
                else delete = delete && delete(item);
            }
            return delete && file.delete();
        }
    }

    /**
     * 发送HTTP请求
     *
     * @param httpUrl 请求地址
     * @return
     * @throws Exception
     */
    public static String getHttp(String httpUrl) throws Exception {
        return getHttp(httpUrl, "POST", null);
    }

    /**
     * 发送HTTP请求
     *
     * @param httpUrl 请求地址
     * @param param   请求的内容
     * @return
     * @throws Exception
     */
    public static String getHttp(String httpUrl, String param) throws Exception {
        return getHttp(httpUrl, "POST", param);
    }

    /**
     * 发送HTTP请求
     *
     * @param httpUrl 请求地址
     * @param Type    请求的方式
     * @param param   请求的内容
     * @return
     * @throws Exception
     */
    public static String getHttp(String httpUrl, String Type, String param) throws Exception {
        HttpURLConnection connection;
        InputStream is = null;
        BufferedReader br = null;
        String result = null;
        URL url = new URL(httpUrl);
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(Type);
        connection.setConnectTimeout(15000);
        connection.setReadTimeout(60000);
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestProperty("Authorization", "Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0");
        connection.setRequestProperty("Connection", " keep-alive");
        connection.setRequestProperty("Content-Type", " application/x-www-form-urlencoded; charset=UTF-8");
        connection.setRequestProperty("User-Agent", " Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.90 Safari/537.36");
        if (param != null && !param.isEmpty()) {
            OutputStream os;
            os = connection.getOutputStream();
            os.write(param.getBytes());
            os.close();
        }
        if (connection.getResponseCode() == 200) {
            is = connection.getInputStream();
            br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            StringBuilder sbf = new StringBuilder();
            String temp;
            while ((temp = br.readLine()) != null) {
                sbf.append(temp);
                sbf.append("\r\n");
            }
            result = sbf.toString();
        }
        if (null != br)
            br.close();
        if (null != is)
            is.close();
        connection.disconnect();
        return result;
    }

    /**
     * 从一段字符内截取另一段字符
     *
     * @param Context 要截取字符的原文
     * @return 截取完毕的内容
     */
    public static String cutString(String Context, String strStart, String strEnd) {
        int strStartIndex = Context.indexOf(strStart);
        int strEndIndex = Context.indexOf(strEnd, strStartIndex + 1);
        if (strStartIndex < 0 || strEndIndex < 0)
            return null;
        return Context.substring(strStartIndex, strEndIndex).substring(strStart.length());
    }

    /**
     * 下载文件
     *
     * @param urlStr   要下载的文件的连接
     * @param fileName 下载后文件的名字
     * @param savePath 要保存的位置
     * @throws IOException
     */
    public static void DownFile(String urlStr, String fileName, String savePath) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(3 * 1000);
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        InputStream inputStream = conn.getInputStream();
        byte[] getData = readInputStream(inputStream);
        File saveDir = new File(savePath);
        if (!saveDir.exists())
            saveDir.mkdir();
        File file = new File(saveDir + File.separator + fileName);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(getData);
        fos.close();
        inputStream.close();
    }

    /**
     * 保存字节流
     *
     * @param inputStream 文件流
     * @return
     * @throws IOException
     */
    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1)
            bos.write(buffer, 0, len);
        bos.close();
        return bos.toByteArray();
    }

    /**
     * 将一段不知道什么玩意转化为纯整数
     *
     * @param object
     * @return
     */
    public static int ObjToInt(Object object) {
        return ObjToInt(object, 0);
    }

    /**
     * 讲一段不知道什么玩意转化为纯数字
     *
     * @param object
     * @param i      若不是纯数字将默认转化的值
     * @return
     */
    public static int ObjToInt(Object object, int i) {
        if (object == null)
            return i;
        String string = String.valueOf(object);
        if (string.isEmpty() || !isInteger(object))
            return i;
        return string.contains(".") ? Float.valueOf(object.toString()).intValue() : Integer.parseInt(string);
    }

    /**
     * 一个Object值转换为bool值，转化失败返回false
     *
     * @param obj
     * @return
     */
    public static boolean ObjToBool(Object obj) {
        return ObjToBool(obj, false);
    }

    /**
     * 一个Object值转换为bool值，转化失败返回false
     *
     * @param obj
     * @param Del
     * @return
     */
    public static boolean ObjToBool(Object obj, boolean Del) {
        if (obj == null)
            return Del;
        try {
            return Boolean.parseBoolean(String.valueOf(obj));
        } catch (Exception e) {
            return Del;
        }
    }

    /**
     * 发送Https请求
     *
     * @param requestUrl 请求的地址
     * @return
     * @throws KeyManagementException
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static String getHttps(String requestUrl) throws KeyManagementException, UnsupportedEncodingException, NoSuchAlgorithmException, IOException {
        return getHttps(requestUrl, "POST", null);
    }

    /**
     * 发送Https请求
     *
     * @param requestUrl 请求的地址
     * @param outputStr  请求的参数值
     * @return
     * @throws KeyManagementException
     * @throws UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static String getHttps(String requestUrl, String outputStr) throws KeyManagementException, UnsupportedEncodingException, NoSuchAlgorithmException, IOException {
        return getHttps(requestUrl, "POST", outputStr);
    }

    /**
     * 发送Https请求
     *
     * @param requestUrl    请求的地址
     * @param requestMethod 请求的方式
     * @param outputStr     请求的参数值
     * @return
     * @throws IOException
     * @throws KeyManagementException
     * @throws NoSuchAlgorithmException
     */
    public static String getHttps(String requestUrl, String requestMethod, String outputStr) throws IOException, KeyManagementException, NoSuchAlgorithmException {
        StringBuilder buffer;
        SSLContext sslContext = SSLContext.getInstance("SSL");
        TrustManager[] tm = {new Tool()};
        sslContext.init(null, tm, new java.security.SecureRandom());
        SSLSocketFactory ssf = sslContext.getSocketFactory();
        URL url = new URL(requestUrl);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        conn.setRequestMethod(requestMethod);
        conn.setSSLSocketFactory(ssf);
        conn.connect();
        if (null != outputStr && !outputStr.isEmpty()) {
            OutputStream os = conn.getOutputStream();
            os.write(outputStr.getBytes(StandardCharsets.UTF_8));
            os.close();
        }
        InputStream is = conn.getInputStream();
        InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(isr);
        buffer = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null)
            buffer.append(line);
        return buffer.toString();
    }

    /**
     * 替换掉字符中的html标签
     *
     * @return
     */
    public static String delHtmlString(String htmlStr) {
        if (htmlStr == null || htmlStr.isEmpty())
            return htmlStr;
        htmlStr = htmlStr.replace("<p>", "\r\n\t").replace("<span>", "\r\n\t").replace("<br>", "\r\n").replace("</br>", "\r\n");
        Pattern p_script = Pattern.compile("<script[^>]*?>[\\s\\S]*?<\\/script>", Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll("");
        Pattern p_style = Pattern.compile("<style[^>]*?>[\\s\\S]*?<\\/style>", Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll("");
        Pattern p_html = Pattern.compile("<[^>]+>", Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll("");
        return htmlStr.replaceAll("&nbsp;", "").trim();
    }

    /**
     * Https下载文件
     *
     * @param urlStr   要下载的文件链接
     * @param fileName 要保存的文件的名字
     * @param savePath 文件的保存位置
     * @throws Exception
     */
    public static void downLoadFromUrlHttps(String urlStr, String fileName, String savePath) throws Exception {
        SSLContext sslContext = SSLContext.getInstance("SSL");
        TrustManager[] tm = {new Tool()};
        sslContext.init(null, tm, new java.security.SecureRandom());
        SSLSocketFactory ssf = sslContext.getSocketFactory();
        URL url = new URL(urlStr);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setHostnameVerifier(new Tool());
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        conn.setSSLSocketFactory(ssf);
        conn.connect();
        InputStream inputStream = conn.getInputStream();
        byte[] getData = readInputStream(inputStream);
        File saveDir = new File(savePath);
        if (!saveDir.exists())
            saveDir.mkdirs();
        FileOutputStream fos = new FileOutputStream(new File(saveDir, fileName));
        fos.write(getData);
        fos.close();
        inputStream.close();
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) {
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }

    @Override
    public boolean verify(String arg0, SSLSession arg1) {
        return true;
    }

    /**
     * 将未知参数转换为小数
     *
     * @param obj
     * @return
     */
    public static Double ObjToDouble(Object obj) {
        return ObjToDouble(obj, 0d);
    }

    /**
     * 将未知参数转换为小数
     *
     * @param obj
     * @param double1
     * @return
     */
    public static Double ObjToDouble(Object obj, Double double1) {
        if (obj == null)
            return double1;
        double d;
        try {
            String S = String.valueOf(obj);
            if (S == null || S.isEmpty())
                return double1;
            d = Double.parseDouble(S);
        } catch (Exception e) {
            return double1;
        }
        return d;
    }

    /**
     * 将Map按数据升序排列
     *
     * @param <K>
     * @param <V>
     * @param map
     * @return
     */
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValueAscending(Map<K, V> map) {
        List<Entry<K, V>> list = new LinkedList<>(map.entrySet());
        list.sort(Entry.comparingByValue());
        Map<K, V> result = new LinkedHashMap<>();
        for (Entry<K, V> entry : list)
            result.put(entry.getKey(), entry.getValue());
        return result;
    }

    /**
     * 将Map降序排序
     *
     * @param <K>
     * @param <V>
     * @param map
     * @return
     */
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValueDescending(Map<K, V> map) {
        List<Entry<K, V>> list = new LinkedList<>(map.entrySet());
        list.sort((a, b) -> -(a.getValue().compareTo(b.getValue())));
        Map<K, V> result = new LinkedHashMap<>();
        for (Entry<K, V> entry : list)
            result.put(entry.getKey(), entry.getValue());
        return result;
    }

    /**
     * 返回一个随机数
     *
     * @param d
     * @param e
     * @return
     */
    public static double getRand(double d, double e) {
        return getRand(ObjToInt(d), ObjToInt(e));
    }
}
