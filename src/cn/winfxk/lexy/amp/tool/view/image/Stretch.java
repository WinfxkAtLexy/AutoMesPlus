package cn.winfxk.lexy.amp.tool.view.image;

public enum Stretch {
    Fit(0, "适合"), Tiled(1, "平铺"), FullScreen(2, "全屏"), Original(3, "原始");
    private final int type;
    private final String Name;

    Stretch(int type, String Name) {
        this.type = type;
        this.Name = Name;
    }

    /**
     * @return 图片拉升名称
     */
    public String getName() {
        return Name;
    }

    /**
     * @return 图片拉升方式
     */
    public int getType() {
        return type;
    }
}
