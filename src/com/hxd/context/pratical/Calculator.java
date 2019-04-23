package com.hxd.context.pratical;

/**
 * @author Qi Liu
 * @date 2019/4/13
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;

/**
 * 继承frame之后当前类就可以当作是一个面板，可以直接操作，方便
 * 实现ActionListener接口之后，可以统一管理组件的功能实现，不用再写n多个功能实现，减少代码冗余
 */
public class Calculator extends Frame implements ActionListener {
    //计算结果显示区
    private TextField resultField;
    // +-*/=命令
    private String operate;
    //保存计算结果
    private double result;
    //判断是否为数字的开始
    private boolean start;
    //用于初始化按钮的标签
    private String[] btName = {
            "sqrt", "1/x", "exp", "del", "C",
            "ln", "7", "8", "9", "/",
            "tan", "4", "5", "6", "*",
            "cos", "1", "2", "3", "-",
            "sin", ".", "0", "=", "+",
            "x^2", "x^3", "Binary", "Octal", "log10"
    };
    //每加一行按钮  扩充5个空间
    private Button[] bt = new Button[30];
    //每行一画布 一行五按钮
    private Panel[] panels = new Panel[6];

    //初始化计算器布局，按钮，以及将监听器绑定在按钮上
    public Calculator() {
        super("计算器");
        this.setLocation(350, 150);
        this.setSize(450, 400);
        this.setVisible(true);
        //控制总行数，现在有7行，1行显示框，6行按钮
        this.setLayout(new GridLayout(7, 1));
        resultField = new TextField(30);
        //切换背景颜色
        Color[] c = {Color.green, Color.CYAN, Color.BLUE, Color.GRAY, Color.MAGENTA, Color.orange, Color.pink, Color.red};
        resultField.setBackground(c[new Random().nextInt(c.length)]);
        //结果显示框不可编辑
        resultField.setEditable(false);
        Font font = new Font("微软雅黑", 1, 50);
        resultField.setFont(font);
        resultField.setText("0");
        this.add(resultField);

        //初始化结果状态
        start = true;
        result = 0;
        operate = "=";

        //创建出所有画布并将画布添加到当前面板  用网格布局，每行5个按钮，美观还大气
        for (int i = 0; i < panels.length; i++) {
            panels[i] = new Panel();
            panels[i].setLayout(new GridLayout(1, 5, 4, 4));
            panels[i].setBackground(c[new Random().nextInt(c.length)]);
            this.add(panels[i]);
        }

        //控制画布下标，添加按钮时使用
        int index = 0;
        //创建出所有按钮对象并绑定动作监听器
        for (int i = 0; i < btName.length; i++) {
            bt[i] = new Button(btName[i]);
            bt[i].addActionListener(this);
            //将按钮添加到对应位置
            panels[index].add(bt[i]);
            if ((i + 1) % 5 == 0) {
                index++;
            }
        }

        //添加窗口关闭动作监听器
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        this.setVisible(true);
    }

    //按钮的单击事件处理方法
    @Override
    public void actionPerformed(ActionEvent e) {
        Button bt = (Button) e.getSource();
        //获取按钮标签文本
        String label = bt.getLabel();
        //对于非运算符的处理方法
        if (label.equals("1") || label.equals("2") ||label.equals("3") || label.equals("4") ||
                label.equals("5") || label.equals("6") || label.equals("7") || label.equals("8") ||
                label.equals("9") || label.equals("0") || label.equals(".") ||label.equals("C") || label.equals("del")) {
            //如果当前操作时对计算器的第一次点击事件
            if (start) {
                //初始状态下，显示框显示0
                resultField.setText("0");
                start = false;
            }
            String str = resultField.getText();
            //退格键的实现方法
            if (label.equals("del")) {
                if (str.length() > 0)
                    resultField.setText(str.substring(0, str.length() - 1));
                //清零键的实现方法
            } else if (label.equals("C")) {
                resultField.setText("0");
                result = 0;
                start = true;
            } else if (label.equals(".")) {
                resultField.setText(resultField.getText() + label);
            }else {
                double v = Double.parseDouble(resultField.getText() + label);
                resultField.setText(showResult(v));
            }
            //对于运算符的操作
        } else {

            if (start) {
                operate = label;
            } else {
                calculate(Double.parseDouble(resultField.getText()));
                operate = label;
                start = true;
            }
        }
    }

    /**
     * 结果都是double类型的，所以一般都会以.0结尾，此时.0就没必要在显示了
     *
     * @param x double类型的运算结果
     * @return 字符类型的运算结果
     */
    public String showResult(double x) {
        String s = String.valueOf(x);
        if (s.endsWith(".0")) s = s.substring(0, s.length() - 2);
        return s;
    }

    /**
     * 点击运算符计算结果，然后将结果显示在结果框中
     *
     * @param x 结果显示框中用户已点击的数字
     */
    public void calculate(double x) {
        if (operate.equals("+")) result += x;
        if (operate.equals("-")) result -= x;
        if (operate.equals("*")) result *= x;
        if (operate.equals("/")) result /= x;
        if (operate.equals("=")) result = x;
        if (operate.equals("sqrt")) result = Math.sqrt(x);
        if (operate.equals("exp")) result = Math.exp(x);
        if (operate.equals("ln")) result = Math.log(x);
        if (operate.equals("tan")) result = Math.tan(x);
        if (operate.equals("cos")) result = Math.cos(x);
        if (operate.equals("sin")) result = Math.sin(x);
        if (operate.equals("x^2")) result = Math.pow(x, 2);
        if (operate.equals("x^3")) result = Math.pow(x, 3);
        if (operate.equals("Binary")) result = Integer.parseInt(Integer.toBinaryString((int) x));
        if (operate.equals("Octal")) result = Integer.parseInt(Integer.toOctalString((int) x));
        if (operate.equals("log10")) result = Math.log10(x);
        if (operate.equals("1/x")) result = 1 / x;
        resultField.setText(showResult(result));
    }

    public static void main(String args[]) {
        new Calculator();
    }
}
