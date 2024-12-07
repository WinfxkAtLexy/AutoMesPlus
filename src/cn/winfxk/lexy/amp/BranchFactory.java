package cn.winfxk.lexy.amp;

import cn.winfxk.lexy.amp.excel.*;

import java.util.Arrays;
import java.util.List;

public enum BranchFactory {
    Factory1("总一外检", "5100", "http://192.168.0.65:8098/DMES/LMQ_IQC_sql", 1, ExcelFactory1.class),
    Factory2("总二外检", "5200", "http://192.168.95.11:8080/DMES/LMQ_IQC_sql", 2, ExcelFactory2.class),
    FactoryS("莱克净水", "5D00", "http://192.168.95.11:8080/DMES/LMQ_IQC_sql", 22, ExcelFactory22.class),
    // Factory3("总三外检", "5A00", "http://192.168.78.6:8080/DMES/LMQ_IQC_sql", 3, ExcelThread3.class),
    Factory5("总五外检", "6000", "http://192.168.82.11:8080/DMES/LMQ_IQC_sql", 5, ExcelFactory5.class),
    FactoryB("莱克电池", "1604", "http://192.168.82.11:8080/DMES/LMQ_IQC_sql", 55, ExcelFactory55.class),
    Factory6("总六外檢", "6300", "http://192.168.82.11:8080/DMES/LMQ_IQC_sql", 6, ExcelFactory6.class),
    Factory7("总七外檢", "7100", "http://192.168.82.11:8080/DMES/LMQ_IQC_sql", 7, ExcelFactory7.class);
    // Factory9("总九外检", "5C00", "http://192.168.78.6:8080/DMES/LMQ_IQC_sql", 9, ExcelThread9.class);
    public static final String[] Keys;
    private final String Name, Code, Host;
    private final int ID;
    private final Class<? extends ExcelFactory> factory;

    static {
        List<BranchFactory> list = Arrays.asList(values());
        Keys = new String[list.size()];
        for (int i = 0; i < list.size(); i++)
            Keys[i] = list.get(i).getName();
    }

    BranchFactory(String Name, String Code, String Host, int ID, Class<? extends ExcelFactory> factory) {
        this.Name = Name;
        this.Code = Code;
        this.Host = Host;
        this.ID = ID;
        this.factory = factory;
    }

    public static BranchFactory getFactory(int ID) {
        for (BranchFactory factory : values())
            if (factory.getID() == ID) return factory;
        return Factory5;
    }

    public static BranchFactory getFactory(String Name) {
        for (BranchFactory factory : values())
            if (factory.getName().equalsIgnoreCase(Name)) return factory;
        return Factory5;
    }

    public String getCode() {
        return Code;
    }

    public String getHost() {
        return Host;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return Name;
    }

    @Override
    public String toString() {
        return "BranchFactory[Name='" + Name + "', Code='" + Code + "', Host='" + Host + "', ID=" + ID + ", Factory=" + factory.toString() + ']';
    }

    public ExcelFactory start() throws InstantiationException, IllegalAccessException {
        ExcelFactory factory = this.factory.newInstance();
        factory.start();
        return factory;
    }
}
