package cn.winfxk.lexy.amp.crucial;

import cn.winfxk.lexy.amp.Log;
import cn.winfxk.lexy.amp.Main;
import cn.winfxk.lexy.amp.tool.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用来封装某个型号关键零部件信息的封装类
 */
public class Crucial {
    /**
     * 这个型号的进口件类型的关键零部件清单
     */
    private final List<CrucialItem> inputItem = new ArrayList<>();
    /**
     * 这个型号内所有关键零部件的信息
     */
    private final List<CrucialItem> allItems = new ArrayList<>();
    /**
     * 这个型号的非进口件关键零部件清单
     */
    private final List<CrucialItem> crucialItems = new ArrayList<>();
    /**
     * 这个型号的关键零部件原始信息
     */
    private final Map<String, Object> map;
    /**
     * 这个型号的名称
     */
    private final String Model;

    @Override
    public Crucial clone() {
        try {
            return (Crucial) super.clone();
        } catch (Exception e) {
            Log.w("在克隆关键零部件清单时出现异常！", e);
            return new Crucial(this);
        }
    }

    /**
     * 将关键零部件清单的数据保存到本地
     */
    public void save() {
        map.clear();
        Map<String, List<String>> inputMap = new HashMap<>();
        for (CrucialItem item : allItems) {
            if (item.isInput()) inputMap.put(item.getName(), item.getCode());
            else map.put(item.getName(), item.getCode());
        }
        map.put("进口件", inputMap);
        Config config = new Config(Main.KeylistFile);
        config.set(getModel(), map).save();
    }

    /**
     * 添加一个关键零部件物料
     *
     * @param item 需要添加的物料
     */
    public void addItem(CrucialItem item) {
        allItems.add(item);
        (item.isInput() ? inputItem : crucialItems).add(item);
    }

    /**
     * 删除某些关键零部件
     *
     * @param string
     */
    public void removeItem(String string) {
        if (string == null || string.isEmpty()) return;
        for (CrucialItem item : new ArrayList<>(allItems))
            if (item.getModel().equals(string)) {
                allItems.remove(item);
                if (item.isInput()) inputItem.remove(item);
                else crucialItems.remove(item);
            }
    }

    /**
     * 克隆一个新的关键零部件清单
     *
     * @param crucial 需要克隆的清单对象
     */
    private Crucial(Crucial crucial) {
        this.inputItem.addAll(crucial.getInputItem());
        this.allItems.addAll(crucial.getAllItems());
        this.crucialItems.addAll(crucial.getCrucialItems());
        this.map = new HashMap<>(crucial.getMap());
        this.Model = crucial.getModel();
    }

    /**
     * 某个型号的关键零部件清单信息
     *
     * @param map   这个型号的关键零部件原始数据
     * @param model 这个关键零部件清单所属的型号
     */
    public Crucial(Map<String, Object> map, String model) {
        this.Model = model;
        this.map = map;
        Object obj = map.get("进口件");
        Map<String, Object> input = obj instanceof Map ? (Map<String, Object>) obj : new HashMap<>();
        map.remove("进口件");
        CrucialItem crucialItem;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getKey() == null || entry.getValue() == null) continue;
            crucialItem = new CrucialItem(entry.getKey(), this, false, entry.getValue());
            allItems.add(crucialItem);
            crucialItems.add(crucialItem);
        }
        for (Map.Entry<String, Object> entry : input.entrySet()) {
            if (entry.getKey() == null || entry.getValue() == null) continue;
            crucialItem = new CrucialItem(entry.getKey(), this, true, entry.getValue());
            allItems.add(crucialItem);
            inputItem.add(crucialItem);
        }
    }

    public String getModel() {
        return Model;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public List<CrucialItem> getCrucialItems() {
        return crucialItems;
    }

    public List<CrucialItem> getAllItems() {
        return allItems;
    }

    public List<CrucialItem> getInputItem() {
        return inputItem;
    }
}
