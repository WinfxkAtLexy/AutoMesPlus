package cn.winfxk.lexy.amp.crucial;

import cn.winfxk.lexy.amp.Log;
import cn.winfxk.lexy.amp.Main;
import cn.winfxk.lexy.amp.tool.Config;
import cn.winfxk.lexy.amp.tool.Tool;
import cn.winfxk.lexy.amp.view.AutoMes;

import java.util.*;

public class CrucialThread extends Thread {
    /**
     * 常见的一些型号后缀，如C3507D中的D
     */
    public static final List<String> exps = Arrays.asList("a", "b", "c", "d", "e", "s", "da", "ed", "mad", "ma");
    private static final Map<String, Crucial> Crucials = new HashMap<>();

    /**
     * 更新关键零部件清单
     */
    public static void reload() {
        Config config = new Config(Main.KeylistFile);
        Crucials.clear();
        for (Map.Entry<String, Object> entry : config.getMap().entrySet()) {
            if (entry.getKey() == null || entry.getKey().isEmpty() || !(entry.getValue() instanceof Map)) continue;
            Crucials.put(entry.getKey().toLowerCase(Locale.ROOT), new Crucial((Map<String, Object>) entry.getValue(), entry.getKey()));
        }
        Log.i("关键零部件清单更新完毕！");
    }

    /**
     * 定时更新缓存的关键零部件清单数据
     */
    @Override
    public void run() {
        Log.i("关键零部件解析进程已启动！");
        super.run();
        int time = 0;
        while (Main.runing) {
            if (Main.isTest) continue;
            Tool.sleep(1000);
            if (AutoMes.isIsSleep() || time-- > 0) continue;
            Log.i("准备更新已读取的关键零部件信息");
            time = Math.max(600, Main.getConfig().getInt("关键零部件清单更新间隔", 1200));
            reload();
        }
    }

    /**
     * 根据型号名称获取关键零部件清单
     *
     * @param model 需要获取关键零部件清单的型号
     * @return 获取到的关键零部件清单
     */
    public static Crucial getCrucial(String model) {
        if (model == null || model.isEmpty()) {
            Log.w("希望读取关键零部件清单的型号名称为空！");
            return null;
        }
        String Model = model;
        model = model.toLowerCase();
        Crucial crucial;
        if (Crucials.containsKey(model)) {
            crucial = Crucials.get(model);
            Log.i("已获取到专用" + crucial.getModel() + "关键零部件清单。");
            return crucial;
        }
        String string;
        for (String exp : exps)
            if (Crucials.containsKey(model + exp)) {
                crucial = Crucials.get(model + exp);
                Log.i("以获取到" + Model + "借用(" + crucial.getModel() + ")的关键零部件清单。");
                return crucial;
            } else if (model.endsWith(exp)) {
                string = model.substring(0, model.lastIndexOf(exp));
                if (Crucials.containsKey(string)) {
                    crucial = Crucials.get(string);
                    Log.i("以获取到" + Model + "借用(" + crucial.getModel() + ")的关键零部件清单。");
                    return crucial;
                }
            }
        for (Map.Entry<String, Crucial> entry : getCrucials().entrySet()) {
            if (!model.startsWith(entry.getKey())) continue;
            Log.i("已取得" + Model + "相关型号(" + entry.getValue().getModel() + ")的关键零部件清单！");
            return entry.getValue();
        }
        Log.w("无法获取到" + Model + "相关的关键零部件信息！");
        return null;
    }

    /**
     * @return 返回所有的关键零部件清单数据
     */
    public static Map<String, Crucial> getCrucials() {
        return new HashMap<>(Crucials);
    }

    /**
     * 判断是否是空关键零部件
     */
    public static boolean isEmpty() {
        return Crucials.isEmpty();
    }
}
