package cn.winfxk.lexy.amp.mes;

import cn.winfxk.lexy.amp.tool.Tool;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MesItem {
    /**
     * MES数据的原始文本
     */
    private final List<String> Content;
    /**
     * 零部件的的物料名称
     */
    private final String Name;
    /**
     * 零部件的检验判定是否合格
     */
    private final boolean Standard;
    /**
     * 零部件的订单号
     */
    private final String S1500;
    /**
     * 零部件的型号
     */
    private final String Model;
    /**
     * 零部件的检验判定的时间
     */
    private final String endTime;
    /**
     * 零部件的料号
     */
    private final String Code;
    /**
     * 零部件的检验用时
     */
    private final long useTime;
    /**
     * 零部件的图号
     */
    private final String ImageCode;
    /**
     * 零部件的供应商
     */
    private final String Suppliers;
    /**
     * 零部件客户
     */
    private final String Custom;
    /**
     * 零部件数量
     */
    private final int Count;

    /**
     * 创建一个MES数据
     *
     * @param content   MES的文本数据
     * @param name
     * @param standard
     * @param s1500
     * @param model
     * @param endTime
     * @param code
     * @param useTime
     * @param imageCode
     * @param suppliers
     * @param custom
     */
    public MesItem(String string) {
        this.Content = Arrays.asList(string.split(","));
        String a = getString(4);
        this.S1500 = a == null || a.isEmpty() || a.equalsIgnoreCase("null") ? null : a;
        this.Custom = getString(13);
        this.Model = getString(15);
        this.ImageCode = getString(17);
        this.Name = getString(18);
        this.Code = getString(19);
        a = getString(51);
        this.Count = Tool.ObjToInt(a == null || a.isEmpty() ? 0 : a.replace(" ", ""), 0);
        this.endTime = getString(78);
        String str = getString(61);
        this.Standard = str != null && !str.isEmpty() && str.toLowerCase(Locale.ROOT).equals("pass");
        this.useTime = Tool.objToLong(getString(79), 0);
        this.Suppliers = getString(26);
    }

    /**
     * 根据提供的索引在总数据中找到对应的数据返回
     *
     * @param index 缩印
     * @return 需要的数据
     */
    public String getString(int index) {
        return Content.size() <= index ? null : Content.get(index);
    }

    public boolean isStandard() {
        return Standard;
    }

    public String getS1500() {
        return S1500;
    }

    public String getEndTime() {
        return endTime;
    }

    public long getUseTime() {
        return useTime;
    }

    public String getImageCode() {
        return ImageCode;
    }

    public String getCustom() {
        return Custom;
    }

    public String getSuppliers() {
        return Suppliers;
    }

    public String getName() {
        return Name;
    }

    public String getModel() {
        return Model;
    }

    public List<String> getContent() {
        return Content;
    }

    public String getCode() {
        return Code;
    }
}
