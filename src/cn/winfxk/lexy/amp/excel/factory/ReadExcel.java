package cn.winfxk.lexy.amp.excel.factory;

import cn.winfxk.lexy.amp.All;
import cn.winfxk.lexy.amp.Log;
import cn.winfxk.lexy.amp.excel.ExcelFactory;
import cn.winfxk.lexy.amp.tool.Tool;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.builder.ExcelReaderSheetBuilder;
import com.alibaba.excel.read.listener.ReadListener;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.biff.EmptyCell;

import javax.swing.JOptionPane;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用来读取Excel文件的封装类
 */
public class ReadExcel implements ReadListener {
    protected static final SimpleDateFormat mm_dd = new SimpleDateFormat("M/dd");
    private static final long Times = -2209017600000L;
    private final ExcelFactory excelFactory;
    protected final List<Object> baseList = new ArrayList<>();

    /**
     * 用来读取Excel文件的封装类
     */
    public ReadExcel(ExcelFactory excelFactory) {
        this.excelFactory = excelFactory;
    }

    /**
     * 用于总七的读取方式
     *
     * @return 数据列表
     */
    public List<Object> getBaseList(boolean isNet) {
        List<Object> list = new ArrayList<>(baseList);
        Log.e("生产计划读取出现异常！正在使用基础模式读取数据。");
        try {
            Workbook workbook = Workbook.getWorkbook(isNet ? ExcelFile.NetFile : excelFactory.Excelfile);
            Sheet sheet = workbook.getSheet(0);
            int rows = sheet.getRows();
            int cloum = sheet.getColumns();
            List<Object> errorList = new ArrayList<>();
            Map<Integer, Object> errorMap;
            Cell cell;
            int CentontInt;
            String Centont;
            for (int r = 0; r < rows; r++) {
                errorMap = new HashMap<>();
                for (int c = 0; c < cloum; c++) {
                    cell = sheet.getCell(c, r);
                    Centont = cell.getContents();
                    if (Tool.isInteger(Centont)) {
                        CentontInt = Tool.ObjToInt(Centont.replace(" ", ""));
                        if (CentontInt > 40000)
                            Centont = mm_dd.format(Times + (Tool.objToLong(CentontInt) - 2) * 86400000L);
                    }
                    errorMap.put(c, cell instanceof EmptyCell ? null : Centont);
                }
                errorList.add(errorMap);
            }
            if (list.size() < 10) {
                list.clear();
                Log.w("EasyExcel加载数据失败！尝试使用JXL加载...");
                list.addAll(errorList);
            }
        } catch (Exception ee) {
            Log.e("无法正常读取生产计划，请检查生产计划是否已被加密或格式出现重大变化", ee);
            JOptionPane.showMessageDialog(null, "无法正常读取生产计划，请检查生产计划是否已被加密或格式出现重大变化", "错误", JOptionPane.ERROR_MESSAGE);
        }
        return list;
    }

    /**
     * @return 返回当日计划文件内的所有计划表
     */
    public List<Object> getAllTask() {
        baseList.clear();
        List<Object> list = getEasyExcel(false);
        if (list == null || list.size() < 10) list = getJXL(false);
        if (list == null || list.size() < 10) list = getBaseList(false);
        if (ExcelFile.NetFile != null && ExcelFile.NetFile.exists() && (list == null || list.size() < 10)) {
            Log.i("本地备份的滚动计划均无法打开，尝试使用源文件打开中...");
            list = getEasyExcel(true);
            if (list == null || list.size() < 10)
                list = getJXL(true);
            if (list == null || list.size() < 10) list = getBaseList(true);
        }
        if (list == null || list.size() < 10) Log.e("无法获取计划！请检查计划文件是否被加密或使用的版本异常。");
        return list;
    }

    /**
     * 使用阿里API获取计划清单
     *
     * @return 计划列表
     */
    public List<Object> getEasyExcel(boolean isNet) {
        try {
            Log.i("准备使用阿里API获取当前计划文件的所有计划");
            ExcelReaderBuilder excel = EasyExcel.read(isNet ? ExcelFile.NetFile : excelFactory.Excelfile);
            excel.build();
            ExcelReaderSheetBuilder excelSheet = excel.sheet("主线计划");
            excelSheet.build();
            List<Object> list = new ArrayList<>(excelSheet.doReadSync());
            if (list.size() <= 10) {
                excelSheet = excel.sheet(1);
                excelSheet.build();
                list = new ArrayList<>(excelSheet.doReadSync());
                Log.i("使用index: “1”工作表为数据源");
            } else Log.i("使用“主线计划”工作表为数据源");
            if (list.size() < 10 || All.Codeindex == 7) {
                Log.w("无法获取主线计划，准备使用全局计划。");
                list.clear();
                list.addAll(excel.doReadAllSync());
            }
            return list;
        } catch (Exception e) {
            Log.e("无法使用阿里API打开这个文件！", e);
            return null;
        }
    }

    /**
     * 使用JXL API获取计划清单
     *
     * @return 计划列表
     */
    public List<Object> getJXL(boolean isNet) {
        try {
            Log.i("准备使用JXL API获取当前计划文件的所有计划");
            Workbook workbook = Workbook.getWorkbook(isNet ? ExcelFile.NetFile : excelFactory.Excelfile);
            Sheet sheet = workbook.getSheet(0);
            int rows = sheet.getRows();
            int cloum = sheet.getColumns();
            List<Object> errorList = new ArrayList<>();
            Map<Integer, Object> errorMap;
            Cell cell;
            int CentontInt;
            String Centont;
            for (int r = 0; r < rows; r++) {
                errorMap = new HashMap<>();
                for (int c = 0; c < cloum; c++) {
                    cell = sheet.getCell(c, r);
                    Centont = cell.getContents();
                    if (Tool.isInteger(Centont)) {
                        CentontInt = Tool.ObjToInt(Centont.replace(" ", ""));
                        if (CentontInt > 40000)
                            Centont = mm_dd.format(Times + (Tool.objToLong(CentontInt) - 2) * 86400000L);
                    }
                    errorMap.put(c, cell instanceof EmptyCell ? null : Centont);
                }
                errorList.add(errorMap);
            }
            return errorList;
        } catch (Exception e) {
            Log.e("无法使用JXL API打开这个Excel文件！请检查。", e);
            return null;
        }
    }

    @Override
    public void invoke(Object o, AnalysisContext analysisContext) {
        baseList.add(o);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
