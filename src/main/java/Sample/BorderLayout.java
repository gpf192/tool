package Sample;

import java.awt.*;

public class BorderLayout {


    public static void main(String[] args) {
        // TODO 自动生成的方法存根
        Frame f = new Frame("BorderLayoutTest");
        f.setLayout(new java.awt.BorderLayout(30,5));
        f.add(new TextArea(20, 40), java.awt.BorderLayout.SOUTH);
        //f.add(new Button("北"),BorderLayout.NORTH);

        Panel p = new Panel();
        p.add(new TextField(20));
        p.add(new TextField(20));
        p.add(new TextField(20));
        p.add(new TextField(20));
        p.add(new TextField(20));
        p.add(new TextField(20));
        p.add(new Button("点击"));
        //默认添加到中间区域
        f.add(p);

        //f.add(new Button("东"),BorderLayout.EAST);
        //设置窗口为最佳大小
        f.pack();
        f.setVisible(true);
    }
}
