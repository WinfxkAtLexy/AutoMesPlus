package cn.winfxk.lexy.amp.tool.view;

import cn.winfxk.lexy.amp.tool.Tool;

import java.awt.Component;
import java.awt.Font;
import java.awt.HeadlessException;
import javax.swing.*;

public class JOptionPane {
    public static final int ERROR_MESSAGE = 0;
    public static final int YES_NO_OPTION = 0;
    public static final int PLAIN_MESSAGE = -1;
    public static final int DEFAULT_OPTION = -1;
    public static final int WARNING_MESSAGE = 2;
    public static final int OK_CANCEL_OPTION = 2;
    public static final int QUESTION_MESSAGE = 3;
    public static final int INFORMATION_MESSAGE = 1;
    public static final int YES_NO_CANCEL_OPTION = 1;
    public static final Font font = new Font("黑体", Font.BOLD, 15);

    public static String showInputDialog(Component component, Object obj, String str, int i) throws HeadlessException {
        return javax.swing.JOptionPane.showInputDialog(component, getLabel(obj), str, i);
    }

    public static JLabel getLabel(Object obj) {
        JLabel jLabel = new JLabel("<html>" + Tool.objToString(obj).replace("\n", "<br>") + "</html>", SwingConstants.CENTER);
        jLabel.setFont(font);
        return jLabel;
    }

    public static void showMessageDialog(Component component, Object obj, String str, int i) {
        javax.swing.JOptionPane.showMessageDialog(component, getLabel(obj), str, i);
    }

    public static int showConfirmDialog(Component component, Object obj, String str, int i, int i2) {
        return javax.swing.JOptionPane.showConfirmDialog(component, getLabel(obj), str, i, i2);
    }

    public static void showMessageDialog(Component component, Object s) {
        showMessageDialog(component, s, "提示", PLAIN_MESSAGE);
    }
}
