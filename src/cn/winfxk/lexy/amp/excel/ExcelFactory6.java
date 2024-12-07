package cn.winfxk.lexy.amp.excel;

import java.io.File;

public class ExcelFactory6 extends ExcelFactory {
    public static final String defPath = "\\\\192.168.0.115\\数据交换\\计划调度中心\\孙婵姣\\分厂滚动计划\\";
    public static final String[] S1500Key = {"S1L00"};

    @Override
    public File getExcelPath() {
        return ExcelPath == null ? ExcelPath = new File(defPath) : ExcelPath;
    }

    @Override
    public String[] getS1500Key() {
        return S1500Key;
    }

    @Override
    public String getKey() {
        return "制造六部";
    }
}
