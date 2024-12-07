package cn.winfxk.lexy.amp.email;

import cn.winfxk.lexy.amp.All;
import cn.winfxk.lexy.amp.AutoMesException;
import cn.winfxk.lexy.amp.Log;
import cn.winfxk.lexy.amp.Main;
import cn.winfxk.lexy.amp.crucial.CrucialItem;
import cn.winfxk.lexy.amp.crucial.Inspectionl;
import cn.winfxk.lexy.amp.excel.ExcelFactory;
import cn.winfxk.lexy.amp.excel.factory.Task;
import cn.winfxk.lexy.amp.mes.MesItem;
import cn.winfxk.lexy.amp.mes.Serialization;
import cn.winfxk.lexy.amp.tool.Config;
import cn.winfxk.lexy.amp.tool.Tool;
import cn.winfxk.lexy.amp.tool.Utils;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

public class EmailThread implements Runnable {
    /**
     * 用来存储已发送邮件的时间，防止重复发送
     */
    private static final File configFile = new File(All.DataFile, "EmailDate.yml");
    /**
     * 存储于防止重复发送的Key用以下结构编码
     */
    private static final SimpleDateFormat data = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    /**
     * 邮件发送时间的结构
     */
    public static final SimpleDateFormat format = new SimpleDateFormat("HH:mm");
    /**
     * 邮件正文的Html尾部
     */
    private static final String HtmlBottom = "</table>\n</body>\n</html>";
    /**
     * 存储邮件收件人的清单
     */
    private static final List<String> recEmailList = new ArrayList<>();
    /**
     * 用来存储已发送邮件的时间，防止重复发送
     */
    public static final Config config = new Config(configFile);
    /**
     * 发送邮件使用的邮箱
     */
    public static final String fromEmail = "lexy_mes@Winfxk.cn";
    /**
     * 发送邮件使用的密码
     */
    public static final String authCode = "Xiaoli809714630";
    /**
     * 发送邮件使用的SMTP服务器地址0
     */
    public static final String emailHost = "smtp.winfxk.cn";
    /**
     * 发送邮件时使用的标题
     */
    public static final String fromUser = "关键零部件预警系统";
    public static final Properties props = new Properties();
    public static final String transportType = "smtp";
    private static boolean isInitok = true;
    /**
     * 邮件正文的开头
     */
    private static final String HtmlStart;
    public static final Session session;
    public static List<String> sendDate;

    static {
        String string = null;
        try {
            string = Utils.readFile(Main.getStream("index.html"));
        } catch (Exception e) {
            Log.e("无法正常读取邮件正文！可能无法正常发送邮件！");
            isInitok = false;
        }
        HtmlStart = string;
        props.setProperty("mail.transport.protocol", transportType);
        props.setProperty("mail.host", emailHost);
        props.setProperty("mail.user", fromUser);
        props.setProperty("mail.from", fromEmail);
        session = Session.getInstance(props, null);
        session.setDebug(true);
        if (isInitok) Log.i("预警邮件服务初始化完成！");
    }

    /**
     * 对邮件正文进行处理，准备发送邮件
     */
    public void sendEmail(Date date) {
        Log.i("准备发送预警邮件。");
        List<Task> list = ExcelFactory.getTaskByFile(ExcelFactory.getTaskFile(System.currentTimeMillis()));
        if (list == null || list.isEmpty()) {
            Log.i("暂无当日计划，取消预警邮件的发送任务。");
            return;
        }
        //提取最近一段时间的MES数据
        int timeLength = Math.min(3, Math.max(Main.getConfig().getInt("MES抓取距离", 1), 1));
        long endTime = date.getTime();
        long startTime = endTime - (86400000L * 31 * timeLength);
        Map<Long, List<MesItem>> map = Serialization.getMesItemByTime(startTime, endTime);
        List<MesItem> mesItems = new ArrayList<>();
        for (List<MesItem> list1 : map.values()) {
            if (list1 == null || list1.isEmpty()) continue;
            mesItems.addAll(list1);
        }
        //进行覆盖率判断
        List<Inspectionl> inspectionls = new ArrayList<>();
        for (Task task : list) inspectionls.add(new Inspectionl(task, mesItems));
        StringBuilder Content = new StringBuilder();
        String Head;
        StringBuilder Body;
        int TaskCount = 0, maxItemCount = 0;
        for (Inspectionl inspectionl : inspectionls) {
            if (inspectionl.getCrucial() == null) continue;
            inspectionl.start();
            if (inspectionl.getNgItem().isEmpty()) continue;
            maxItemCount = Math.max(maxItemCount, inspectionl.getNgItem().size());
            Head = "<tr>\n        <td>" + inspectionl.getTask().Model + "</td>\n        <td>" + inspectionl.getTask().S1500 + "</td>\n        ";
            Body = new StringBuilder();
            for (CrucialItem item : inspectionl.getNgItem())
                Body.append("        <td>").append(item.getName()).append("</td>\n");
            TaskCount++;
            Content.append(Head).append(Body).append("\n    </tr>\n");
        }
        try {
            List<MesItem> mesCountlist = Serialization.getMesItemByTime(date);
            int mesCount = mesCountlist == null ? 0 : mesCountlist.size();
            if (TaskCount > 0)
                ClientTestA(HtmlStart.replace("{Colspan}", maxItemCount + "").
                        replace("{Title}", "当日已录入记录数：" + mesCount) + Content + HtmlBottom);
            else Log.i("今日所有关键零部件可能都已录入完毕！取消预警邮件发送！");
        } catch (Exception e) {
            Log.e("预警邮件发送时出现错误！", e);
        }
    }

    /**
     * 发送邮件的接口
     *
     * @param Content 邮件正文，需HTML代码
     * @throws Exception 可能的错误
     */
    public void ClientTestA(String Content) throws Exception {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(MimeUtility.encodeWord(fromUser) + " <" + fromEmail + ">"));
        InternetAddress[] address = getEmailPlayer();
        if (address.length < 1) throw new AutoMesException("无法发送预警邮件！因为收件人列表为空！");
        String title = Main.getConfig().getString("预警邮件标题", "{Date} 关键零部件检验明细");
        if (title == null || title.isEmpty()) title = "{Date} 关键零部件检验明细";
        message.setRecipients(Message.RecipientType.TO, address);
        message.setSubject(title.replace("{Date}", Tool.getDate()));
        message.setContent(Content, "text/html;charset=UTF-8");
        message.saveChanges();
        Transport transport = session.getTransport();
        transport.connect(null, fromEmail, authCode);
        transport.sendMessage(message, message.getAllRecipients());
        config.set(data.format(new Date()), "true").save();
        Log.i("预警邮件发送完成！");
    }

    /**
     * @return 返回会受到预警邮件的人
     */
    public InternetAddress[] getEmailPlayer() {
        List<InternetAddress> list = new ArrayList<>();
        for (String player : recEmailList)
            if (player != null && !player.isEmpty()) try {
                list.add(new InternetAddress(player));
            } catch (AddressException e) {
                Log.e("添加收件人" + player + "时出现错误！", e);
            }
        InternetAddress[] addresses = new InternetAddress[list.size()];
        for (int i = 0; i < addresses.length; i++) addresses[i] = list.get(i);
        return addresses;
    }

    /**
     * 更新邮箱数据，比如邮箱发送的时间，比如需要发送的人,需要发送的时间等
     */
    public static void reload() {
        List<Object> objects = Main.getConfig().getList("预警邮件接收清单", new ArrayList<>());
        recEmailList.clear();
        sendDate = Main.getConfig().getList("邮件发送时间", Collections.singletonList("16:00"));
        if (sendDate == null || sendDate.isEmpty()) {
            Log.w("未设定邮件发送时间，取消邮件发送");
            return;
        }
        if (objects == null) return;
        String s;
        for (Object obj : objects) {
            s = Tool.objToString(obj, null);
            if (s == null || s.isEmpty()) continue;
            recEmailList.add(s);
        }
    }

    /**
     * 判断是否到达预警邮件发送时间
     *
     * @param date 当前时间
     * @return 是否能发送
     */
    public boolean isDate(Date date) {
        List<Object> list = Main.getConfig().getList("邮件发送时间时", Collections.singletonList("16:00"));
        if (list == null || list.isEmpty()) return false;
        return list.contains(EmailThread.format.format(date));
    }

    /**
     * 启用定时线程
     */
    @Override
    public void run() {
        Date date;
        Log.i("预警邮件服务启动！");
        while (Main.runing) {
            Tool.sleep(1000);
            date = new Date();
            if (!isDate(date)) continue;
            if (!isInitok) {
                Log.w("预警邮件服务初始化失败！无法进行预警邮件发送操作！");
                break;
            }
            if (config.containsKey(EmailThread.data.format(date))) continue;
            reload();
            sendEmail(date);
        }
    }
}
