package cn.winfxk.lexy.amp.crucial;

import cn.winfxk.lexy.amp.Log;
import cn.winfxk.lexy.amp.Main;
import cn.winfxk.lexy.amp.excel.factory.Task;
import cn.winfxk.lexy.amp.mes.MesItem;
import cn.winfxk.lexy.amp.tool.Config;
import cn.winfxk.lexy.amp.tool.view.StartView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 某型号关键零部件清单里物料的检验情况
 */
public class Inspectionl implements StartView {
    /**
     * 用于存储可能是这个型号的MES记录
     */
    private final List<MesItem> TaskMesItems = new ArrayList<>();
    private final List<CrucialItem> okItem = new ArrayList<>();
    private final List<CrucialItem> ngItem = new ArrayList<>();
    private final String[] ModelKey = {"VC-"};
    private final List<MesItem> mesItems;
    private final Crucial crucial;
    private final Task task;

    /**
     * 封装一个某型号零部件检验情况的类
     *
     * @param task     需要判断的滚动计划
     * @param mesItems MES的搜索范围
     */
    public Inspectionl(Task task, List<MesItem> mesItems) {
        this.mesItems = mesItems;
        this.task = task;
        this.crucial = CrucialThread.getCrucial(task.Model);
    }

    @Override
    public void start() {
        okItem.clear();
        ngItem.clear();
        if (crucial != null) {
            ngItem.addAll(crucial.getAllItems());
            Config config = new Config(Main.IgnoreFile);
            Log.i("正在进行MES覆盖率检查：" + task.Model + "/" + task.S1500);
            String modelKey = task.S1500 + " / " + task.Model;
            for (CrucialItem crucialItem : new ArrayList<>(crucial.getAllItems())) {
                if (crucialItem == null) continue;
                String itemKey = modelKey + " " + crucialItem.getName();
                if (config.getBoolean(itemKey)) {
                    ngItem.remove(crucialItem);
                    if (!okItem.contains(crucialItem)) okItem.add(crucialItem);
                    continue;
                }
                for (MesItem mesItem : new ArrayList<>(mesItems)) {
                    if (mesItem == null) continue;
                    if (mesItem.getS1500() != null && mesItem.getModel() != null)
                        if (mesItemIsTask(mesItem)) TaskMesItems.add(mesItem);
                    if (task.S1500 == null || task.S1500.isEmpty() || task.S1500.equals("试产") || task.S1500.equalsIgnoreCase("null")) {
                        if (mesItem.getS1500() == null || mesItem.getS1500().isEmpty() || mesItem.getS1500().equals("试产")) {
                            if (crucialItem.isCode(mesItem.getCode(), mesItem.getImageCode())) {
                                if (!okItem.contains(crucialItem)) okItem.add(crucialItem);
                                ngItem.remove(crucialItem);
                            }
                        }
                    } else {
                        if ((mesItem.getS1500() == null || mesItem.getS1500().isEmpty()) || mesItem.getS1500().equalsIgnoreCase("null")) {
                            if (crucialItem.isInput()) {
                                if (crucialItem.isCode(mesItem.getCode(), mesItem.getImageCode())) {
                                    if (!okItem.contains(crucialItem)) okItem.add(crucialItem);
                                    ngItem.remove(crucialItem);
                                }
                            }
                        } else if ((mesItem.getS1500() != null && !mesItem.getS1500().isEmpty() && !mesItem.getS1500().equalsIgnoreCase("null"))) {
                            if (isS1500(mesItem.getS1500()) && crucialItem.isCode(mesItem.getCode(), mesItem.getImageCode())) {
                                if (!okItem.contains(crucialItem)) okItem.add(crucialItem);
                                ngItem.remove(crucialItem);
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean isS1500(String S1500) {
        if (S1500 == null) return false;
        if (task.S1500 == null) return false;
        if (S1500.equalsIgnoreCase(task.S1500)) return true;
        if (!task.S1500.contains("-")) return false;
        return task.S1500.substring(0, task.S1500.lastIndexOf("-")).equalsIgnoreCase(S1500);
    }

    /**
     * @return 判断是否可能是这个订单的物料
     */
    private boolean mesItemIsTask(MesItem item) {
        if (task.S1500 == null || task.S1500.isEmpty() || task.S1500.equalsIgnoreCase("null") ||
                item.getS1500() == null || item.getS1500().isEmpty() || item.getS1500().equalsIgnoreCase("null"))
            return false;
        if (!isS1500(item.getS1500())) return false;
        if (item.getModel() == null || item.getModel().isEmpty()) return false;
        if (task.Model == null || task.Model.isEmpty()) return false;
        if (item.getModel().equalsIgnoreCase(task.Model)) return true;
        String lowMesModel = item.getModel().toLowerCase(Locale.ROOT);
        String lowTaskModel = task.Model.toLowerCase(Locale.ROOT);
        if (lowMesModel.startsWith(lowTaskModel)) return true;
        String newTaskmodel;
        for (String startWith : ModelKey) {
            if (!lowMesModel.startsWith(startWith.toLowerCase(Locale.ROOT))) continue;
            newTaskmodel = startWith.toLowerCase(Locale.ROOT) + task.Model;
            if (newTaskmodel.equalsIgnoreCase(lowMesModel) || lowMesModel.startsWith(newTaskmodel)) return true;
        }
        return false;
    }

    /**
     * @return 返回可能是这个型号的物料的MES记录
     */
    public List<MesItem> getTaskMesItems() {
        return TaskMesItems;
    }

    /**
     * @return 对应的滚动计划
     */
    public Task getTask() {
        return task;
    }

    /**
     * @return 滚动计划表
     */
    public Crucial getCrucial() {
        return crucial;
    }

    /**
     * @return 为正常录入MES的零部件清单
     */
    public List<CrucialItem> getNgItem() {
        return ngItem;
    }

    /**
     * @return 已录入MES的零部件清单
     */
    public List<CrucialItem> getOkItem() {
        return okItem;
    }
}
