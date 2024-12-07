package cn.winfxk.lexy.amp.tool.view.list;

import cn.winfxk.lexy.amp.AutoMesException;

public class ListViewException extends AutoMesException {
    public ListViewException() {
        super();
    }

    public ListViewException(String messige) {
        super(messige);
    }

    public ListViewException(CloneNotSupportedException e) {
        super(e);
    }
}
