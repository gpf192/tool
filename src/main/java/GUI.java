import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GUI {

    public static void main(String[] args) {
        // 面板组件
        JPanel taskPanel = new JPanel();
        JPanel dbfPanel = new JPanel(new BorderLayout());
        JTabbedPane tabbedPane = buildJTabbedPane(taskPanel, dbfPanel);
        buildFrame(tabbedPane);
    }

    private static JTabbedPane buildJTabbedPane(JPanel taskPanel, JPanel dbPanel) {
        // 选项卡面板
        JTabbedPane tabbedPane = new JTabbedPane();
        // 通过BorderFactory来设置边框的特性
        tabbedPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        //tabbedPane.add("执行任务", taskPanel);
        tabbedPane.add("dbf文件提取", dbPanel);

        TextArea textArea = new TextArea(30, 1);
        textArea.setEditable(false);
        dbPanel.add(textArea, BorderLayout.SOUTH);
        //dbPanel.add(new Button("北"),BorderLayout.NORTH);

        String now_yyyyMMdd = new SimpleDateFormat("yyyyMMdd").format(new Date().getTime());
        Panel p = new Panel();
        p.add(buildJLabel("客户文件", 10, 0, 80, 25));
        JTextField customerFile = buildJTextField("D:\\两融文件\\两融产品发送清单.xlsx", "customerFile", 20, 100, 50, 165, 25);
        p.add(customerFile);

        p.add(buildJLabel("读取文件", 10, 0, 80, 25));
        JTextField readFile = buildJTextField("D:\\两融文件\\rzrq_dzls_" + now_yyyyMMdd + ".dbf", "readFile", 20, 100, 50, 165, 25);
        p.add(readFile);

        p.add(buildJLabel("输出路径", 10, 0, 80, 25));
        JTextField writePath = buildJTextField("D:\\两融文件\\输出", "writePath", 20, 100, 50, 165, 25);
        p.add(writePath);

        JButton executeButton = buildJButton("执行", 185, 230, 80, 25);
        addActionListener(executeButton, textArea, customerFile, readFile, writePath);
        p.add(executeButton);
        //默认添加到中间区域
        dbPanel.add(p);

        //dbPanel.add(new Button("东"),BorderLayout.EAST);
        dbPanel.setVisible(true);
        return tabbedPane;
    }

    private static void addActionListener(JButton executeButton, TextArea textArea, JTextField customerFile, JTextField readFile, JTextField writePath) {
        // 为按钮绑定监听器
        executeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setText("");
                new Dbf(textArea).handler(customerFile.getText(), readFile.getText(), writePath.getText());
                // 对话框
                JOptionPane.showMessageDialog(null, "执行完成！");
            }
        });
    }

    private static JButton buildJButton(String name, int x, int y, int width, int height) {
        JButton button = new JButton(name);
        button.setBounds(x, y, width, height);
        return button;
    }

    private static JTextField buildJTextField(String defaultValue, String name, int columns, int x, int y, int width, int height) {
        JTextField text = new JTextField(columns);
        text.setText(defaultValue);
        text.setName(name);
        text.setBounds(x, y, width, height);
        return text;
    }

    private static JLabel buildJLabel(String name, int x, int y, int width, int height) {
        JLabel label = new JLabel(name);
        label.setBounds(x, y, width, height);
        return label;
    }

    private static void buildFrame(JComponent component) {
        // 窗体容器
        JFrame frame = new JFrame("运营工具");
        frame.add(component);


        //  JFrame.EXIT_ON_CLOSE  退出
        //  JFrame.HIDE_ON_CLOSE  最小化隐藏
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 设置布局
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(BorderLayout.CENTER, component);
        // 设置窗口最小尺寸
        frame.setMinimumSize(new Dimension(1060, 560));
        // 调整此窗口的大小，以适合其子组件的首选大小和布局
        frame.pack();
        // 设置窗口相对于指定组件的位置
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        // 设置窗口尺寸是否固定不变
        frame.setResizable(true);
    }

}
