package cn.winfxk.lexy.amp.crucial;

import cn.winfxk.lexy.amp.tool.Tool;

import java.util.ArrayList;
import java.util.List;

/**
 * 一个用于存储关键零部件清单内单个零部件数据的封装类
 */
public class CrucialItem {
    /**
     * 这个零部件的名称
     */
    private String name;
    /**
     * 这个关键零部件所属的型号
     */
    private final String Model;
    /**
     * 这个关键零部件所属的型号
     */
    private final Crucial crucial;
    /**
     * 是否是进口件
     */
    private boolean isInput;
    /**
     * 这个零部件的料号或图号
     */
    private final List<String> Code = new ArrayList<>();

    /**
     * 创建一个关键零部件的信息
     *
     * @param model   这个零部件所属的型号
     * @param name    零部件的名称
     * @param crucial 零部件所属的关键零部件清单
     * @param isInput 是否是进口件
     * @param obj     料号、图号等，可以是list也可以是文本
     */
    public CrucialItem(String name, Crucial crucial, boolean isInput, Object obj) {
        this.Model = crucial.getModel();
        this.name = name;
        this.crucial = crucial;
        this.isInput = isInput;
        if (obj instanceof List) {
            String code;
            for (Object object : (List<Object>) obj) {
                if (object == null) continue;
                code = Tool.objToString(object, null);
                if (code == null || code.isEmpty()) continue;
                this.Code.add(code);
            }
        } else this.Code.add(Tool.objToString(obj, null));
    }

    /**
     * 设置物料状态
     *
     * @param input
     */
    public void setInput(boolean input) {
        isInput = input;
    }

    /**
     * 重新设置关键零部件物料的名称
     *
     * @param name 新名称
     */
    public void setName(String name) {
        this.name = name;
    }

    public boolean isInput() {
        return isInput;
    }

    public Crucial getCrucial() {
        return crucial;
    }

    /**
     * 更改关键零部件的料号、图号等
     *
     * @param list 包含料号图号的代码
     */
    public void setCode(List<String> list) {
        Code.clear();
        Code.addAll(list);
    }

    /**
     * @param obj 图号或料号
     * @return 判断料号或图号是否OK
     */
    public boolean isCode(String code, String image) {
        if ((code == null || code.isEmpty()) && (image == null || image.isEmpty())) return false;
        if (Code.contains(code) || Code.contains(image)) return true;
        for (String c : Code) {
            if (c == null || c.isEmpty()) continue;
            if (c.equalsIgnoreCase(code) || c.equalsIgnoreCase(image)) return true;
        }
        return false;
    }

    public List<String> getCode() {
        return Code;
    }

    public String getModel() {
        return Model;
    }

    public String getName() {
        return name;
    }
}
