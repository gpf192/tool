package Sample;

import java.awt.Button;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

public class MyWindowDemo {
    private Frame frame;
    private TextField tf;
    private Button but;
    private TextArea ta;
    private Dialog dlg;
    private Label lbl;
    private Button butOK;

    MyWindowDemo() {
        init();
    }

    public void init() {
        frame = new Frame("my Window");
        frame.setBounds(900, 400, 400, 500);
        frame.setLayout(new FlowLayout());

        tf = new TextField(30);
        but = new Button("转到");
        ta = new TextArea(20, 40);

        frame.add(tf);
        frame.add(but);
        frame.add(ta);
        ta.setEditable(false);
        frame.setVisible(true);
        myEvent();
        tf.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e){
                if(KeyEvent.getKeyText(e.getKeyCode())=="Enter")
                    doFile();
            }
        });
        but.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                doFile();
            }
        });
    }

    private void doFile(){
        ta.setText("2222222");
        File f = new File(tf.getText());
        if (!f.exists()) {
//			ta.setText("请重新输入地址");
            dlg = new Dialog(frame, "输入错误",false);
            lbl = new Label("\""+f.toString()+"\""+"不是一个有效路径");
            butOK = new Button("确定");
            dlg.setLayout(new FlowLayout());
            dlg.setBounds(900, 500, 300, 160);
            dlg.add(lbl);
            dlg.add(butOK);
            dlg.setVisible(true);
            dlg.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e){
                    dlg.setVisible(false);
                }
            });
            butOK.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    // TODO 自动生成的方法存根
                    dlg.setVisible(false);
                }
            });
        } else
            fileList(f);
    }
    private void fileList(File f) {
        // TODO 自动生成的方法存根
        File[] files = f.listFiles();

        for (File file : files) {
            ta.append(file.toString());
            ta.append("\r\n");
        }
        /*
         * for (File file : files) { if (!file.isHidden()) { if
         * (file.isDirectory()) { ta.append(file.toString()); ta.append("\r\n");
         * fileList(file); } else { ta.append(file.toString());
         * ta.append("\r\n"); // System.out.println(f); } } }
         */
    }

    private void myEvent() {
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        // TODO 自动生成的方法存根
        new MyWindowDemo();
    }
}

