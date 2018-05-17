package ballot.view;

import java.awt.EventQueue;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import net.miginfocom.swing.MigLayout;


public class MigLayoutGaps2 extends JFrame {

    public MigLayoutGaps2() {

        initUI();

        setTitle("Gaps");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    private void initUI() {

        JPanel base = new JPanel(new MigLayout("flowy, ins 30, gap 15"));
        setContentPane(base);

        JPanel pnl1 = new JPanel();
        pnl1.setBorder(
                BorderFactory.createTitledBorder("Grid gaps")
        );

        pnl1.setLayout(new MigLayout("gap 5 5, ins 10, wrap 3"));

        pnl1.add(new JButton("1"));
        pnl1.add(new JButton("2"));
        pnl1.add(new JButton("3"));
        pnl1.add(new JButton("4"));
        pnl1.add(new JButton("5"));
        pnl1.add(new JButton("6"));

        JPanel pnl2 = new JPanel();
        pnl2.setBorder(
                BorderFactory.createTitledBorder("Column gaps")
        );

        pnl2.setLayout(new MigLayout("wrap 3", "[]10[]"));

        JLabel lbl1 = new JLabel();
        lbl1.setBorder(
            BorderFactory.createEtchedBorder()
        );

        JLabel lbl2 = new JLabel();
        lbl2.setBorder(
            BorderFactory.createEtchedBorder()
        );

        JLabel lbl3 = new JLabel();
        lbl3.setBorder(
            BorderFactory.createEtchedBorder()
        );        

        pnl2.add(lbl1, "w 40, h 110");
        pnl2.add(lbl2, "w 40, h 110");
        pnl2.add(lbl3, "w 40, h 110");

        JPanel pnl3 = new JPanel();
        pnl3.setBorder(
                BorderFactory.createTitledBorder("Row gaps")
        );

        pnl3.setLayout(new MigLayout("wrap", "", "[]15[]"));

        JLabel lbl4 = new JLabel();
        lbl4.setBorder(
            BorderFactory.createEtchedBorder()
        );

        JLabel lbl5 = new JLabel();
        lbl5.setBorder(
            BorderFactory.createEtchedBorder()
        );

        JLabel lbl6 = new JLabel();
        lbl6.setBorder(
            BorderFactory.createEtchedBorder()
        );        

        pnl3.add(lbl4, "w 150, h 20");
        pnl3.add(lbl5, "w 150, h 20");
        pnl3.add(lbl6, "w 150, h 20");        

        JPanel pnl4 = new JPanel();
        pnl4.setBorder(
                BorderFactory.createTitledBorder("Component gaps")
        );

        pnl4.setLayout(new MigLayout());

        pnl4.add(new JLabel("Name:"), "gapright 5");
        pnl4.add(new JTextField(10), "gapbottom 20, gaptop 20");

        base.add(pnl1);
        base.add(pnl2);
        base.add(pnl3);
        base.add(pnl4);

        pack();
    }

    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                MigLayoutGaps2 ex = new MigLayoutGaps2();
                ex.setVisible(true);
            }
        });
    }
}
